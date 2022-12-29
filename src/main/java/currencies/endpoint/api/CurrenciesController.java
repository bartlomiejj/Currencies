package currencies.endpoint.api;

import currencies.domain.CurrenciesFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequestMapping("/api/currencies")
@RequiredArgsConstructor
public class CurrenciesController {

    private final CurrenciesFacade currenciesFacade;

    @PutMapping("/refresh")
    public void refreshCurrency(){
        currenciesFacade.refresh();
    }
}
