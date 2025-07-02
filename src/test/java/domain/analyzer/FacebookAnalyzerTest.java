package domain.analyzer;

import domain.model.RiskLevel;
import domain.model.SocialMention;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class FacebookAnalyzerTest {

    private FacebookAnalyzer analyzer;

    @BeforeEach
    void setUp() {
        analyzer = new FacebookAnalyzer();
    }

    @Test
    void shouldSupportFacebookMention() {
        // Given
        SocialMention mention = new SocialMention();
        mention.setFacebookAccount("fb.com/xyz");

        // When
        boolean supports = analyzer.supports(mention);

        // Then
        assertTrue(supports);
    }

    @Test
    void shouldNotSupportNonFacebookMention() {
        // Given
        SocialMention mention = new SocialMention();
        mention.setTweeterAccount("@usuario");

        // When
        boolean supports = analyzer.supports(mention);

        // Then
        assertFalse(supports);
    }

    @Test
    void shouldAnalyzeFacebookMentionWithComments() {
        // Given
        SocialMention mention = new SocialMention();
        mention.setMessage("Test message");
        mention.setFacebookAccount("fb.com/xyz");
        mention.setFacebookComments(Arrays.asList("comment1", "comment2"));

        // When
        RiskLevel result = analyzer.analyze(mention);

        // Then
        assertNotNull(result);
        assertTrue(Arrays.asList(RiskLevel.HIGH_RISK, RiskLevel.MEDIUM_RISK, RiskLevel.LOW_RISK).contains(result));
    }

    @Test
    void shouldAnalyzeFacebookMentionWithoutComments() {
        // Given
        SocialMention mention = new SocialMention();
        mention.setMessage("Test message");
        mention.setFacebookAccount("fb.com/xyz");

        // When
        RiskLevel result = analyzer.analyze(mention);

        // Then
        assertNotNull(result);
        assertTrue(Arrays.asList(RiskLevel.HIGH_RISK, RiskLevel.MEDIUM_RISK, RiskLevel.LOW_RISK).contains(result));
    }

    @Test
    void shouldCalculateFacebookCommentsScore() {
        // Given
        String shortComments = "short comment";
        String longComments = "very long comment that exceeds the threshold for high risk analysis and should be more than 100 characters to trigger the lower score calculation. This comment needs to be long enough to test the boundary condition properly.";

        // When
        double shortScore = FacebookAnalyzer.calculateFacebookCommentsScore(shortComments);
        double longScore = FacebookAnalyzer.calculateFacebookCommentsScore(longComments);

        // Then
        assertEquals(60d, shortScore);
        assertEquals(30d, longScore);
    }

    @Test
    void shouldAnalyzePost() {
        // Given
        String shortMessage = "short";
        String longMessage = "very long message that exceeds the threshold for analysis and should be more than 200 characters to trigger the lower score calculation. This message needs to be long enough to test the boundary condition properly.";

        // When
        double shortScore = FacebookAnalyzer.analyzePost(shortMessage, "fb.com/xyz");
        double longScore = FacebookAnalyzer.analyzePost(longMessage, "fb.com/xyz");

        // Then
        assertEquals(80d, shortScore);
        assertEquals(40d, longScore);
    }
} 