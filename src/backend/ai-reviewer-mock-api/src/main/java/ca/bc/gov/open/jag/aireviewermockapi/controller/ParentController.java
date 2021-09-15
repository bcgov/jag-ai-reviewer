package ca.bc.gov.open.jag.aireviewermockapi.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class ParentController {

    Logger logger = LoggerFactory.getLogger(ParentController.class);

    @PostMapping("/airsavejsondata")
    public ResponseEntity documentReady(@RequestBody Object documentReady) {

        logger.info("Request received");

        return ResponseEntity.ok("Something Happened");

    }
}
