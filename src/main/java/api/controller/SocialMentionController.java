package api.controller;

import application.service.SocialMentionService;
import domain.model.RiskLevel;
import domain.model.SocialMention;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.annotation.Body;
import jakarta.inject.Inject;

@Controller
public class SocialMentionController {

    private final SocialMentionService service;

    @Inject
    public SocialMentionController(SocialMentionService service) {
        this.service = service;
    }

    @Post("/AnalyzeSocialMention")
    @Produces(MediaType.TEXT_PLAIN)
    public String analyze(@Body SocialMention socialMention) {
        try {
            RiskLevel riskLevel = service.analyze(socialMention);
            return riskLevel.name();
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }
}