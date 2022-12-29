package currencies.domain;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "app.calculation")
public class CalculationConfig {

    String baseCurrency;
}
