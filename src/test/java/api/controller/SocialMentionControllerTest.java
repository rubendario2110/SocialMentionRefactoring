package api.controller;

import application.service.SocialMentionService;
import domain.model.RiskLevel;
import domain.model.SocialMention;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
class SocialMentionControllerTest {

    @Inject
    @Client("/")
    HttpClient client;

    @Test
    void shouldAnalyzeFacebookMention() {
        // Given
        SocialMention mention = new SocialMention();
        mention.setMessage("Test message");
        mention.setFacebookAccount("fb.com/xyz");
        mention.setFacebookComments(Arrays.asList("comment1", "comment2"));

        HttpRequest<SocialMention> request = HttpRequest.POST("/AnalyzeSocialMention", mention);

        // When
        String response = client.toBlocking().retrieve(request);

        // Then
        assertNotNull(response);
        assertTrue(Arrays.asList("HIGH_RISK", "MEDIUM_RISK", "LOW_RISK").contains(response));
    }

    @Test
    void shouldAnalyzeTwitterMention() {
        // Given
        SocialMention mention = new SocialMention();
        mention.setMessage("Test tweet");
        mention.setTweeterAccount("@usuario");
        mention.setTweeterUrl("https://twitter.com/usuario/status/123");

        HttpRequest<SocialMention> request = HttpRequest.POST("/AnalyzeSocialMention", mention);

        // When
        String response = client.toBlocking().retrieve(request);

        // Then
        assertNotNull(response);
        assertTrue(Arrays.asList("HIGH_RISK", "MEDIUM_RISK", "LOW_RISK").contains(response));
    }

    @Test
    void shouldReturnErrorWhenNoAccountProvided() {
        // Given
        SocialMention mention = new SocialMention();
        mention.setMessage("Test message without account");

        HttpRequest<SocialMention> request = HttpRequest.POST("/AnalyzeSocialMention", mention);

        // When
        String response = client.toBlocking().retrieve(request);

        // Then
        assertEquals("Error, Tweeter or Facebook account must be present", response);
    }
} 