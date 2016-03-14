package io.nowave.api.service;

import io.nowave.api.dto.vhx.*;
import io.nowave.api.model.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

import static org.springframework.http.HttpMethod.*;

@Service
public class VHXVideoService implements VideoService {

    private final static String APIKey = "Basic bUFzTXBUeXdCMWNob0Roem50eS1WQm96SzhCd2lvanQ6";
    private final static String API = "https://api.vhx.tv";
    private final static String NoWave_PRODUCT_URL = "https://api.vhx.tv/products/14931";

    private RestTemplate template;
    HttpHeaders headers;

    public VHXVideoService() {
        headers = new HttpHeaders();
        headers.add("Authorization", APIKey);
        template = new RestTemplate();
    }

    public void authorizeCustomerForVideos(User persistedUser) {
    }

    @Override
    public Optional<Authorization> getVideoAccessAuthorization(User user, String videoTitle) {
        VhxCustomer customer = getCustomer(user.getEmail()).orElse(createCustomer(user));
        return authorizeCustomerForVideo(customer, videoTitle);
    }

    @Override
    /**
     * Note, by convention, there must be one product / subscription created for each film.
     * The product's name and the film's title must be the same.
     */
    public Optional<Trailer> getTrailerFor(String videoTitle) {
        return getTrailer(videoTitle).map(VhxVideo::toTrailer);
    }

    public VhxCustomer createCustomer(User user) {
        HttpEntity request = new HttpEntity<>(headers);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(API + "/customers")
                .queryParam("product", NoWave_PRODUCT_URL)
                .queryParam("email", user.getEmail())
                .queryParam("name", user.getFirstName() + " " + user.getLastName());

        return template.exchange(builder.build().encode().toUri(), POST, request, VhxCustomer.class).getBody();
    }

    protected HttpStatus deleteCustomer(long customerId) {
        HttpEntity<String> request = new HttpEntity<>(headers);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(API + "/customers/" + customerId)
                .queryParam("product", NoWave_PRODUCT_URL);

        try {
            return template.exchange(builder.build().encode().toUri(), DELETE, request, String.class).getStatusCode();
        } catch (HttpClientErrorException ex) {
            return ex.getStatusCode();
        }
    }

    protected Optional<VhxCustomer> getCustomer(String email) {
        HttpEntity request = new HttpEntity<>(headers);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(API + "/customers")
                .queryParam("email", email);

        CustomerList customerList =
                template.exchange(builder.build().encode().toUri(), GET, request, CustomerList.class).getBody();

        return customerList.getCustomerEmbed().first();
    }

    protected VideoEmbed getVideoEmbedded(String title) {
        HttpEntity request = new HttpEntity<>(headers);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(API + "/videos")
                .queryParam("query", title);

        VideoList videoList =
                template.exchange(builder.build().encode().toUri(), GET, request, VideoList.class).getBody();
        return videoList.getVideoEmbed();
    }

    public Optional<VhxVideo> getVideo(String title) {
        return getVideoEmbedded(title).video();
    }

    public Optional<VhxVideo> getTrailer(String title) {
        return getVideoEmbedded(title).trailer();
    }

    protected Authorization getAuthorization(VhxCustomer customer, VhxVideo video) {
        HttpEntity request = new HttpEntity<>(headers);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(API + "/authorizations")
                .queryParam("customer", API + "/customers/" + customer.getId())
                .queryParam("video", API + "/videos/" + video.getId());

        Authorization authorization =
                template.exchange(builder.build().encode().toUri(), POST, request, Authorization.class).getBody();
        authorization.setNewPlayer(video.playerHtml());
        authorization.setVideoId(video.getId());
        return authorization;
    }

    protected Optional<Authorization> authorizeCustomerForVideo(VhxCustomer customer, String videoTitle) {
        return getVideo(videoTitle).map(video -> getAuthorization(customer, video));
    }
}