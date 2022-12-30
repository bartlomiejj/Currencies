package currencies.infrastructure.provider;

import currencies.infrastructure.provider.protocol.AbstractProviderResponse;

public interface ProviderService {

    AbstractProviderResponse getCurrencies();
}
