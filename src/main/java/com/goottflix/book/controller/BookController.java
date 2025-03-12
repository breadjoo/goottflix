package com.goottflix.book.controller;

import com.goottflix.book.model.BookInfo;
import com.goottflix.book.model.NfcRequest;
import com.goottflix.book.model.repository.CardMapper;
import com.goottflix.book.service.CardService;
import com.goottflix.book.service.NfcService;
import com.goottflix.user.jwt.JWTUtil;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.time.Duration;

@RestController
@RequestMapping("/book")
public class BookController {

    private final Sinks.Many<String> sink;  // SSE 스트림을 위한 sink
    private final CardService cardService;
    private final JWTUtil jwtUtil;
    private final NfcService nfcService;
    private final CardMapper cardMapper;

    public BookController(CardService cardService, JWTUtil jwtUtil, NfcService nfcService, CardMapper cardMapper) {
        this.sink = Sinks.many().multicast().onBackpressureBuffer();
        this.cardService = cardService;
        this.jwtUtil = jwtUtil;
        this.nfcService = nfcService;
        this.cardMapper = cardMapper;
    }

    // SSE 엔드포인트: 실시간으로 NFC 데이터를 클라이언트로 전송
    @GetMapping(value = "/nfc-data", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamNfcData() {
        return sink.asFlux().delayElements(Duration.ofMillis(100));
    }
    @PostMapping("/nfc-data")
    public ResponseEntity<String> receiveNfcData(@RequestBody NfcRequest nfcRequest) {
        String uid = nfcRequest.getUid();
        System.out.println("Received NFC UID: " + uid);
        System.out.println("250312 배포용 푸쉬");
        sink.tryEmitNext(uid);
        nfcService.sendNfcData(uid);
        return ResponseEntity.ok("NFC data processed successfully.");
    }

    @PostMapping("/save-card")
    public ResponseEntity<?> registerCard(@CookieValue("Authorization") String token,
                                          @RequestParam String uid) {
        Long userId = jwtUtil.getUserID(token);
        try {
            cardService.registerCard(uid, userId);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/process-card")
    public ResponseEntity<?> processCard(@CookieValue("Authorization") String token,
                                         @RequestBody NfcRequest nfcRequest) {
        Long userId = jwtUtil.getUserID(token);
        String uid = nfcRequest.getUid();
        String mode = nfcRequest.getMode();

        if ("register".equals(mode)) {
            if (cardService.isCardRegistered(uid)) {
                return ResponseEntity.ok("이미 등록된 카드입니다.");
            } else {
                cardService.saveCard(uid);
                return ResponseEntity.ok("성공적으로 카드를 등록했습니다.");
            }
        } else if ("validate".equals(mode)) {
            if (cardService.checkBook(uid, userId)) {
                cardService.enterPerson(uid);
                BookInfo bookInfo = cardMapper.findBookInfo(uid);
                return ResponseEntity.ok(bookInfo);
            }
        } else if ("registerUser".equals(mode)) {
            if (cardService.isCardRegistered(uid)) {
                cardService.registerCard(uid, userId);
                return ResponseEntity.ok("카드에 로그인정보를 저장했습니다.");
            }
        }
        return ResponseEntity.badRequest().body("사용할 수 없는 카드입니다.");
    }
}
