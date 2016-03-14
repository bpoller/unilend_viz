package io.nowave.api.service.billing;

import io.nowave.api.model.NoWaveSubscription;
import io.nowave.api.model.User;

public interface BillingService {

    NoWaveSubscription subscribe(User user, String token) throws CreditCardException, BillingPlatformException;

}

