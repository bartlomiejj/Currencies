package currencies.domain.protocol;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Collection;

@Data
@AllArgsConstructor
public class CurrenciesDTO {

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
