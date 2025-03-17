package com.goottflix.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class FileService {

    @Value("${file.upload-dir:/uploads}")
    private String uploadDir;

    // 파일 저장
    public String saveFile(MultipartFile file) throws IOException {
        UUID uuid = UUID.randomUUID();
        String fileName = uuid + "_" + file.getOriginalFilename();

        File saveFile = new File(uploadDir, fileName);
        saveFile.getParentFile().mkdirs();
        file.transferTo(saveFile);

        return "/files/" + fileName;  // 프론트에 반환할 URL
    }

    // 파일 삭제
    public void deleteExistingFile(String fileUrl) {
        String filePath = uploadDir + fileUrl.replace("/files", "");
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }
}
