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
        double facebookCommentsScore = 0;
        double facebookScore = 0d;
        
        // Preparar mensaje con prefijo
        String originalMessage = mention.getMessage();
        mention.setMessage("facebookMessage: " + originalMessage);
        
        // Procesar comentarios si existen
        if (mention.getFacebookComments() != null && !mention.getFacebookComments().isEmpty()) {
            String comments = mention.getFacebookComments()
                .stream()
                .reduce("", (h, c) -> h + " " + c);
            mention.setMessage(mention.getMessage() + " || comments: " + comments);
            
            // Analizar comentarios de Facebook
            if (mention.getMessage().contains("comments:")) {
                facebookCommentsScore = calculateFacebookCommentsScore(
                    mention.getMessage().substring(mention.getMessage().indexOf("comments:"))
                );
                if (facebookCommentsScore < 50d) {
                    facebookScore = Double.sum(facebookScore, -100d);
                }
            }
        }
        
        // Analizar post de Facebook (solo si no está ya en riesgo alto)
        if (facebookScore > -100) {
            facebookScore = analyzePost(mention.getMessage(), mention.getFacebookAccount());
        }
        
        // Mapear score a nivel de riesgo
        if (facebookScore == -100d) {
            return RiskLevel.HIGH_RISK;
        } else if (facebookScore > -100d && facebookScore < 50d) {
            return RiskLevel.MEDIUM_RISK;
        } else {
            return RiskLevel.LOW_RISK;
        }
    }
    
    public static double calculateFacebookCommentsScore(String comments) {
        // Lógica simplificada para calcular score de comentarios
        return comments.length() > 100 ? 30d : 60d;
    }
    
    public static double analyzePost(String message, String facebookAccount) {
        // Lógica simplificada para analizar post
        return message.length() > 200 ? 40d : 80d;
    }
}