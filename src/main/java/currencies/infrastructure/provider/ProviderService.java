package currencies.infrastructure.provider;

import currencies.infrastructure.provider.protocol.ProviderResponse;

public interface ProviderService {

    ProviderResponse getCurrencies();
}
