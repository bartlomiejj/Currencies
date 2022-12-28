package CV.currency.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrencyFacade {

    private final CurrencyService currencyService;

    public void refresh() {
        currencyService.refresh();
    }
}
