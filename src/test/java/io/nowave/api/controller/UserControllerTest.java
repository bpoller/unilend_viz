package io.nowave.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.nowave.api.dto.Signup;
import io.nowave.api.model.BasicMonthlySubscription;
import io.nowave.api.model.User;
import io.nowave.api.repository.UserRepository;
import io.nowave.api.service.billing.BillingService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;

import java.net.ConnectException;

import static io.nowave.api.repository.UserDataProvider.userToBeSaved;
import static io.nowave.api.repository.UserDataProvider.userToBeSavedJSON;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@SuppressWarnings("ALL")
public class UserControllerTest {

    MockMvc mockMvc;

    @InjectMocks
    UserController userController;

    @Mock
    UserRepository userRepository;

    @Mock
    BillingService billingService;


    @Before
    public void setup() {
        initMocks(this);

        mockMvc = standaloneSetup(userController).setMessageConverters(new MappingJackson2HttpMessageConverter())
                .build();
    }

    /*
     * CREATE user controller test
     */
    @Test
    public void thatCreateNewUserIsCreatedSuccessfully() throws Exception {


        when(userRepository.findByEmail(anyString())).thenReturn(null);
        when(userRepository.save(any(User.class))).thenReturn(userToBeSaved());
        when(billingService.subscribe(any(User.class), any(String.class))).thenReturn(new BasicMonthlySubscription());

        Signup signup = new Signup();
        signup.setUser(userToBeSaved());
        signup.setStripeToken("stripe");
        ObjectMapper mapper = new ObjectMapper();

        this.mockMvc
                .perform(
                        post("/api/users").content(mapper.writeValueAsString(signup)).contentType(APPLICATION_JSON)
                                .accept(APPLICATION_JSON)).andDo(print()).andExpect(status().isCreated());

        verify(userRepository).findByEmail(anyString());
        verify(userRepository, times(2)).save(any(User.class));
        verify(billingService).subscribe(any(User.class), anyString());
    }

    /*
     * CREATE user controller test
     */
    @Test
    public void thatCreateExistingUserIsNotOverwritten() throws Exception {

        when(userRepository.findByEmail(anyString())).thenReturn(userToBeSaved());

        Signup signup = new Signup();
        signup.setUser(userToBeSaved());
        signup.setStripeToken("stripe");
        ObjectMapper mapper = new ObjectMapper();

        this.mockMvc
                .perform(
                        post("/api/users").content(mapper.writeValueAsString(signup)).contentType(APPLICATION_JSON)
                                .accept(APPLICATION_JSON)).andDo(print()).andExpect(status().is(412));

        verify(userRepository).findByEmail(anyString());
    }

    @Test
    public void thatCreateUserUsesInternalServerErrorOnDBConnectionFailure() throws Exception {
        when(userRepository.save(any(User.class))).thenThrow(ConnectException.class);
        this.mockMvc
                .perform(
                        post("/api/users").content(userToBeSavedJSON()).contentType(APPLICATION_JSON)
                                .accept(APPLICATION_JSON)).andDo(print())
                .andExpect(status().isInternalServerError());
    }
}