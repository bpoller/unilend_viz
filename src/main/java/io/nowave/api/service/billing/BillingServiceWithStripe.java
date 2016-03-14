package io.nowave.api.service.billing;

import com.stripe.Stripe;
import com.stripe.exception.*;
import com.stripe.model.Customer;
import io.nowave.api.model.BasicMonthlySubscription;
import io.nowave.api.model.NoWaveSubscription;
import io.nowave.api.model.User;

import java.util.HashMap;
import java.util.Map;

public class BillingServiceWithStripe implements BillingService {

    public BillingServiceWithStripe(String apiKey) {
        Stripe.apiKey = apiKey;
    }


    @Override
    public NoWaveSubscription subscribe(User user, String token) throws CreditCardException, BillingPlatformException {

        NoWaveSubscription subscription = new BasicMonthlySubscription();

        Map<String, Object> customerParams = new HashMap<>();
        customerParams.put("email", user.getEmail());
        customerParams.put("source", token);

        try {
            Customer customer = Customer.create(customerParams);

            subscription.setCustomerId(customer.getId());

            Map<String, Object> subscriptionParams = new HashMap<>();
            subscriptionParams.put("plan", subscription.getPlanId());
            customer.createSubscription(subscriptionParams);

        } catch (AuthenticationException e) {
            throw new BillingPlatformException("Unable to authenticate on the billing platform", e);
        } catch (InvalidRequestException e) {
            throw new BillingPlatformException("Bad request to the billing platform", e);
        } catch (APIConnectionException e) {
            throw new BillingPlatformException("API connection error on the billing platform", e);
        } catch (CardException e) {
            throw new CreditCardException(e.getMessage(), e);
        } catch (APIException e) {
            throw new BillingPlatformException("API error on the billing platform", e);
        }

        return subscription;
    }
}
