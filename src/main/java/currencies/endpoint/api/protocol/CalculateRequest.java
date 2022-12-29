package currencies.endpoint.api.protocol;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class CalculateRequest {
    @NotNull
    public BigDecimal sourceValue;
    @NotNull
    public String sourceCurrency;
    @NotNull
    public String targetCurrency;
}
