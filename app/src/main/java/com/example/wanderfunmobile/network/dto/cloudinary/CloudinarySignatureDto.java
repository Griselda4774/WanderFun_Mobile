package com.example.wanderfunmobile.network.dto.cloudinary;

public class CloudinarySignatureDto {
    private String signature;
    private String apiKey;
    private String cloudName;

    public CloudinarySignatureDto() {};

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getCloudName() {
        return cloudName;
    }

    public void setCloudName(String cloudName) {
        this.cloudName = cloudName;
    }
}
