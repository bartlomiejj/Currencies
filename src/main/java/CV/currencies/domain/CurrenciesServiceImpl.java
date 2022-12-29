package CV.currencies.domain;

import CV.currencies.domain.protocol.CurrenciesDTO;
import CV.currencies.domain.protocol.CurrenciesMapper;
import CV.currencies.infrastructure.mongodb.CurrenciesDocument;
import CV.currencies.infrastructure.mongodb.CurrenciesRepository;
import CV.currencies.infrastructure.provider.NBP.NBPService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static liquibase.repackaged.org.apache.commons.collections4.CollectionUtils.isEmpty;

@RequiredArgsConstructor
@Component
class CurrenciesServiceImpl implements CurrenciesService {

    private final NBPService nbpService;
    private final CurrenciesRepository currencyRepository;

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
