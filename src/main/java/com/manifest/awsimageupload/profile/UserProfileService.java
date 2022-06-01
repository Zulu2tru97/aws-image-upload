package com.manifest.awsimageupload.profile;

import com.manifest.awsimageupload.bucket.BucketName;
import com.manifest.awsimageupload.filestore.FileStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

@Service
public class UserProfileService {



    private  final FileStore fileStore;
    private  final UserProfileDataAccessService userProfileDataAccessService;

    @Autowired
    public UserProfileService(FileStore fileStore, UserProfileDataAccessService userProfileDataAccessService) {
        this.fileStore = fileStore;


        this.userProfileDataAccessService = userProfileDataAccessService;
    }

    List<UserProfile> getUserProfiles() {
        return userProfileDataAccessService.getUserProfiles();
    }

    public void uploadUserProfileImage(String userProfileId, MultipartFile file)  {
        if (!file.isEmpty()) {
            isImage(userProfileId, file);

        }

        else { throw new IllegalStateException("file not uploaded");}
    }

    private void isImage(String userProfileId, MultipartFile file)  {
        if (file.getContentType().contains("image")) {

            checkProfiles(userProfileId, file);
        }

        else {  throw new IllegalStateException("file not image");}
    }

    private void checkProfiles(String userProfileId, MultipartFile file) {
        getUserProfiles().forEach(getUserProfileConsumer(userProfileId, file));
    }

    private Consumer<UserProfile> getUserProfileConsumer(String userProfileId, MultipartFile file) {
        return userProfile -> {
            checkDB(userProfileId, file, userProfile);
            ;
        };
    }

    private void checkDB(String userProfileId, MultipartFile file, UserProfile userProfile) {
        if (userProfile.getUserProfileId().equals(userProfileId)) {

            sendToS3(userProfileId, file);

        }
    }

    private void sendToS3(String userProfileId, MultipartFile file) {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Name", file.getName());
        metadata.put("Size", String.valueOf(file.getSize()));


        String path = String.format("%s/%s", BucketName.PROFILE_IMAGE.getBucketname(), userProfileId);
        String filename = String.format("%s-%s", file.getName(), UUID.randomUUID().toString());

        try {
            fileStore.save(path,filename, Optional.of(metadata), file.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


//    public byte[] downloadUserProfileImage(UUID userProfileId) {
//    }
}
