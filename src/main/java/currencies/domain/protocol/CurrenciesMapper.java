package currencies.domain.protocol;

import currencies.endpoint.api.protocol.CalculateRequest;
import currencies.infrastructure.mongodb.CurrenciesDocument;
import currencies.infrastructure.provider.protocol.AbstractProviderResponse;
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

    default CurrenciesDTO mapToDto(AbstractProviderResponse abstractProviderResponse) {
        return new CurrenciesDTO(mapCurrencies(abstractProviderResponse));
    }

    default HashSet<CurrenciesDTO.Currency> mapCurrencies(AbstractProviderResponse abstractProviderResponse) {
        return abstractProviderResponse.getCurrencies().stream()
                .map(currency -> mapCurrency(currency, abstractProviderResponse.getLastUpdated(), abstractProviderResponse.getSource()))
                .collect(Collectors.toCollection(HashSet::new));
    }

    CurrenciesDTO.Currency mapCurrency(Currency currency, String lastUpdated, String source);

    @Mapping(target = "id", ignore = true)
    CurrenciesDocument updateDocument(@MappingTarget CurrenciesDocument currenciesDocument, CurrenciesDTO currenciesDTO);

    CalculateDTO mapToDto(CalculateRequest calculateRequest);
}
