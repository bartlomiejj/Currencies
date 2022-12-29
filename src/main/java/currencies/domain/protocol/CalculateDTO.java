package currencies.domain.protocol;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CalculateDTO {
    public BigDecimal sourceValue;
    public String sourceCurrency;
    public String targetCurrency;
}
