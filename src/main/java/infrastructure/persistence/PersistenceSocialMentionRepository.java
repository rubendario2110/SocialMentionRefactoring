package infrastructure.persistence;

import domain.model.RiskLevel;
import domain.model.SocialMention;

public class PersistenceSocialMentionRepository implements SocialMentionRepository {

    @Override
    public void save(SocialMention mention, RiskLevel riskLevel) {
        System.out.println("Guardado: " + mention.getMessage() + " -> " + riskLevel);
    }
}