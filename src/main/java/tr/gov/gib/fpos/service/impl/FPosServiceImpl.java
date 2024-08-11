package tr.gov.gib.fpos.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpStatusCodeException;
import tr.gov.gib.fpos.object.request.FPosKartBilgiRequest;
import tr.gov.gib.fpos.object.request.BankaServerRequest;
import tr.gov.gib.fpos.object.response.BankaServerResponse;
import tr.gov.gib.fpos.service.FPosService;

import java.math.BigDecimal;

@Service("FPosService")
public class FPosServiceImpl implements FPosService {

    private static final String BANK_ENDPOINT_PHYSICAL = "http://127.0.0.1:5000/odeme_al/fiziksel_pos";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public FPosServiceImpl() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public BankaServerResponse kartBilgileriniAl(FPosKartBilgiRequest request) {
        try {
            // Create the request payload
            BankaServerRequest bankaRequest = createBankaServerRequest(request);

            // Send the request to the bank endpoint and get the response
            BankaServerResponse bankResponse = sendToBankEndpoint(bankaRequest);

            return bankResponse;
        } catch (Exception e) {
            // Handle the exception
            e.printStackTrace();
            throw new RuntimeException("Error processing request", e);
        }
    }

    private BankaServerRequest createBankaServerRequest(FPosKartBilgiRequest request) {
        // Manually set oid and OdenecekTutar for demonstration
        BigDecimal bd1 = new BigDecimal("100.23");
        return BankaServerRequest.builder()
                .kartNo(request.getKartNo())
                .ccv(request.getCcv())
                .sonKullanimTarihiAy(request.getSonKullanimTarihiAy())
                .sonKullanimTarihiYil(request.getSonKullanimTarihiYil())
                .kartSahibiAd(request.getKartSahibiAd())
                .kartSahibiSoyad(request.getKartSahibiSoyad())
                .oid("12345") // Manually set oid
                .odenecekTutar(bd1) // Manually set OdenecekTutar
                .build();
    }

    private BankaServerResponse sendToBankEndpoint(BankaServerRequest request) {
        try {
            // Prepare request entity
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
            HttpEntity<BankaServerRequest> requestEntity = new HttpEntity<>(request, headers);

            // Send POST request
            ResponseEntity<JsonNode> bankResponse = restTemplate.exchange(
                    BANK_ENDPOINT_PHYSICAL,
                    HttpMethod.POST,
                    requestEntity,
                    JsonNode.class
            );

            // Parse the JSON response
            JsonNode jsonNode = bankResponse.getBody();

            // Create BankaServerResponse from JSON
            BankaServerResponse bankaResponse = BankaServerResponse.builder()
                    .oid(jsonNode.path("oid").asText(null))
                    .message(jsonNode.path("message").asText(null))
                    .status(jsonNode.path("status").asText(null))
                    .bankaAdi(jsonNode.path("bankaAdi").asText(null))
                    .posId(jsonNode.path("posId").asInt(0))
                    .build();

            return bankaResponse;
        } catch (HttpStatusCodeException e) {
            // Log the error details
            System.err.println("Error response code: " + e.getStatusCode());
            System.err.println("Error response body: " + e.getResponseBodyAsString());
            throw e;
        } catch (Exception e) {
            // Log other exceptions
            e.printStackTrace();
            throw e;
        }
    }
}