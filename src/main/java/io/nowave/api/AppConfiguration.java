package io.nowave.api;

import com.mongodb.MongoClientURI;
import io.nowave.api.dto.ClientConfig;
import io.nowave.api.repository.UserRepository;
import io.nowave.api.security.CookieFactory;
import io.nowave.api.security.DevCookieFactory;
import io.nowave.api.security.ProdCookieFactory;
import io.nowave.api.service.billing.BillingService;
import io.nowave.api.service.billing.BillingServiceWithStripe;
import io.nowave.api.service.EmailService;
import io.nowave.api.service.SendGridEmailService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.net.UnknownHostException;

@Configuration
@EnableMongoRepositories
public class AppConfiguration {

    private final Log logger = LogFactory.getLog(getClass());

    @Bean
    @Profile("production")
    public EmailService emailServiceProd() {
        final String sendGridApiKey = "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";
        return new SendGridEmailService(sendGridApiKey);
    }

    @Bean
    @Profile("dev")
    public EmailService emailServiceDev() {
        return (to, from, subject, htmlBody) -> logger.info("Sending an email to " + to);
    }

    @Bean
    @Profile("dev")
    public WebMvcConfigurer corsConfigurerDev() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(final CorsRegistry registry) {
                logger.info("cors dev");
                registry.addMapping("/api/**").allowedOrigins("http://localhost:9000");
            }
        };
    }

    @Bean
    @Profile("dev")
    public CookieFactory cookieFactoryDev() {
        return new DevCookieFactory();
    }

    @Bean
    @Profile("production")
    public CookieFactory cookieFactoryProd() {
        return new ProdCookieFactory();
    }

    @Bean
    @Profile("production")
    public WebMvcConfigurer corsConfigurerProduction() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(final CorsRegistry registry) {
                logger.info("cors prod");
                registry.addMapping("/api/**").allowedOrigins("https://nowave.io");
            }
        };
    }

    @Bean(name = "mongoTemplate")
    @Profile("dev")
    public MongoTemplate mongoTemplateDev() throws UnknownHostException {
        final String connectionURL = "mongodb://nowave:123@ds019268.mlab.com:19268/dev01";
        return new MongoTemplate(new SimpleMongoDbFactory(new MongoClientURI(connectionURL)));
    }

    @Bean(name = "mongoTemplate")
    @Profile("production")
    public MongoTemplate mongoTemplateProd() throws UnknownHostException {
        final String connectionURL = "mongodb://nowave:sfgd3fhfjgkhgsdfgdfgdfghfgyj@ds019678.mlab.com:19678/prod01";
        return new MongoTemplate(new SimpleMongoDbFactory(new MongoClientURI(connectionURL)));
    }

    @Bean(name = "mongoTemplate")
    @Profile("test")
    public MongoTemplate mongoTemplateTest() throws UnknownHostException {
        final String connectionURL = "mongodb://localhost/nowave-test";
        return new MongoTemplate(new SimpleMongoDbFactory(new MongoClientURI(connectionURL)));
    }

    @Bean
    @Profile("dev")
    public BillingService billingServiceDev() {
        return new BillingServiceWithStripe("sk_test_HxB1TBTeWe87ftcRIVhtD3Fa");
    }

    @Bean
    @Profile("production")
    public BillingService billingServiceProd() {
//        return new BillingServiceWithStripe("sk_live_MdUgFaNk25cRJoRjhi2iyPuV");
        // FIXME Nico: this is the test key to do some testing on the prod infrastructure
        return new BillingServiceWithStripe("sk_test_HxB1TBTeWe87ftcRIVhtD3Fa");
    }

    @Bean
    @Profile("dev")
    public ClientConfig clientConfigDev() {
        return new ClientConfig("pk_test_Udjn5oL0zaLPPorQrZqJqyI1", "1599bfb2dc59d75cdba51898c509095b");
    }

    @Bean
    @Profile("production")
    public ClientConfig clientConfigProd() {
//        return new ClientConfig("pk_live_ABZS6LI6hsEtbtAubzJNgYub", "25756e4ff862ecb4b82785998e72bdff");
        // FIXME Nico: this is the test key to do some testing on the prod infrastructure
        return new ClientConfig("pk_test_Udjn5oL0zaLPPorQrZqJqyI1", "25756e4ff862ecb4b82785998e72bdff");
    }

    @Bean
    @Profile("dev")
    public DataBootStrap dataBootStrap(UserRepository userRepository) {
        return new DataBootStrap(userRepository);
    }


}
