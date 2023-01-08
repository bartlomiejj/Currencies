package currencies.utils

import currencies.infrastructure.mongodb.CurrenciesDocument

class CurrenciesTestUtils {

    def static exampleCurrenciesDocument() {
        CurrenciesDocument document = new CurrenciesDocument()

        CurrenciesDocument.Currency usdCurrency = new CurrenciesDocument.Currency(
                name: USD_CURRENCY_NAME,
                code: USD_CURRENCY_CODE,
                mid: 1.00,
                lastUpdated: "1900-01-01",
                source: "notNBP"
        )

        CurrenciesDocument.Currency eurCurrency = new CurrenciesDocument.Currency(
                name: EUR_CURRENCY_NAME,
                code: EUR_CURRENCY_CODE,
                mid: 1.00,
                lastUpdated: "1900-01-01",
                source: "notNBP"
        )

        document.setCurrencies(List.of(usdCurrency, eurCurrency))

        return document
    }

    static String REFRESH = "/api/currencies/refresh"
    static String CALCULATE = "/api/currencies/calculate"
    static String NBP_RESPONSE_JSON_PATH = "src/test/resources/NBPResponse.json"
    static String NBP_SOURCE = "NBP"
    static String USD_CURRENCY_CODE = "USD"
    static String USD_CURRENCY_NAME = "dolar amerykański"
    static String EUR_CURRENCY_CODE = "EUR"
    static String EUR_CURRENCY_NAME = "euro"
}
