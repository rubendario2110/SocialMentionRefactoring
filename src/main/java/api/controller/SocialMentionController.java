package api.controller;

import application.service.SocialMentionService;
import domain.model.RiskLevel;
import domain.model.SocialMention;
import infrastructure.persistence.PersistenceSocialMentionRepository;

public class SocialMentionController {

    private final SocialMentionService service = new SocialMentionService(
        new PersistenceSocialMentionRepository()
    );

    public String analyze(SocialMention mention) {
        RiskLevel risk = service.analyze(mention);
        return risk.name();
    }
}