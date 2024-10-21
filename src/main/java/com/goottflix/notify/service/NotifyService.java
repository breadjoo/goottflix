package com.goottflix.notify.service;


import com.goottflix.movie.mapper.MovieMapper;
import com.goottflix.movie.model.Movie;
import com.goottflix.notify.entity.NotifyEntity;
import com.goottflix.notify.entity.repository.NotifyMapper;
import com.goottflix.user.model.repository.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
@Service
@RequiredArgsConstructor
public class NotifyService {

    private final NotifyMapper notifyMapper;
    private final UserMapper userMapper;
    private final Map<Long, SseEmitter> clients = new ConcurrentHashMap<>();
    private final MovieMapper movieMapper;

    // 영화 추가 알림 메소드
    public void addMovieUpdate(Long movieId) {

        List<Long> userIds = userMapper.findAllUserId();
        Movie movie = movieMapper.findById(movieId);
        for (Long id : userIds) {
            NotifyEntity notify = new NotifyEntity();
            notify.setContent("주목할만한 영화 "+movie.getTitle()+"가 업데이트 되었습니다");
            notify.setUrl("/description");
            notify.setIsRead(false);
            notify.setNotifyType(NotifyEntity.NotifyType.movieUpdate);
            notify.setUserId(id);
            notify.setMovieId(movieId);
            // 새로운 알림을 DB에 저장한다잉
            notifyMapper.insertNotify(notify);
            sendNotify(id, notify);
        }

    }

    // 친구 추가 알림 메소드
    public void addFriendUpdate(Long userId) {
        NotifyEntity notify = new NotifyEntity();
        notify.setContent("새로운 친구가 추가되었습니다");
        notify.setUrl("/myFriendList"); // 프론트에서 url 설정하는거에 따라 변동
        notify.setIsRead(false);
        notify.setNotifyType(NotifyEntity.NotifyType.friendadd);
        notify.setUserId(userId);

        notifyMapper.insertNotify(notify);

        sendNotify(userId, notify);
    }


    // sse로 알림 전송하는 메소드
    private void sendNotify(Long userId, NotifyEntity notify) {
        SseEmitter emitter = clients.get(userId);
        System.out.println("작동 테스트 1");
        if (emitter != null) {
            try {
                System.out.println("트라이문 들어왔음");
                emitter.send(SseEmitter.event().name("notify").data(notify));
            } catch (IOException e) {
                System.out.println("sse오류낫음");
                clients.remove(userId);
            }
        }
    }

    public SseEmitter subscribe(Long userId) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE); //Long.MAX_VALUE 클라이너트 구독시간 무제한
        clients.put(userId, emitter);
        emitter.onCompletion(() -> clients.remove(userId)); // 연결 종료시 제거
        emitter.onTimeout(() -> clients.remove(userId)); // 시간 지나면 제거
        return emitter;
    }

    // 알림 확인 메서드
    public void notifyRead(Long userId, Long notifyId) {
        notifyMapper.updateIsRead(userId, notifyId);
    }

    // 알림 전체 조회 메소드
    public List<NotifyEntity> getAllNotify(Long userId) {
        return notifyMapper.findAllUserNotify(userId);
    }

    // 알림 삭제
    public void deleteNotify(Long notifyId) {
        notifyMapper.deleteNotify(notifyId);
    }


}
