package CV.currency.domain.protocol;

import CV.currency.infrastructure.mongodb.CurrenciesDocument;
import CV.currency.infrastructure.provider.NBP.endpoint.api.protocol.CurrenciesResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.Collection;
import java.util.stream.Collectors;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface CurrenciesMapper {
    CurrenciesMapper INSTANCE = Mappers.getMapper(CurrenciesMapper.class);

    default CurrenciesDTO mapToDto(Collection<CurrenciesResponse> currenciesResponses) {
        CurrenciesDTO currenciesDTO = new CurrenciesDTO();
        currenciesDTO.setCurrencies(currenciesResponses.stream()
                .map(this::mapToCurrencies)
                .collect(Collectors.toSet()));
        return currenciesDTO;
    }

    CurrenciesDTO.Currencies mapToCurrencies(CurrenciesResponse currenciesResponse);

    CurrenciesDocument updateDocument(@MappingTarget CurrenciesDocument currenciesDocument, CurrenciesDTO currenciesDTO);

}
