package currencies.infrastructure.mongodb;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;

@Document
@Data
public class CurrenciesDocument {

    @MongoId
    private ObjectId id;

    private Collection<Currency> currencies = new HashSet<>();

    @Data
    public static class Currency {
        private String name;
        private String code;
        private BigDecimal mid;
        private String lastUpdated;
        private String source;
    }
}
