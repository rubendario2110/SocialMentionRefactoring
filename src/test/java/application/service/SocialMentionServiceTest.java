package application.service;

import domain.analyzer.SocialAnalyzer;
import domain.model.RiskLevel;
import domain.model.SocialMention;
import infrastructure.persistence.SocialMentionRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SocialMentionServiceTest {

    @Test
    void shouldAnalyzeFacebookMention() {
        SocialMention mention = new SocialMention();
        mention.setFacebookAccount("fb.com/xyz");

        SocialMentionRepository mockRepo = (m, r) -> {};
        SocialAnalyzer mockAnalyzer = new SocialAnalyzer() {
            public boolean supports(SocialMention m) { return true; }
            public RiskLevel analyze(SocialMention m) { return RiskLevel.MEDIUM_RISK; }
        };

        SocialMentionService service = new SocialMentionService(mockRepo);
        RiskLevel result = service.analyze(mention);
        assertEquals(RiskLevel.LOW_RISK, result);
    }
}