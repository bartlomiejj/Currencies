package currencies.endpoint.api;

import currencies.domain.CurrenciesFacade;
import currencies.domain.protocol.CurrenciesMapper;
import currencies.endpoint.api.protocol.CalculateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;

@RestControllerAdvice
@RequiredArgsConstructor
@RequestMapping("/api/currencies")
public class CurrenciesController {

    private final CurrenciesFacade currenciesFacade;

    @PostMapping("/calculate")
    public ResponseEntity<BigDecimal> calculateCurrency(@RequestBody @Valid CalculateRequest calculateRequest) {
        return ResponseEntity.ok(currenciesFacade.calculate(CurrenciesMapper.INSTANCE.mapToDto(calculateRequest)));
    }

    @PutMapping("/refresh")
    public void refreshCurrency(){
        currenciesFacade.refresh();
    }
}
