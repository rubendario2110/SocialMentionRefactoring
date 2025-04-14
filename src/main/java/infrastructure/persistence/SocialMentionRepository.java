package infrastructure.persistence;

import domain.model.RiskLevel;
import domain.model.SocialMention;

public interface SocialMentionRepository {
    void save(SocialMention mention, RiskLevel riskLevel);
}