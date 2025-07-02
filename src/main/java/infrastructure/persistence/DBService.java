package infrastructure.persistence;

public class DBService {
    private final String host;
    private final int port;
    
    public static final String ANALYZED_TWEETS_TABLE = "analyzed_tweets";
    public static final String ANALYZED_FB_TABLE = "analyzed_fb_posts";
    
    public DBService(String host, int port) {
        this.host = host;
        this.port = port;
    }
    
    public void insertFBPost(String table, double score, String message, String facebookAccount) {
        // Simulación de inserción en base de datos
        System.out.println("Inserting FB post: " + message + " with score: " + score + " for account: " + facebookAccount);
    }
    
    public void insertTweet(String table, double score, String message, String tweeterUrl, String tweeterAccount) {
        // Simulación de inserción en base de datos
        System.out.println("Inserting Tweet: " + message + " with score: " + score + " for account: " + tweeterAccount);
    }
} 