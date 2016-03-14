package io.bpoller.unilend.controller;

import io.bpoller.unilend.model.SayHi;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class HelloController {

    private Logger logger = getLogger(getClass());

    @RequestMapping(path = "/api/hello", method = GET)
    public ResponseEntity<SayHi> sayHello() {

        logger.info("Hello {}", "world");
        return new ResponseEntity<>(new SayHi("Hello World"), OK);
    }

    @RequestMapping(path = "/", method = GET)
    public ResponseEntity<String> index() {
        return new ResponseEntity<>("Up and running", OK);
    }
}