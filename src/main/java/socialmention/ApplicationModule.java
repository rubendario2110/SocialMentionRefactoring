package socialmention;

import application.service.SocialMentionService;
import domain.analyzer.FacebookAnalyzer;
import domain.analyzer.SocialAnalyzer;
import domain.analyzer.TwitterAnalyzer;
import infrastructure.persistence.DBService;
import infrastructure.persistence.PersistenceSocialMentionRepository;
import io.micronaut.context.annotation.Factory;
import jakarta.inject.Singleton;

@Factory
public class ApplicationModule {

    @Singleton
    public DBService dbService() {
        return new DBService("localhost", 5432);
    }

    @Singleton
    public PersistenceSocialMentionRepository socialMentionRepository() {
        return new PersistenceSocialMentionRepository();
    }

    @Singleton
    public SocialMentionService socialMentionService(PersistenceSocialMentionRepository repository, DBService dbService) {
        return new SocialMentionService(repository, dbService);
    }
} 