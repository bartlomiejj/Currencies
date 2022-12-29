package currencies.infrastructure.provider.protocol;

import currencies.shared.Currency;

import java.util.Collection;

public abstract class ProviderResponse {

    public abstract Collection<Currency> getCurrencies();
    public abstract String getLastUpdated();
    public abstract String getSource();
}
