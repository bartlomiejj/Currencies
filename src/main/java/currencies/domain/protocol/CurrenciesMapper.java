package currencies.domain.protocol;

import currencies.infrastructure.mongodb.CurrenciesDocument;
import currencies.infrastructure.provider.protocol.ProviderResponse;
import currencies.shared.Currency;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.HashSet;
import java.util.stream.Collectors;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface CurrenciesMapper {
    CurrenciesMapper INSTANCE = Mappers.getMapper(CurrenciesMapper.class);

    default CurrenciesDTO mapToDto(ProviderResponse providerResponse) {
        return new CurrenciesDTO(mapCurrencies(providerResponse));
    }

    default HashSet<CurrenciesDTO.Currency> mapCurrencies(ProviderResponse providerResponse) {
        return providerResponse.getCurrencies().stream()
                .map(currency -> mapCurrency(currency, providerResponse.getLastUpdated(), providerResponse.getSource()))
                .collect(Collectors.toCollection(HashSet::new));
    }

    CurrenciesDTO.Currency mapCurrency(Currency currency, String lastUpdated, String source);

    @Mapping(target = "id", ignore = true)
    CurrenciesDocument updateDocument(@MappingTarget CurrenciesDocument currenciesDocument, CurrenciesDTO currenciesDTO);

}
