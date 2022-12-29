package currencies.infrastructure.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrenciesRepository extends MongoRepository<CurrenciesDocument, Integer> {
}
