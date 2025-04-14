package domain.analyzer;

import domain.model.RiskLevel;
import domain.model.SocialMention;

public class FacebookAnalyzer implements SocialAnalyzer {

    @Override
    public boolean supports(SocialMention mention) {
        return mention.getFacebookAccount() != null;
    }

    @Override
    public RiskLevel analyze(SocialMention mention) {
        double score = 50; // lógica simulada
        return score < 50 ? RiskLevel.HIGH_RISK : RiskLevel.LOW_RISK;
    }
}