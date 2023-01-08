package currencies.infrastructure.provider.NBPprovider;

import currencies.domain.exceptions.ProviderServiceException;
import currencies.infrastructure.provider.NBPprovider.protocol.NBPResponse;
import currencies.infrastructure.provider.ProviderConfig;
import currencies.infrastructure.provider.ProviderService;
import currencies.infrastructure.provider.protocol.AbstractProviderResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class NBPService implements ProviderService {

    private final RestTemplate restTemplate;
    private final ProviderConfig providerConfig;

    @Override
    public AbstractProviderResponse getCurrencies() {
        try {
            log.info("Getting currencies from NBP service started.");
            NBPResponse[] response = Optional.ofNullable(restTemplate.getForObject(providerConfig.getUri(), NBPResponse[].class))
                    .orElseGet(() -> new NBPResponse[0]);
            log.info("Getting currencies from NBP service finished.");
            return Arrays.stream(response)
                    .findFirst()
                    .orElseThrow(ProviderServiceException::new);
        } catch (RestClientException e) {
            log.error("Error while getting currencies.", e);
            throw new ProviderServiceException();
        }
    }
}
