package CV.currencies.infrastructure.provider.protocol;

import CV.currencies.shared.Currency;

import java.util.Collection;

public abstract class ProviderResponse {

    public abstract Collection<Currency> getCurrencies();
    public abstract String getLastUpdated();
    public abstract String getSource();
}
