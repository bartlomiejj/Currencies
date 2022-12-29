package currencies.domain;

import currencies.domain.protocol.CalculateDTO;
import currencies.domain.protocol.CurrenciesDTO;
import currencies.domain.protocol.CurrenciesMapper;
import currencies.infrastructure.mongodb.CurrenciesDocument;
import currencies.infrastructure.mongodb.CurrenciesRepository;
import currencies.infrastructure.provider.NBPprovider.NBPService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

import static liquibase.repackaged.org.apache.commons.collections4.CollectionUtils.isEmpty;

@RequiredArgsConstructor
@Component
class CurrenciesServiceImpl implements CurrenciesService {

    private final NBPService nbpService;
    private final CurrenciesRepository currencyRepository;
    private final CalculationConfig calculationConfig;

    @Override
    public BigDecimal calculate(CalculateDTO calculateDTO) {
        CurrenciesDocument currenciesDocument = currencyRepository.findAll().stream()
                .findFirst()
                .orElseThrow(RuntimeException::new);
        BigDecimal baseValue = calculateBaseValue(calculateDTO, currenciesDocument);
        return calculateTargetValue(calculateDTO, currenciesDocument, baseValue);
    }

    private BigDecimal calculateBaseValue(CalculateDTO calculateDTO, CurrenciesDocument currenciesDocument) {
        BigDecimal rate = findRate(currenciesDocument, calculateDTO.getSourceCurrency());
        return rate.multiply(calculateDTO.getSourceValue());
    }


    private BigDecimal calculateTargetValue(CalculateDTO calculateDTO, CurrenciesDocument currenciesDocument, BigDecimal baseValue) {
        BigDecimal rate = findRate(currenciesDocument, calculateDTO.getTargetCurrency());
        return baseValue.divide(rate, RoundingMode.UP).setScale(2, RoundingMode.UP);
    }

    private BigDecimal findRate(CurrenciesDocument currenciesDocument, String desiredCurrency) {
        return currenciesDocument.getCurrencies().stream()
                .filter(currency -> Objects.equals(currency.getCode(), desiredCurrency))
                .findFirst()
                .orElseThrow(RuntimeException::new)
                .getMid();
    }

    @Override
    public void refresh() {
        CurrenciesDTO currenciesDTO = CurrenciesMapper.INSTANCE.mapToDto(nbpService.getCurrencies());
        if (!isEmpty(currenciesDTO.currencies)) {
            CurrenciesDocument currenciesDocument = currencyRepository.findAll().stream()
                    .findFirst()
                    .orElseGet(CurrenciesDocument::new);
            currencyRepository.save(CurrenciesMapper.INSTANCE.updateDocument(currenciesDocument, currenciesDTO));
        }

    }
}
