package CV.currency.infrastructure.provider.NBP.endpoint.api;

import CV.currency.infrastructure.provider.NBP.endpoint.api.protocol.CurrenciesResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class NBPService {

    private final RestTemplate restTemplate;

    private static final String NBP_URL = "http://api.nbp.pl/api/exchangerates/tables/A/";

    public Collection<CurrenciesResponse> getCurrencies() {
        ObjectMapper mapper = new ObjectMapper();
        CurrenciesResponse[] response = new CurrenciesResponse[0];
        try {
            response = restTemplate.getForObject(NBP_URL, CurrenciesResponse[].class);
        } catch (RestClientException e) {
            log.error("Error while getting currencies.", e);
        }
        return Arrays.stream(response)
                .filter(Objects::nonNull)
                .map(object -> mapper.convertValue(object, CurrenciesResponse.class))
                .collect(Collectors.toSet());
    }
}
