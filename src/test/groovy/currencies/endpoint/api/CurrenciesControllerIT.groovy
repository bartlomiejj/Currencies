package currencies.endpoint.api

import currencies.IntegrationTest
import currencies.infrastructure.mongodb.CurrenciesDocument
import currencies.infrastructure.mongodb.CurrenciesRepository
import currencies.infrastructure.provider.NBPprovider.protocol.NBPResponse
import currencies.shared.Currency
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.client.RestClientException
import org.springframework.web.util.NestedServletException

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class CurrenciesControllerIT extends IntegrationTest {

    @Autowired
    CurrenciesRepository currencyRepository

    def cleanup() {
        currencyRepository.deleteAll()
    }

    def "should save currencies"() {
        given:
            def responseJson = new File(NBP_RESPONSE_JSON_PATH)
            def nbpResponse = objectMapper.readValue(responseJson, NBPResponse[].class)
            def exampleResponseCurrency = nbpResponse[0].currencies.find {
                Currency it -> it.code == EXAMPLE_CURRENCY}

        when:
             restTemplate.getForObject(_, _) >> nbpResponse

             mockMvc.perform(put(REFRESH)).andExpect(status().isOk())

        then:
            def documents = currencyRepository.findAll()
            documents.size() == 1
            documents[0].currencies.size() == nbpResponse[0].currencies.size()
            def exampleDocumentCurrency = documents[0].currencies.find({
                CurrenciesDocument.Currency it -> it.code == EXAMPLE_CURRENCY })
            exampleDocumentCurrency.name == exampleResponseCurrency.name
            exampleDocumentCurrency.code == exampleResponseCurrency.code
            exampleDocumentCurrency.mid == exampleResponseCurrency.mid
            exampleDocumentCurrency.lastUpdated == nbpResponse[0].effectiveDate
            exampleDocumentCurrency.source == NBP_SOURCE

    }

    def "should refresh currencies"() {
        given:
            def savedCurrency = saveBasicCurrenciesDocument().currencies.find(
                    { CurrenciesDocument.Currency it -> it.code == EXAMPLE_CURRENCY })
            def responseJson = new File(NBP_RESPONSE_JSON_PATH)
            def nbpResponse = objectMapper.readValue(responseJson, NBPResponse[].class)
            def exampleResponseCurrency = nbpResponse[0].currencies.find {
                Currency it -> it.code == EXAMPLE_CURRENCY}

        when:
            restTemplate.getForObject(_, _) >> nbpResponse

            mockMvc.perform(put(REFRESH)).andExpect(status().isOk())

        then:
            def documents = currencyRepository.findAll()
            documents.size() == 1
            documents[0].currencies.size() == nbpResponse[0].currencies.size()
            def exampleDocCurrency = documents[0].currencies.find( { CurrenciesDocument.Currency it -> it.code == EXAMPLE_CURRENCY})
            exampleDocCurrency.name == savedCurrency.name && exampleResponseCurrency.name
            exampleDocCurrency.code == savedCurrency.code && exampleResponseCurrency.code
            exampleDocCurrency.mid != savedCurrency.mid
            exampleDocCurrency.mid == exampleResponseCurrency.mid
            exampleDocCurrency.lastUpdated != savedCurrency.lastUpdated
            exampleDocCurrency.lastUpdated == nbpResponse[0].effectiveDate
            exampleDocCurrency.source == NBP_SOURCE
    }

    def "should throw exception not update document when no response from provider"() {
        given:
            def savedCurrency = saveBasicCurrenciesDocument()
            def exampleSavedCurrency = savedCurrency.currencies.find(
                { CurrenciesDocument.Currency it -> it.code == EXAMPLE_CURRENCY })
        when:
            restTemplate.getForObject(_, _) >> { it -> throw new RestClientException("message") }

            mockMvc.perform(put(REFRESH)).andExpect(status().is5xxServerError())

        then:
//            thrown(ProviderServiceException.class)
            thrown(NestedServletException.class)
            def documents = currencyRepository.findAll()
            documents.size() == 1
            documents[0].currencies.size() == savedCurrency.currencies.size()
            def exampleDocCurrency = documents[0].currencies.find( { CurrenciesDocument.Currency it -> it.code == EXAMPLE_CURRENCY})
            exampleDocCurrency.name == exampleSavedCurrency.name
            exampleDocCurrency.code == exampleSavedCurrency.code
            exampleDocCurrency.mid == exampleSavedCurrency.mid
            exampleDocCurrency.lastUpdated == exampleSavedCurrency.lastUpdated
            exampleDocCurrency.source == exampleSavedCurrency.source
    }

    def saveBasicCurrenciesDocument() {
        CurrenciesDocument document = new CurrenciesDocument()
        CurrenciesDocument.Currency currency = new CurrenciesDocument.Currency()
        currency.setName(EXAMPLE_CURRENCY_NAME)
        currency.setCode(EXAMPLE_CURRENCY)
        currency.setMid(0.12)
        currency.setLastUpdated("1900-01-01")
        currency.setSource("notNBP")
        document.setCurrencies(List.of(currency))
        currencyRepository.save(document)
    }

    private static String REFRESH = "/api/currencies/refresh"
    private static String NBP_RESPONSE_JSON_PATH = "src/test/resources/NBPResponse.json"
    private static String NBP_SOURCE = "NBP"
    private static String EXAMPLE_CURRENCY = "USD"
    private static String EXAMPLE_CURRENCY_NAME = "dolar amerykański"
}
