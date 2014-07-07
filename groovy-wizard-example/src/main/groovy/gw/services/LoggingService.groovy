package gw.services

import groovy.util.logging.Slf4j

@Slf4j
class LoggingService {

    void logInfo(String message) {
        log.info(message)
    }

}
