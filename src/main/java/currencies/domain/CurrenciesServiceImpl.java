package currencies.domain;

import currencies.domain.exceptions.BadRateException;
import currencies.domain.exceptions.NoCurrenciesException;
import currencies.domain.protocol.CalculateDTO;
import currencies.domain.protocol.CurrenciesDTO;
import currencies.domain.protocol.CurrenciesMapper;
import currencies.infrastructure.mongodb.CurrenciesDocument;
import currencies.infrastructure.mongodb.CurrenciesRepository;
import currencies.infrastructure.provider.NBPprovider.NBPService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static liquibase.repackaged.org.apache.commons.collections4.CollectionUtils.isEmpty;

@RequiredArgsConstructor
@Component
@Slf4j
class CurrenciesServiceImpl implements CurrenciesService {

    private final NBPService nbpService;
    private final CurrenciesRepository currencyRepository;

    @Override
    public BigDecimal calculate(CalculateDTO calculateDTO) {
        log.info("Calculating value from {} {} to {}.", calculateDTO.getSourceValue(), calculateDTO.getSourceCurrency(),
                calculateDTO.getTargetCurrency());
        Map<String, BigDecimal> ratesMap = currencyRepository.findAll().stream()
                .findFirst()
                .orElseThrow(() -> new NoCurrenciesException("No currencies in DB."))
                .getCurrencies().stream()
                .collect(Collectors.toMap(CurrenciesDocument.Currency::getCode, CurrenciesDocument.Currency::getMid));
        BigDecimal calculatedValue = calculateValue(calculateDTO, ratesMap);
        log.info("Calculation finished. Result: {}", calculatedValue);
        return calculatedValue;
    }

    @Override
    public void refresh() {
        CurrenciesDTO currenciesDTO = CurrenciesMapper.INSTANCE.mapToDto(nbpService.getCurrencies());
        if (!isEmpty(currenciesDTO.getCurrencies())) {
            CurrenciesDocument currenciesDocument = currencyRepository.findAll().stream()
                    .findFirst()
                    .orElseGet(CurrenciesDocument::new);
            currencyRepository.save(CurrenciesMapper.INSTANCE.updateDocument(currenciesDocument, currenciesDTO));
        }
    }

    private BigDecimal calculateValue(CalculateDTO calculateDTO, Map<String, BigDecimal> ratesMap) {
        BigDecimal baseValue = calculateBaseValue(calculateDTO, ratesMap);
        return calculateTargetValue(calculateDTO, ratesMap, baseValue);
    }

    private BigDecimal calculateBaseValue(CalculateDTO calculateDTO, Map<String, BigDecimal> ratesMap) {
        BigDecimal rate = findRate(ratesMap, calculateDTO.getSourceCurrency());
        return rate.multiply(calculateDTO.getSourceValue());
    }

    private BigDecimal calculateTargetValue(CalculateDTO calculateDTO, Map<String, BigDecimal> ratesMap, BigDecimal baseValue) {
        BigDecimal rate = findRate(ratesMap, calculateDTO.getTargetCurrency());
        return baseValue.divide(rate, RoundingMode.UP).setScale(2, RoundingMode.UP);
    }

    private static BigDecimal findRate(Map<String, BigDecimal> ratesMap, String desiredCurrency) {
        return Optional.ofNullable(ratesMap.get(desiredCurrency)).orElseThrow(
                () -> new BadRateException("Could not find rate for ".concat(desiredCurrency))
        );
    }
}
