package domain.analyzer;

import domain.model.RiskLevel;
import domain.model.SocialMention;

public class TwitterAnalyzer implements SocialAnalyzer {

    @Override
    public boolean supports(SocialMention mention) {
        return mention.getTwitterAccount() != null;
    }

    @Override
    public RiskLevel analyze(SocialMention mention) {
        double score = 0.6; // lógica simulada
        if (score <= -0.5) return RiskLevel.HIGH_RISK;
        if (score < 0.7) return RiskLevel.MEDIUM_RISK;
        return RiskLevel.LOW_RISK;
    }
}