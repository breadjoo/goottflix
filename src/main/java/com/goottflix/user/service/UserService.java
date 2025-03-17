package com.goottflix.user.service;

import com.goottflix.common.FileService;
import com.goottflix.user.model.UpdateDTO;
import com.goottflix.user.model.repository.UserMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final FileService fileService;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Transactional
    public void updateProfile(UpdateDTO user, MultipartFile file) throws IOException {

        if (user != null) {

            String existImage = userMapper.findImageById(user.getId());

            if (!file.isEmpty()) {
                String newImageUrl = fileService.saveFile(file);
                user.setProfileImage(newImageUrl);
                if (existImage != null) {
                    fileService.deleteExistingFile(existImage);
                }
            }

            user.setId(user.getId());
            user.setUsername(user.getUsername());
            user.setBirth(user.getBirth());
            user.setGender(user.getGender());

            try {
                userMapper.updateProfile(user);
                logger.info("Updated profile: {}", user);

            } catch (Exception e) {
                logger.error("Error updating profile", e);
            }

        } else {
            throw new IllegalArgumentException("User is null");
        }
    }

//    private String handleFileUpload(MultipartFile file) throws IOException {
//        String projectPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files";
//        UUID uuid = UUID.randomUUID();
//        String fileName = uuid + "_" + file.getOriginalFilename();
//        File saveFile = new File(projectPath, fileName);
//        file.transferTo(saveFile);
//        return "/files/"+fileName;
//    }
//
//    private void deleteExistingFile(String imageUrl) {
//        File file = new File(System.getProperty("user.dir") + "\\src\\main\\resources\\static" + imageUrl);
//        if (file.exists()) {
//            file.delete();
//        }
//    }

    public String findUsernameByuserId(Long userId){
        return userMapper.findByUserId(userId).getUsername();
    }

    public String getUserSubscribe(Long userId){
        return userMapper.getUserSubscribe(userId);
    }

    public String requestVerifyCode(){
        String verificationCode = String.format("%06d", new Random().nextInt(1000000));
        return verificationCode;
    }


}
