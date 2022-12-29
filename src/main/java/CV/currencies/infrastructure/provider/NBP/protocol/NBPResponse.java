package CV.currencies.infrastructure.provider.NBP.protocol;

import CV.currencies.infrastructure.provider.protocol.ProviderResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = true)
public class NBPResponse extends ProviderResponse {

    private static final String NBP_SOURCE = "NBP";

        public String table;
        public String no;
        public String effectiveDate;
        public Collection<Currency> rates = new HashSet<>();

    @Override
    public Collection<CV.currencies.shared.Currency> getCurrencies() {
        return rates.stream()
                .filter(Objects::nonNull)
                .map(nbpRate -> new CV.currencies.shared.Currency(nbpRate.currency, nbpRate.code, nbpRate.mid))
                .collect(Collectors.toSet());
    }

    @Override
    public String getLastUpdated() {
        return effectiveDate;
    }

    @Override
    public String getSource() {
        return NBP_SOURCE;
    }

    @Data
    public static class Currency {
        public String currency;
        public String code;
        public BigDecimal mid;
    }
}
