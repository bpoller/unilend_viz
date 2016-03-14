

package io.nowave.api.controller;


import io.nowave.api.dto.ClientConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClientConfigController {

    private ClientConfig clientConfig;

    @RequestMapping(path = "/api/config", method = RequestMethod.GET)
    public ResponseEntity<ClientConfig> getConfig() {

        return new ResponseEntity<>(clientConfig, HttpStatus.OK);

    }

    @Autowired
    public void setClientConfig(ClientConfig clientConfig) {
        this.clientConfig = clientConfig;
    }

}
