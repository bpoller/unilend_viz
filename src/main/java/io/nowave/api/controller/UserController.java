package io.nowave.api.controller;

import io.jsonwebtoken.Jwts;
import io.nowave.api.dto.Credentials;
import io.nowave.api.dto.Signup;
import io.nowave.api.model.NoWaveSubscription;
import io.nowave.api.model.User;
import io.nowave.api.repository.UserRepository;
import io.nowave.api.security.CookieFactory;
import io.nowave.api.service.VHXVideoService;
import io.nowave.api.service.billing.BillingPlatformException;
import io.nowave.api.service.billing.BillingService;
import io.nowave.api.service.billing.CreditCardException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

import static io.jsonwebtoken.SignatureAlgorithm.HS256;
import static io.nowave.api.WebSecurityConfig.SIGNING_KEY;
import static java.time.LocalDateTime.now;
import static java.time.ZoneId.systemDefault;
import static java.util.Date.from;
import static java.util.Optional.ofNullable;
import static org.springframework.http.HttpStatus.*;

@RestController
public class UserController {

    private final UserRepository userRepository;
    private final BillingService billingService;
    private VHXVideoService videoService;
    private CookieFactory cookieFactory;
    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    public UserController(final UserRepository userRepository, final BillingService billingService, final VHXVideoService videoService, final CookieFactory cookieFactory) {
        this.userRepository = userRepository;
        this.billingService = billingService;
        this.videoService = videoService;
        this.cookieFactory = cookieFactory;
    }

    @RequestMapping(path = "api/users", method = RequestMethod.POST)
    public ResponseEntity<String> createUser(@RequestBody final Signup signup, final HttpServletResponse response) {
        try {

            final User user = userRepository.findByEmail(signup.getUser().getEmail());

            if (user != null) {
                logger.debug("user already created");
                return new ResponseEntity<>("user already created", PRECONDITION_FAILED);
            }

            final User persistedUser = userRepository.save(signup.getUser());

            final NoWaveSubscription subscription = billingService.subscribe(persistedUser, signup.getStripeToken());
            persistedUser.setBillingId(subscription.getCustomerId());

            videoService.createCustomer(persistedUser);
            videoService.authorizeCustomerForVideos(persistedUser);
            userRepository.save(persistedUser);

            return Optional.of(persistedUser)
                    .map(newUser -> putToken(newUser, response))
                    .orElse(new ResponseEntity<>(UNAUTHORIZED));

        } catch (final CreditCardException e) {
            logger.error("Error with the credit card", e);
            return new ResponseEntity<>(e.getMessage(), BAD_REQUEST);
        } catch (final BillingPlatformException e) {
            logger.error("Error with the billing platform", e);
            return new ResponseEntity<>(e.getMessage(), INTERNAL_SERVER_ERROR);
        } catch (final Exception e) {
            logger.error("An error occurred while saving a user", e);
            return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(path = "/api/users/login", method = RequestMethod.POST)
    public ResponseEntity login(@RequestBody final Credentials credentials, final HttpServletResponse response) {
        final Optional<User> userOptional = ofNullable(userRepository.findByEmail(credentials.getEmail()));

        return userOptional
                .filter(user -> user.validate(credentials.getPassword()))
                .map(user -> putToken(user, response))
                .orElse(new ResponseEntity<>(UNAUTHORIZED));
    }

    private ResponseEntity putToken(final User user, final HttpServletResponse response) {
        final String jwtToken = Jwts.builder()
                .setIssuer("https://nowave.io/")
                .setSubject(user.getId())
                .setExpiration(from(now().plusDays(2).atZone(systemDefault()).toInstant()))
                .signWith(HS256, SIGNING_KEY)
                .compact();

        Cookie cookie = cookieFactory.create(jwtToken);
        response.addCookie(cookie);

        return new ResponseEntity<>(OK);
    }

}