package CV.currency.infrastructure.mongodb;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Collection;

@Document
@Data
public class CurrenciesDocument {

    public Collection<Currencies> currencies;

    @Data
    public static class Currencies {
        public String table;
        public String no;
        public String effectiveDate;
        public Collection<Rates> rates;

        @Data
        public static class Rates {
            public String currency;
            public String code;
            public BigDecimal mid;
        }
    }
}
