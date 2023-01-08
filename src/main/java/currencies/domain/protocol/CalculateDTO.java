package currencies.domain.protocol;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CalculateDTO {
    private BigDecimal sourceValue;
    private String sourceCurrency;
    private String targetCurrency;
}
