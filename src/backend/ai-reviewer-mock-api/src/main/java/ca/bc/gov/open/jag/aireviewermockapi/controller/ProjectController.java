package ca.bc.gov.open.jag.aireviewermockapi.controller;

import ca.bc.gov.open.jag.aireviewermockapi.config.AiReviewerApiProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.util.Map;

@Controller
@EnableConfigurationProperties(AiReviewerApiProperties.class)
public class ProjectController {

    private final AiReviewerApiProperties aiReviewerApiProperties;

    public ProjectController(AiReviewerApiProperties aiReviewerApiProperties) {
        this.aiReviewerApiProperties = aiReviewerApiProperties;
    }

    @PostMapping("/api/projects/{projectId}/documents")
    public ResponseEntity postFile(@PathVariable String projectId) {
        return ResponseEntity.ok("accepted");
    }

    @GetMapping("/api/projects/{projectId}/documents")
    public ResponseEntity getFileId(@PathVariable String projectId,
                                    @RequestParam Map<String, String> filter) {
        String fileName = filter.get("filter[fileName]");
        String sendWebhookEvent = filter.get("send_event");

        if (fileName.equals("test-valid-document.pdf")) {
            if(sendWebhookEvent != null && sendWebhookEvent.equals("true")) {
                new Thread(() -> sendWebhookEvent()).start();
            }
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\n" +
                            "  \"data\": {\n" +
                            "    \"documents\": [\n" +
                            "      {\n" +
                            "        \"file_id\": 1234,\n" +
                            "        \"file_status\": \"PROCESSED\"\n" +
                            "      }\n" +
                            "    ]\n" +
                            "  }\n" +
                            "}");
        }

        if (fileName.equals("test-invalid-document.pdf")) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\n" +
                            "  \"data\": {\n" +
                            "    \"documents\": [\n" +
                            "      {\n" +
                            "        \"file_id\": 9999,\n" +
                            "        \"file_status\": \"PROCESSED\"\n" +
                            "      }\n" +
                            "    ]\n" +
                            "  }\n" +
                            "}");
        }


        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    private void sendWebhookEvent() {
        String baseUrl = aiReviewerApiProperties.getBaseUrl();
        String url = MessageFormat.format("{0}{1}", baseUrl, "/documents/webhookEvent");

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String body = "{\n" +
                "  \"data\": [\n" +
                "    {\n" +
                "      \"id\": 1234,\n" +
                "      \"status\": \"PROCESSED\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"event\": \"FILE_PROCESSED\"\n" +
                "}";

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);
    }

}
