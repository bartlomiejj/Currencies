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
        private String name;
        private String code;
        private BigDecimal mid;
        private String lastUpdated;
        private String source;
    }
}
