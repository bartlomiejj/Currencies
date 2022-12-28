package CV.currency.infrastructure.provider.NBP.endpoint.api.protocol;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Collection;

@Data
public class CurrenciesResponse {

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
