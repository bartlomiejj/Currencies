package currencies.infrastructure.provider.NBPprovider;

import currencies.infrastructure.provider.NBPprovider.protocol.NBPResponse;
import currencies.infrastructure.provider.ProviderConfig;
import currencies.infrastructure.provider.ProviderService;
import currencies.infrastructure.provider.protocol.ProviderResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class NBPService implements ProviderService {

    private final RestTemplate restTemplate;
    private final ProviderConfig providerConfig;

    @Override
    public ProviderResponse getCurrencies() {
        try {
            NBPResponse[] response = restTemplate.getForObject(providerConfig.getUri(), NBPResponse[].class);
            if (Objects.nonNull(response)) {
                return Arrays.stream(response).findFirst().orElseGet(NBPResponse::new);
            }
        } catch (RestClientException e) {
            log.error("Error while getting currencies.", e);
        }
        return new NBPResponse();
    }
}
