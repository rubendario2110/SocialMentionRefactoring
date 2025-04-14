package domain.model;

import java.util.List;

public class SocialMention {
    private String message;
    private String facebookAccount;
    private String twitterAccount;
    private String creationDate;
    private String twitterUrl;
    private List<String> facebookComments;

    // Getters and Setters
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getFacebookAccount() { return facebookAccount; }
    public void setFacebookAccount(String facebookAccount) { this.facebookAccount = facebookAccount; }

    public String getTwitterAccount() { return twitterAccount; }
    public void setTwitterAccount(String twitterAccount) { this.twitterAccount = twitterAccount; }

    public String getCreationDate() { return creationDate; }
    public void setCreationDate(String creationDate) { this.creationDate = creationDate; }

    public String getTwitterUrl() { return twitterUrl; }
    public void setTwitterUrl(String twitterUrl) { this.twitterUrl = twitterUrl; }

    public List<String> getFacebookComments() { return facebookComments; }
    public void setFacebookComments(List<String> facebookComments) { this.facebookComments = facebookComments; }
}