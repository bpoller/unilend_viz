package io.nowave.api.dto.vhx;


import com.fasterxml.jackson.annotation.JsonProperty;

public class CustomerList {


    @JsonProperty("_embedded")
    CustomerEmbed customerEmbed;

    private CustomerList() {

    }

    public CustomerEmbed getCustomerEmbed() {
        return customerEmbed;
    }
}
