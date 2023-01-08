package currencies.endpoint.api

import currencies.IntegrationTest
import currencies.endpoint.api.protocol.CalculateRequest
import currencies.infrastructure.mongodb.CurrenciesDocument
import currencies.infrastructure.mongodb.CurrenciesRepository
import currencies.infrastructure.provider.NBPprovider.protocol.NBPResponse
import currencies.shared.Currency
import currencies.utils.CurrenciesTestUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class CurrenciesControllerIT extends IntegrationTest {

    @Autowired
    CurrenciesRepository currenciesRepository

    def cleanup() {
        currenciesRepository.deleteAll()
    }

    def "should save currencies"() {
        given:
            def responseJson = new File(CurrenciesTestUtils.NBP_RESPONSE_JSON_PATH)
            def nbpResponse = objectMapper.readValue(responseJson, NBPResponse[].class)
            def exampleResponseCurrency = nbpResponse[0].currencies.find {
                Currency it -> it.code == CurrenciesTestUtils.USD_CURRENCY_CODE}

        when:
             restTemplate.getForObject(_, _) >> nbpResponse

             mockMvc.perform(put(CurrenciesTestUtils.REFRESH)).andExpect(status().isOk())

        then:
            def documents = currenciesRepository.findAll()
            documents.size() == 1
            documents[0].currencies.size() == nbpResponse[0].currencies.size()
            def exampleDocumentCurrency = documents[0].currencies.find({
                CurrenciesDocument.Currency it -> it.code == CurrenciesTestUtils.USD_CURRENCY_CODE })
            exampleDocumentCurrency.name == exampleResponseCurrency.name
            exampleDocumentCurrency.code == exampleResponseCurrency.code
            exampleDocumentCurrency.mid == exampleResponseCurrency.mid
            exampleDocumentCurrency.lastUpdated == nbpResponse[0].effectiveDate
            exampleDocumentCurrency.source == CurrenciesTestUtils.NBP_SOURCE

    }

    def "should refresh currencies"() {
        given:
            def savedCurrency = currenciesRepository.save(CurrenciesTestUtils.exampleCurrenciesDocument())
                    .currencies.find({ CurrenciesDocument.Currency it -> it.code == CurrenciesTestUtils.USD_CURRENCY_CODE })
            def responseJson = new File(CurrenciesTestUtils.NBP_RESPONSE_JSON_PATH)
            def nbpResponse = objectMapper.readValue(responseJson, NBPResponse[].class)
            def exampleResponseCurrency = nbpResponse[0].currencies.find {
                Currency it -> it.code == CurrenciesTestUtils.USD_CURRENCY_CODE}

        when:
            restTemplate.getForObject(_, _) >> nbpResponse

            mockMvc.perform(put(CurrenciesTestUtils.REFRESH)).andExpect(status().isOk())

        then:
            def documents = currenciesRepository.findAll()
            documents.size() == 1
            documents[0].currencies.size() == nbpResponse[0].currencies.size()
            def exampleDocCurrency = documents[0].currencies.find
                    { CurrenciesDocument.Currency it -> it.code == CurrenciesTestUtils.USD_CURRENCY_CODE }
            exampleDocCurrency.name == savedCurrency.name && exampleResponseCurrency.name
            exampleDocCurrency.code == savedCurrency.code && exampleResponseCurrency.code
            exampleDocCurrency.mid != savedCurrency.mid
            exampleDocCurrency.mid == exampleResponseCurrency.mid
            exampleDocCurrency.lastUpdated != savedCurrency.lastUpdated
            exampleDocCurrency.lastUpdated == nbpResponse[0].effectiveDate
            exampleDocCurrency.source == CurrenciesTestUtils.NBP_SOURCE

    }

    def "should return calculated currency from EUR to USD"() {
        given:
            currenciesRepository.save(CurrenciesTestUtils.exampleCurrenciesDocument())
            def usdMid = currenciesRepository.findAll()[0].currencies.find
                    { CurrenciesDocument.Currency it -> it.code == CurrenciesTestUtils.USD_CURRENCY_CODE }.mid
            def eurMid = currenciesRepository.findAll()[0].currencies.find
                    { CurrenciesDocument.Currency it -> it.code == CurrenciesTestUtils.EUR_CURRENCY_CODE }.mid
            def request = new CalculateRequest(
                    sourceValue: 10.00,
                    sourceCurrency: CurrenciesTestUtils.USD_CURRENCY_CODE,
                    targetCurrency: CurrenciesTestUtils.EUR_CURRENCY_CODE)
            def requestJSON = objectMapper.writeValueAsString(request)

        when:
            def response = mockMvc.perform(post(CurrenciesTestUtils.CALCULATE)
                    .content(requestJSON)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn().response.contentAsString

        then:
            response == (request.sourceValue * usdMid / eurMid).toString()

    }

}
