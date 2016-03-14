package io.nowave.api.controller;

import io.nowave.api.dto.ClientConfig;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class ClientConfigControllerTest {

    MockMvc mockMvc;

    @InjectMocks
    ClientConfigController clientConfigController;

    @Before
    public void setup() {
        initMocks(this);

        mockMvc = standaloneSetup(clientConfigController).setMessageConverters(new MappingJackson2HttpMessageConverter())
                .build();

        clientConfigController.setClientConfig(new ClientConfig("stripe", "mixpanel"));
    }

    @Test
    public void thatRetrievingAStripeKeyWorks() throws Exception {

        this.mockMvc
                .perform(
                        get("/api/config").contentType(APPLICATION_JSON)
                                .accept(APPLICATION_JSON)).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.stripeKey", is(notNullValue())));

    }
}