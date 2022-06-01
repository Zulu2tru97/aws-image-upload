package com.manifest.awsimageupload.profile;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/user-profile")
@CrossOrigin("http://localhost:3000")

public class UserProfileController {
    private UserProfileService userProfileService;

    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @GetMapping
    public List<UserProfile> getUserProfiles() {
        return  userProfileService.getUserProfiles();
    }

    @PostMapping(
            path = "{userProfileId}/image/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public void uploadUserProfileImage(@PathVariable("userProfileId") String userProfileId,
                                       @RequestParam("file") MultipartFile file)  {
        userProfileService.uploadUserProfileImage(userProfileId, file);
    }

//    @GetMapping("{userProfileId}/image/download")
//    public byte[] downloadUserProfileImage(@PathVariable("userProfileId") UUID userProfileId) {
//        return userProfileService.downloadUserProfileImage(userProfileId);
//    }
}
