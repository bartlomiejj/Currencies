package currencies.domain;

import currencies.domain.protocol.CalculateDTO;

import java.math.BigDecimal;

interface CurrenciesService {

    BigDecimal calculate(CalculateDTO calculateDTO);
    void refresh();
}
