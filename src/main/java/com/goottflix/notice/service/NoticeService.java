package com.goottflix.notice.service;

import com.goottflix.common.FileService;
import com.goottflix.notice.model.Notice;
import com.goottflix.notice.model.repository.NoticeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeMapper noticeMapper;
    private final FileService fileService;

    //파일 업로드하기
//
//    private String handleFileUpload(MultipartFile file) throws IOException {
//        String projectPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files";
//        UUID uuid = UUID.randomUUID();
//        String fileName = uuid + "_" + file.getOriginalFilename();
//        File saveFile = new File(projectPath, fileName);
//        file.transferTo(saveFile);
//        return "/files/" + fileName;
//    }

    //파일 삭제
//    private void deleteExistFile(Notice notice) {
//        File originFile = new File(System.getProperty("user.dir") + "\\src\\main\\resources\\statuc" + notice.getImage());
//        if (originFile.exists()) {
//            originFile.delete();
//        }
//    }
    // 공지사항 작성
    public void save(Long writer, Notice notice, MultipartFile file) throws IOException {

        if(file != null && !file.isEmpty()) {
            notice.setImage(fileService.saveFile(file));
        }
        if (notice.getId()==null) {
            notice.setWriter(writer);
            noticeMapper.save(notice);
        } else {
            if (file != null && file.isEmpty()) {
                fileService.deleteExistingFile(notice.getImage());
            }
            noticeMapper.update(notice);
        }
    }

    public void modify (Notice notice, MultipartFile file) throws IOException {
        if (notice.getId() == null) {
            throw new IllegalArgumentException("공지사항 ID가 필요합니다.");
        }
        if (!file.isEmpty()) {
            fileService.deleteExistingFile(notice.getImage());
            notice.setImage(fileService.saveFile(file));
        }
        noticeMapper.update(notice);
    }

    //공지사항 불러오기
    public List<Notice> getAllNotices() {
        return noticeMapper.findAllSort();
    }


}
