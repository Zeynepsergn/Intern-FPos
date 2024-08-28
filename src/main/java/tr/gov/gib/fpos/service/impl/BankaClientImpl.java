// src/main/java/tr/gov/gib/fpos/service/impl/BankaClientImpl.java
package tr.gov.gib.fpos.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpStatusCodeException;
import tr.gov.gib.fpos.object.request.BankaServerRequest;
import tr.gov.gib.fpos.object.response.BankaServerResponse;
import tr.gov.gib.fpos.service.BankaClient;

@Component
public class BankaClientImpl implements BankaClient {

    @Value("${banka.servis.url}")
    private String BANK_ENDPOINT;
    private static final Logger logger = LoggerFactory.getLogger(BankaClientImpl.class);

    private final RestTemplate restTemplate;

    public BankaClientImpl() {
        this.restTemplate = new RestTemplate();
    }

    @Override
    public BankaServerResponse sendToBankEndpoint(BankaServerRequest request) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
            HttpEntity<BankaServerRequest> requestEntity = new HttpEntity<>(request, headers);

            ResponseEntity<JsonNode> bankResponse = restTemplate.exchange(
                    BANK_ENDPOINT,
                    HttpMethod.POST,
                    requestEntity,
                    JsonNode.class
            );

            JsonNode jsonNode = bankResponse.getBody();

            BankaServerResponse bankaResponse = BankaServerResponse.builder()
                    .oid(jsonNode.path("oid").asText(null))
                    .message(jsonNode.path("message").asText(null))
                    .status(jsonNode.path("status").asText(null))
                    .bankaAdi(jsonNode.path("bankaAdi").asText(null))
                    .posId(jsonNode.path("posId").asInt(0))
                    .hash(jsonNode.path("hash").asText(null))
                    .build();

            logger.info("Received response from bank: {}", bankaResponse);
            return bankaResponse;
        } catch (HttpStatusCodeException e) {
            logger.error("Error response code: {}", e.getStatusCode());
            logger.error("Error response body: {}", e.getResponseBodyAsString());
            throw e;
        } catch (Exception e) {
            logger.error("Exception occurred: ", e);
            throw e;
        }
    }
}