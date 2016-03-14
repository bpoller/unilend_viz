package io.nowave.api;

import io.nowave.api.repository.UserDataProvider;
import io.nowave.api.repository.UserRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


public class DataBootStrap {

    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    public DataBootStrap(UserRepository userRepository) {
        logger.info("Loading bootstrap data...");
        userRepository.save(UserDataProvider.userToBeSaved());
    }
}
