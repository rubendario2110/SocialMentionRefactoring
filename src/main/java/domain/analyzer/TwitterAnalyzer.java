package domain.analyzer;

import domain.model.RiskLevel;
import domain.model.SocialMention;

public class TwitterAnalyzer implements SocialAnalyzer {

    @Override
    public boolean supports(SocialMention mention) {
        return mention.getTweeterAccount() != null;
    }

    @Override
    public RiskLevel analyze(SocialMention mention) {
        // Preparar mensaje con prefijo
        mention.setMessage("tweeterMessage: " + mention.getMessage());
        
        // Analizar tweet
        double tweeterScore = analyzeTweet(
            mention.getMessage(),
            mention.getTweeterUrl(),
            mention.getTweeterAccount()
        );
        
        // Mapear score a nivel de riesgo
        if (tweeterScore >= -1 && tweeterScore <= -0.5d) {
            return RiskLevel.HIGH_RISK;
        } else if (tweeterScore > -0.5d && tweeterScore < 0.7d) {
            return RiskLevel.MEDIUM_RISK;
        } else {
            return RiskLevel.LOW_RISK;
        }
    }
    
    public static double analyzeTweet(String message, String tweeterUrl, String tweeterAccount) {
        // Lógica simplificada para analizar tweet
        double score = 0.6d;
        
        // Factores que afectan el score
        if (message.length() > 280) {
            score -= 0.2;
        }
        if (tweeterUrl != null && !tweeterUrl.isEmpty()) {
            score += 0.1;
        }
        if (tweeterAccount != null && tweeterAccount.startsWith("@")) {
            score += 0.1;
        }
        
        return score;
    }
}