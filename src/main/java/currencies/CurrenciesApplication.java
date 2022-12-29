package currencies;

import currencies.domain.CalculationConfig;
import currencies.infrastructure.provider.ProviderConfig;
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

	@Bean
	public CalculationConfig calculationConfig() {
		return new CalculationConfig();
	}

	public static void main(String[] args) {
		SpringApplication.run(CurrenciesApplication.class, args);
	}

}
