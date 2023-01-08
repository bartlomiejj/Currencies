package currencies.domain


import currencies.IntegrationTest
import currencies.domain.exceptions.BadRateException
import currencies.domain.exceptions.NoCurrenciesException
import currencies.domain.exceptions.ProviderServiceException
import currencies.domain.protocol.CalculateDTO
import currencies.infrastructure.mongodb.CurrenciesDocument
import currencies.infrastructure.mongodb.CurrenciesRepository
import currencies.infrastructure.provider.NBPprovider.protocol.NBPResponse
import currencies.utils.CurrenciesTestUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.client.RestClientException

class CurrenciesServiceIT extends IntegrationTest {

    @Autowired
    CurrenciesRepository currenciesRepository

    @Autowired
    CurrenciesServiceImpl currenciesService

    def cleanup() {
        currenciesRepository.deleteAll()
    }

    def "should throw NoCurrenciesException when no currencies in DB"() {
        given:
            def request = new CalculateDTO(
                    sourceValue: 10.00,
                    sourceCurrency: CurrenciesTestUtils.USD_CURRENCY_CODE,
                    targetCurrency: CurrenciesTestUtils.EUR_CURRENCY_CODE)

        when:
            currenciesService.calculate(request)

        then:
            thrown(NoCurrenciesException.class)

    }

    def "should throw BadRateException when currency not found"() {
        given:
            currenciesRepository.save(CurrenciesTestUtils.exampleCurrenciesDocument())
            def request = new CalculateDTO(
                    sourceValue: 10.00,
                    sourceCurrency: "unknown",
                    targetCurrency: CurrenciesTestUtils.EUR_CURRENCY_CODE)

        when:
            currenciesService.calculate(request)

        then:
            thrown(BadRateException.class)

    }

    def "should throw ProviderServiceException and not update document when empty response from provider"() {
        given:
            def savedCurrency = currenciesRepository.save(CurrenciesTestUtils.exampleCurrenciesDocument())
            def exampleSavedCurrency = savedCurrency.currencies.find
                    { CurrenciesDocument.Currency it -> it.code == CurrenciesTestUtils.USD_CURRENCY_CODE }

        when:
            restTemplate.getForObject(_, _) >> new NBPResponse[0]

            currenciesService.refresh()

        then:
            thrown(ProviderServiceException.class)
            def documents = currenciesRepository.findAll()
            documents.size() == 1
            documents[0].currencies.size() == savedCurrency.currencies.size()
            def exampleDocCurrency = documents[0].currencies.find
                    { CurrenciesDocument.Currency it -> it.code == CurrenciesTestUtils.USD_CURRENCY_CODE }
            exampleDocCurrency.name == exampleSavedCurrency.name
            exampleDocCurrency.code == exampleSavedCurrency.code
            exampleDocCurrency.mid == exampleSavedCurrency.mid
            exampleDocCurrency.lastUpdated == exampleSavedCurrency.lastUpdated
            exampleDocCurrency.source == exampleSavedCurrency.source

    }

    def "should throw ProviderServiceException and not update document when no response from provider"() {
        given:
            def savedCurrency = currenciesRepository.save(CurrenciesTestUtils.exampleCurrenciesDocument())
            def exampleSavedCurrency = savedCurrency.currencies.find
                    { CurrenciesDocument.Currency it -> it.code == CurrenciesTestUtils.USD_CURRENCY_CODE }

        when:
            restTemplate.getForObject(_, _) >> { it -> throw new RestClientException("message") }

            currenciesService.refresh()

        then:
            thrown(ProviderServiceException.class)
            def documents = currenciesRepository.findAll()
            documents.size() == 1
            documents[0].currencies.size() == savedCurrency.currencies.size()
            def exampleDocCurrency = documents[0].currencies.find
                    { CurrenciesDocument.Currency it -> it.code == CurrenciesTestUtils.USD_CURRENCY_CODE }
            exampleDocCurrency.name == exampleSavedCurrency.name
            exampleDocCurrency.code == exampleSavedCurrency.code
            exampleDocCurrency.mid == exampleSavedCurrency.mid
            exampleDocCurrency.lastUpdated == exampleSavedCurrency.lastUpdated
            exampleDocCurrency.source == exampleSavedCurrency.source

    }
}
