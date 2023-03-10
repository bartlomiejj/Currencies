package currencies.domain;

import currencies.domain.protocol.CalculateDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CurrenciesFacade {

    private final CurrenciesService currenciesService;

    public BigDecimal calculate(CalculateDTO calculateDTO) {
        return currenciesService.calculate(calculateDTO);
    }

    @EventListener(ContextRefreshedEvent.class)
    public void refresh() {
        currenciesService.refresh();
    }

}
