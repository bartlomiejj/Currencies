package CV.currencies;

import CV.currencies.infrastructure.provider.ProviderConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class CurrenciesApplication {
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	@Bean
	public ProviderConfig providerConfig() {
		return new ProviderConfig();
	}

	public static void main(String[] args) {
		SpringApplication.run(CurrenciesApplication.class, args);
	}

}
