package application.service;

import domain.analyzer.FacebookAnalyzer;
import domain.analyzer.SocialAnalyzer;
import domain.analyzer.TwitterAnalyzer;
import domain.model.RiskLevel;
import domain.model.SocialMention;
import infrastructure.persistence.SocialMentionRepository;

import java.util.List;

public class SocialMentionService {

    private final List<SocialAnalyzer> analyzers = List.of(
        new FacebookAnalyzer(),
        new TwitterAnalyzer()
    );

    private final SocialMentionRepository repository;

    public SocialMentionService(SocialMentionRepository repository) {
        this.repository = repository;
    }

    public RiskLevel analyze(SocialMention mention) {
        return analyzers.stream()
            .filter(analyzer -> analyzer.supports(mention))
            .map(analyzer -> {
                RiskLevel level = analyzer.analyze(mention);
                repository.save(mention, level);
                return level;
            })
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("No valid account found"));
    }
}