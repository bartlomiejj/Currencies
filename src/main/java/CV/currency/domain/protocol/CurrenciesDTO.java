package CV.currency.domain.protocol;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Collection;

@Data
public class CurrenciesDTO {

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
