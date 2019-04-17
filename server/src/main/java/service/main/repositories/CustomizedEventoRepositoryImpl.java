package service.main.repositories;

import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import service.main.entity.Evento;
import service.main.exception.BadRequestException;
import service.main.exception.InternalErrorException;

public class CustomizedEventoRepositoryImpl implements CustomizedEventoRepository {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public CustomizedEventoRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void addParticipant(String participantmail, String eventId) throws BadRequestException {
        Query query = new Query(Criteria.where("id").is(eventId));
        Update update = new Update();
        update.addToSet("participantes",participantmail);
        UpdateResult result = mongoTemplate.updateFirst(query,update,Evento.class);
        if(result.getModifiedCount() != 1) throw new BadRequestException("The user already participates in the event");
    }
}
