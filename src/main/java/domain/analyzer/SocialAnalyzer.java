package domain.analyzer;

import domain.model.SocialMention;
import domain.model.RiskLevel;

public interface SocialAnalyzer {
    boolean supports(SocialMention mention);
    RiskLevel analyze(SocialMention mention);
}