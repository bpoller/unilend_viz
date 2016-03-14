package io.nowave.api.dto.vhx;


import java.util.List;
import java.util.Optional;

public class CustomerEmbed {


    List<VhxCustomer> customers;

    private CustomerEmbed() {
    }

    public List<VhxCustomer> getCustomers() {
        return customers;
    }

    public Optional<VhxCustomer> first() {
        return customers.stream().findFirst();
    }
}
