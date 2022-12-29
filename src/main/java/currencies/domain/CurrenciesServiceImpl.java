package currencies.domain;

import currencies.domain.protocol.CurrenciesDTO;
import currencies.domain.protocol.CurrenciesMapper;
import currencies.infrastructure.mongodb.CurrenciesDocument;
import currencies.infrastructure.mongodb.CurrenciesRepository;
import currencies.infrastructure.provider.NBPprovider.NBPService;
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
