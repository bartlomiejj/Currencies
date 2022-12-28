package CV.currency.domain;

import CV.currency.domain.protocol.CurrenciesDTO;
import CV.currency.domain.protocol.CurrenciesMapper;
import CV.currency.infrastructure.mongodb.CurrenciesDocument;
import CV.currency.infrastructure.mongodb.CurrencyRepository;
import CV.currency.infrastructure.provider.NBP.endpoint.api.NBPService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class CurrencyServiceImpl implements CurrencyService {

    private final NBPService nbpService;
    private final CurrencyRepository currencyRepository;

    @Override
    public void refresh() {
        CurrenciesDTO currenciesDTO = CurrenciesMapper.INSTANCE.mapToDto(nbpService.getCurrencies());
        CurrenciesDocument currenciesDocument = currencyRepository.findAll().stream()
                .findFirst()
                .orElseGet(CurrenciesDocument::new);
        currencyRepository.save(CurrenciesMapper.INSTANCE.updateDocument(currenciesDocument, currenciesDTO));
    }
}
