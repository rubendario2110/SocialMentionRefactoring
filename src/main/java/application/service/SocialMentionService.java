package application.service;

import domain.analyzer.FacebookAnalyzer;
import domain.analyzer.SocialAnalyzer;
import domain.analyzer.TwitterAnalyzer;
import domain.model.RiskLevel;
import domain.model.SocialMention;
import infrastructure.persistence.DBService;
import infrastructure.persistence.SocialMentionRepository;

import java.util.List;

public class SocialMentionService {

    private final List<SocialAnalyzer> analyzers = List.of(
        new FacebookAnalyzer(),
        new TwitterAnalyzer()
    );

    private final SocialMentionRepository repository;
    private final DBService dbService;

    public SocialMentionService(SocialMentionRepository repository, DBService dbService) {
        this.repository = repository;
        this.dbService = dbService;
    }

    public RiskLevel analyze(SocialMention mention) {
        boolean isFacebook = mention.getFacebookAccount() != null;
        boolean isTweeter = mention.getTweeterAccount() != null;
        
        if (!isFacebook && !isTweeter) {
            throw new IllegalArgumentException("Error, Tweeter or Facebook account must be present");
        }
        
        return analyzers.stream()
            .filter(analyzer -> analyzer.supports(mention))
            .map(analyzer -> {
                RiskLevel level = analyzer.analyze(mention);
                
                // Persistir en base de datos
                if (isFacebook) {
                    double score = getFacebookScore(level);
                    dbService.insertFBPost(
                        DBService.ANALYZED_FB_TABLE,
                        score,
                        mention.getMessage(),
                        mention.getFacebookAccount()
                    );
                } else if (isTweeter) {
                    double score = getTweeterScore(level);
                    dbService.insertTweet(
                        DBService.ANALYZED_TWEETS_TABLE,
                        score,
                        mention.getMessage(),
                        mention.getTweeterUrl(),
                        mention.getTweeterAccount()
                    );
                }
                
                repository.save(mention, level);
                return level;
            })
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("No valid account found"));
    }
    
    private double getFacebookScore(RiskLevel level) {
        switch (level) {
            case HIGH_RISK: return -100d;
            case MEDIUM_RISK: return 25d;
            case LOW_RISK: return 75d;
            default: return 0d;
        }
    }
    
    private double getTweeterScore(RiskLevel level) {
        switch (level) {
            case HIGH_RISK: return -0.7d;
            case MEDIUM_RISK: return 0.3d;
            case LOW_RISK: return 0.8d;
            default: return 0d;
        }
    }
}