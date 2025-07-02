package domain.model;

import java.util.List;

public class SocialMention {
    private String message;
    private String facebookAccount;
    private String tweeterAccount;
    private String creationDate;
    private String tweeterUrl;
    private List<String> facebookComments;

    // Getters and Setters
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getFacebookAccount() { return facebookAccount; }
    public void setFacebookAccount(String facebookAccount) { this.facebookAccount = facebookAccount; }

    public String getTweeterAccount() { return tweeterAccount; }
    public void setTweeterAccount(String tweeterAccount) { this.tweeterAccount = tweeterAccount; }

    public String getCreationDate() { return creationDate; }
    public void setCreationDate(String creationDate) { this.creationDate = creationDate; }

    public String getTweeterUrl() { return tweeterUrl; }
    public void setTweeterUrl(String tweeterUrl) { this.tweeterUrl = tweeterUrl; }

    public List<String> getFacebookComments() { return facebookComments; }
    public void setFacebookComments(List<String> facebookComments) { this.facebookComments = facebookComments; }
}