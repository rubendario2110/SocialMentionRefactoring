package domain.analyzer;

import domain.model.RiskLevel;
import domain.model.SocialMention;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class TwitterAnalyzerTest {

    private TwitterAnalyzer analyzer;

    @BeforeEach
    void setUp() {
        analyzer = new TwitterAnalyzer();
    }

    @Test
    void shouldSupportTwitterMention() {
        // Given
        SocialMention mention = new SocialMention();
        mention.setTweeterAccount("@usuario");

        // When
        boolean supports = analyzer.supports(mention);

        // Then
        assertTrue(supports);
    }

    @Test
    void shouldNotSupportNonTwitterMention() {
        // Given
        SocialMention mention = new SocialMention();
        mention.setFacebookAccount("fb.com/xyz");

        // When
        boolean supports = analyzer.supports(mention);

        // Then
        assertFalse(supports);
    }

    @Test
    void shouldAnalyzeTwitterMention() {
        // Given
        SocialMention mention = new SocialMention();
        mention.setMessage("Test tweet");
        mention.setTweeterAccount("@usuario");
        mention.setTweeterUrl("https://twitter.com/usuario/status/123");

        // When
        RiskLevel result = analyzer.analyze(mention);

        // Then
        assertNotNull(result);
        assertTrue(Arrays.asList(RiskLevel.HIGH_RISK, RiskLevel.MEDIUM_RISK, RiskLevel.LOW_RISK).contains(result));
    }

    @Test
    void shouldAnalyzeTweet() {
        // Given
        String shortMessage = "short tweet";
        // Create a message with exactly 300 characters to ensure it's over 280
        String longMessage = "This is a very long tweet that needs to exceed 280 characters to trigger the penalty in the Twitter analyzer. Let me add more text to make sure we reach the required length. This should be more than enough characters to trigger the penalty. Adding even more text to ensure we reach the 280 character limit. This is a very long tweet that should definitely exceed the Twitter character limit and trigger the score reduction penalty. Adding even more text to ensure we reach the 280 character limit. This should be more than enough characters to trigger the penalty in the Twitter analyzer. Adding even more text to ensure we reach the 280 character limit. This should be more than enough characters to trigger the penalty in the Twitter analyzer.";
        String tweeterUrl = "https://twitter.com/usuario/status/123";
        String tweeterAccount = "@usuario";

        // When
        double shortScore = TwitterAnalyzer.analyzeTweet(shortMessage, tweeterUrl, tweeterAccount);
        double longScore = TwitterAnalyzer.analyzeTweet(longMessage, tweeterUrl, tweeterAccount);

        // Then
        assertEquals(0.8d, shortScore, 0.001); // 0.6 + 0.1 + 0.1
        assertEquals(0.6d, longScore, 0.001); // 0.6 - 0.2 + 0.1 + 0.1 = 0.6
    }

    @Test
    void shouldAnalyzeTweetWithoutUrl() {
        // Given
        String message = "Test tweet";
        String tweeterAccount = "@usuario";

        // When
        double score = TwitterAnalyzer.analyzeTweet(message, null, tweeterAccount);

        // Then
        assertEquals(0.7d, score, 0.001); // 0.6 + 0.1
    }

    @Test
    void shouldAnalyzeTweetWithoutAccount() {
        // Given
        String message = "Test tweet";
        String tweeterUrl = "https://twitter.com/usuario/status/123";

        // When
        double score = TwitterAnalyzer.analyzeTweet(message, tweeterUrl, null);

        // Then
        assertEquals(0.7d, score, 0.001); // 0.6 + 0.1
    }

    @Test
    void shouldVerifyLongMessageLength() {
        // Given
        String longMessage = "This is a very long tweet that needs to exceed 280 characters to trigger the penalty in the Twitter analyzer. Let me add more text to make sure we reach the required length. This should be more than enough characters to trigger the penalty. Adding even more text to ensure we reach the 280 character limit. This is a very long tweet that should definitely exceed the Twitter character limit and trigger the score reduction penalty. Adding even more text to ensure we reach the 280 character limit. This should be more than enough characters to trigger the penalty in the Twitter analyzer.";
        
        // When
        int length = longMessage.length();
        double score = TwitterAnalyzer.analyzeTweet(longMessage, "https://twitter.com/usuario/status/123", "@usuario");
        
        // Then
        System.out.println("Message length: " + length);
        System.out.println("Calculated score: " + score);
        assertTrue(length > 280, "Message should be longer than 280 characters, but was: " + length);
        assertEquals(0.6d, score, 0.001); // 0.6 - 0.2 + 0.1 + 0.1 = 0.6
    }

    @Test
    void shouldDebugAnalyzeTweet() {
        // Given - Create a message with exactly 281 characters
        StringBuilder messageBuilder = new StringBuilder();
        for (int i = 0; i < 281; i++) {
            messageBuilder.append("a");
        }
        String message = messageBuilder.toString();
        
        // When
        int length = message.length();
        double score = TwitterAnalyzer.analyzeTweet(message, "https://twitter.com/usuario/status/123", "@usuario");
        
        // Then
        System.out.println("=== DEBUG INFO ===");
        System.out.println("Message length: " + length);
        System.out.println("Is longer than 280: " + (length > 280));
        System.out.println("Calculated score: " + score);
        System.out.println("Expected score: 0.6");
        System.out.println("==================");
        
        // Manual calculation for debugging
        double expectedScore = 0.6d;
        if (length > 280) {
            expectedScore -= 0.2;
            System.out.println("Applied penalty: -0.2");
        }
        if ("https://twitter.com/usuario/status/123" != null && !"https://twitter.com/usuario/status/123".isEmpty()) {
            expectedScore += 0.1;
            System.out.println("Applied URL bonus: +0.1");
        }
        if ("@usuario" != null && "@usuario".startsWith("@")) {
            expectedScore += 0.1;
            System.out.println("Applied account bonus: +0.1");
        }
        System.out.println("Manual calculation result: " + expectedScore);
        
        // Let's also test the method step by step
        System.out.println("=== STEP BY STEP DEBUG ===");
        double stepScore = 0.6d;
        System.out.println("Initial score: " + stepScore);
        
        boolean isLong = length > 280;
        System.out.println("Is message long (>280): " + isLong);
        if (isLong) {
            stepScore -= 0.2;
            System.out.println("After penalty: " + stepScore);
        }
        
        boolean hasUrl = "https://twitter.com/usuario/status/123" != null && !"https://twitter.com/usuario/status/123".isEmpty();
        System.out.println("Has URL: " + hasUrl);
        if (hasUrl) {
            stepScore += 0.1;
            System.out.println("After URL bonus: " + stepScore);
        }
        
        boolean hasAccount = "@usuario" != null && "@usuario".startsWith("@");
        System.out.println("Has account: " + hasAccount);
        if (hasAccount) {
            stepScore += 0.1;
            System.out.println("After account bonus: " + stepScore);
        }
        
        System.out.println("Final step-by-step result: " + stepScore);
        System.out.println("==========================");
        
        assertEquals(0.6d, score, 0.001);
    }
} 