package io.nowave.api.service;


import io.nowave.api.dto.vhx.*;
import org.junit.Test;

import java.util.Optional;

import static io.nowave.api.repository.UserDataProvider.userToBeSaved;
import static java.util.Optional.empty;
import static org.junit.Assert.*;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

public class VHXVideoServiceTest {


    VHXVideoService videoService = new VHXVideoService();

    @Test
    public void shouldNotDeleteUnexistingCustomer() {
        assertEquals("Should be 404", videoService.deleteCustomer(1213123312131L), NOT_FOUND);
    }

    @Test
    public void shouldDeleteExistingCustomer() {
        VhxCustomer customer = videoService.createCustomer(userToBeSaved());
        assertEquals("Should be 200", videoService.deleteCustomer(customer.getId()), OK);
    }

    @Test
    public void shouldGetExistingCustomer() {
        VhxCustomer customer = videoService.createCustomer(userToBeSaved());
        VhxCustomer customer2 = videoService.getCustomer(customer.getEmail()).get();
        assertEquals("Should be equal to email", customer2.getEmail(), "bpoller@ekito.fr");
        videoService.deleteCustomer(customer2.getId());
    }

    @Test
    public void shouldReturnEmptyWhenGettingNotExistingCustomer() {
        Optional<VhxCustomer> customerOptional = videoService.getCustomer("titi@toto.com");
        assertEquals("Should be empty", customerOptional, empty());
    }

    @Test
    public void shouldGetExistingVideo() {
        VhxVideo video = videoService.getVideo("bees").get();
        assertEquals("Title should be 'bees'", video.getTitle(), "bees");
    }

    @Test
    public void shouldReturnEmptyWhenGettingNotExistingVideo() {
        Optional<VhxVideo> video = videoService.getVideo("Not there");
        assertEquals("video should not exist", video, empty());
    }

    @Test
    public void shouldObtainAuthorizationForKnownCustomerAndVideo() {
        VhxVideo video = videoService.getVideo("bees").get();
        VhxCustomer customer = videoService.createCustomer(userToBeSaved());
        Authorization authorization = videoService.getAuthorization(customer, video);
        assertTrue("Authorization should be granted", authorization.getToken() != null);
        videoService.deleteCustomer(customer.getId());
    }

    @Test
    public void shouldGetATrailer(){
        Optional<Trailer> trailer = videoService.getTrailerFor("bees");
        assertNotNull("There should be a trailer",trailer.get().getHtml());
    }
}