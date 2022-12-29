package currencies.infrastructure.provider;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "app.provider")
public class ProviderConfig {

    String uri;
}
