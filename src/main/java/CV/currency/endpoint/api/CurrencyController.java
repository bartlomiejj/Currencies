package CV.currency.endpoint.api;

import CV.currency.domain.CurrencyFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/currency")
@RequiredArgsConstructor
public class CurrencyController {

    private final CurrencyFacade currencyFacade;

    @PostMapping("/refresh")
    public void refreshCurrency(){
        currencyFacade.refresh();
    }
}
