package currencies.domain.protocol;

import currencies.endpoint.api.protocol.CalculateRequest;
import currencies.infrastructure.mongodb.CurrenciesDocument;
import currencies.shared.Currency;
import java.util.ArrayList;
import java.util.Collection;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-12-29T20:29:45+0100",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 18.0.1.1 (Oracle Corporation)"
)
public class CurrenciesMapperImpl implements CurrenciesMapper {

    @Override
    public CurrenciesDTO.Currency mapCurrency(Currency currency, String lastUpdated, String source) {
        if ( currency == null && lastUpdated == null && source == null ) {
            return null;
        }

        CurrenciesDTO.Currency currency1 = new CurrenciesDTO.Currency();

        if ( currency != null ) {
            currency1.name = currency.name;
            currency1.code = currency.code;
            currency1.mid = currency.mid;
        }
        currency1.lastUpdated = lastUpdated;
        currency1.source = source;

        return currency1;
    }

    @Override
    public CurrenciesDocument updateDocument(CurrenciesDocument currenciesDocument, CurrenciesDTO currenciesDTO) {
        if ( currenciesDTO == null ) {
            return currenciesDocument;
        }

        if ( currenciesDocument.currencies != null ) {
            Collection<CurrenciesDocument.Currency> collection = currencyCollectionToCurrencyCollection( currenciesDTO.currencies );
            if ( collection != null ) {
                currenciesDocument.currencies.clear();
                currenciesDocument.currencies.addAll( collection );
            }
            else {
                currenciesDocument.currencies = null;
            }
        }
        else {
            Collection<CurrenciesDocument.Currency> collection = currencyCollectionToCurrencyCollection( currenciesDTO.currencies );
            if ( collection != null ) {
                currenciesDocument.currencies = collection;
            }
        }

        return currenciesDocument;
    }

    @Override
    public CalculateDTO mapToDto(CalculateRequest calculateRequest) {
        if ( calculateRequest == null ) {
            return null;
        }

        CalculateDTO calculateDTO = new CalculateDTO();

        calculateDTO.sourceValue = calculateRequest.sourceValue;
        calculateDTO.sourceCurrency = calculateRequest.sourceCurrency;
        calculateDTO.targetCurrency = calculateRequest.targetCurrency;

        return calculateDTO;
    }

    protected CurrenciesDocument.Currency currencyToCurrency(CurrenciesDTO.Currency currency) {
        if ( currency == null ) {
            return null;
        }

        CurrenciesDocument.Currency currency1 = new CurrenciesDocument.Currency();

        currency1.name = currency.name;
        currency1.code = currency.code;
        currency1.mid = currency.mid;
        currency1.lastUpdated = currency.lastUpdated;
        currency1.source = currency.source;

        return currency1;
    }

    protected Collection<CurrenciesDocument.Currency> currencyCollectionToCurrencyCollection(Collection<CurrenciesDTO.Currency> collection) {
        if ( collection == null ) {
            return null;
        }

        Collection<CurrenciesDocument.Currency> collection1 = new ArrayList<CurrenciesDocument.Currency>( collection.size() );
        for ( CurrenciesDTO.Currency currency : collection ) {
            collection1.add( currencyToCurrency( currency ) );
        }

        return collection1;
    }
}
