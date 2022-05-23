package com.manifest.awsimageupload.bucket;

public enum BucketName {

    PROFILE_IMAGE("aws-image-upload0");

    private final String bucketname;

    BucketName(String bucketname) {
        this.bucketname = bucketname;
    }

    public String getBucketname() {
        return bucketname;
    }
}
