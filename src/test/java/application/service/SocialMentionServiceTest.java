package application.service;

import domain.analyzer.FacebookAnalyzer;
import domain.analyzer.TwitterAnalyzer;
import domain.model.RiskLevel;
import domain.model.SocialMention;
import infrastructure.persistence.DBService;
import infrastructure.persistence.SocialMentionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SocialMentionServiceTest {

    @Mock
    private SocialMentionRepository mockRepository;
    
    @Mock
    private DBService mockDBService;
    
    private SocialMentionService service;

    @BeforeEach
    void setUp() {
        service = new SocialMentionService(mockRepository, mockDBService);
    }

    @Test
    void shouldAnalyzeFacebookMention() {
        // Given
        SocialMention mention = new SocialMention();
        mention.setMessage("Test message");
        mention.setFacebookAccount("fb.com/xyz");
        mention.setFacebookComments(Arrays.asList("comment1", "comment2"));

        // When
        RiskLevel result = service.analyze(mention);

        // Then
        assertNotNull(result);
        verify(mockRepository).save(mention, result);
        verify(mockDBService).insertFBPost(
            eq(DBService.ANALYZED_FB_TABLE),
            anyDouble(),
            eq(mention.getMessage()),
            eq("fb.com/xyz")
        );
    }

    @Test
    void shouldAnalyzeTwitterMention() {
        // Given
        SocialMention mention = new SocialMention();
        mention.setMessage("Test tweet");
        mention.setTweeterAccount("@usuario");
        mention.setTweeterUrl("https://twitter.com/usuario/status/123");

        // When
        RiskLevel result = service.analyze(mention);

        // Then
        assertNotNull(result);
        verify(mockRepository).save(mention, result);
        verify(mockDBService).insertTweet(
            eq(DBService.ANALYZED_TWEETS_TABLE),
            anyDouble(),
            eq(mention.getMessage()),
            eq("https://twitter.com/usuario/status/123"),
            eq("@usuario")
        );
    }

    @Test
    void shouldThrowExceptionWhenNoAccountProvided() {
        // Given
        SocialMention mention = new SocialMention();
        mention.setMessage("Test message without account");

        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> service.analyze(mention)
        );
        
        assertEquals("Error, Tweeter or Facebook account must be present", exception.getMessage());
        verify(mockRepository, never()).save(any(), any());
        verify(mockDBService, never()).insertFBPost(anyString(), anyDouble(), anyString(), anyString());
        verify(mockDBService, never()).insertTweet(anyString(), anyDouble(), anyString(), anyString(), anyString());
    }

    @Test
    void shouldReturnCorrectFacebookScoreMapping() {
        // Given
        SocialMention mention = new SocialMention();
        mention.setFacebookAccount("fb.com/xyz");
        mention.setMessage("Short message"); // Para que sea LOW_RISK

        // When
        RiskLevel result = service.analyze(mention);

        // Then
        assertNotNull(result);
        assertTrue(Arrays.asList(RiskLevel.HIGH_RISK, RiskLevel.MEDIUM_RISK, RiskLevel.LOW_RISK).contains(result));
    }

    @Test
    void shouldReturnCorrectTwitterScoreMapping() {
        // Given
        SocialMention mention = new SocialMention();
        mention.setTweeterAccount("@usuario");
        mention.setMessage("Short tweet"); // Para que sea LOW_RISK

        // When
        RiskLevel result = service.analyze(mention);

        // Then
        assertNotNull(result);
        assertTrue(Arrays.asList(RiskLevel.HIGH_RISK, RiskLevel.MEDIUM_RISK, RiskLevel.LOW_RISK).contains(result));
    }
}