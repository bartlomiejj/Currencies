package currencies.infrastructure.provider.protocol;

import currencies.shared.Currency;

import java.util.Collection;

public abstract class AbstractProviderResponse {

    public abstract Collection<Currency> getCurrencies();
    public abstract String getLastUpdated();
    public abstract String getSource();
}
