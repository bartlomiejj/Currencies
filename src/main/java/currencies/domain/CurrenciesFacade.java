package currencies.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrenciesFacade {

    private final CurrenciesService currenciesService;

    public void refresh() {
        currenciesService.refresh();
    }
}
