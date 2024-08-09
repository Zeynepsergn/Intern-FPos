package tr.gov.gib.fpos.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import tr.gov.gib.fpos.object.request.FPosSorguRequest;
import tr.gov.gib.fpos.object.response.FPosResponse;
import tr.gov.gib.fpos.service.FPosService;
import tr.gov.gib.gibcore.object.response.GibResponse;
import org.springframework.core.ParameterizedTypeReference;
import java.math.BigDecimal;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

@Service("FPosService")
public class FPosServiceImpl implements FPosService {

    @Autowired
    private WebClient webClient;

    private static final String BANK_ENDPOINT = "http://localhost:7010/odeme_al";

    public GibResponse<FPosResponse> kartBilgileriniAl(FPosSorguRequest request) {
        FPosResponse fPosResponse = createFPosResponse(request);

        GibResponse<FPosResponse> result = new GibResponse<>();
        result.setData(fPosResponse);

        try {
            // Send the response to the bank endpoint and get the response
            return sendToBankEndpoint(result);
        } catch (JsonProcessingException e) {
            // Handle the exception
            e.printStackTrace();
            // Return an appropriate response or rethrow the exception
            throw new RuntimeException("Error processing JSON", e);
        }
    }

    private FPosResponse createFPosResponse(FPosSorguRequest request) {
        BigDecimal bd1 = new BigDecimal("123.45");
        return FPosResponse.builder()
                .kartNo(request.getKartNo())
                .ccv(request.getCcv())
                .sonKullanimTarihiAy(request.getSonKullanimTarihiAy())
                .sonKullanimTarihiYil(request.getSonKullanimTarihiYil())
                .kartSahibiAd(request.getKartSahibiAd())
                .kartSahibiSoyad(request.getKartSahibiSoyad())
                .oid("1234") // Manually set oid
                .odenecekTutar(bd1) // Manually set OdenecekTutar
                .build();
    }

    private GibResponse<FPosResponse> sendToBankEndpoint(GibResponse<FPosResponse> response) throws JsonProcessingException {
        try {
            String bankResponse = webClient.post()
                    .uri(BANK_ENDPOINT)
                    .body(Mono.just(response), GibResponse.class)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            // Parse the JSON response
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(bankResponse);

            // Create FPosResponse from JSON
            FPosResponse fPosResponse = FPosResponse.builder()
                    .oid(jsonNode.get("oid").asText())
                    .message(jsonNode.get("message").asText())
                    .status(jsonNode.get("status").asText())
                    .build();

            // Wrap in GibResponse
            GibResponse<FPosResponse> gibResponse = new GibResponse<>();
            gibResponse.setData(fPosResponse);

            return gibResponse;
        } catch (WebClientResponseException e) {
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