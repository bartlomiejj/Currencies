package currencies.infrastructure.provider;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotNull;

@Data
@Configuration
@ConfigurationProperties(prefix = "app.provider")
public class ProviderConfig {

    @NotNull
    private String uri;

}
