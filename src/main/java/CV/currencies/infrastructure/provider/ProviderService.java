package CV.currencies.infrastructure.provider;

import CV.currencies.infrastructure.provider.protocol.ProviderResponse;

public interface ProviderService {

    ProviderResponse getCurrencies();
}
