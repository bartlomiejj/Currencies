package CV.currencies.infrastructure.mongodb;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.math.BigDecimal;
import java.util.Collection;

@Document
@Data
public class CurrenciesDocument {

    @MongoId
    public ObjectId id;

    public Collection<Currency> currencies;

    @Data
    public static class Currency {
        public String name;
        public String code;
        public BigDecimal mid;
        public String lastUpdated;
        public String source;
    }
}
