package tr.gov.gib.fpos.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpStatusCodeException;
import tr.gov.gib.fpos.object.request.BankaServerRequest;
import tr.gov.gib.fpos.object.request.OdemeServisRequest;
import tr.gov.gib.fpos.object.response.BankaServerResponse;
import tr.gov.gib.fpos.object.response.OdemeServisResponse;
import tr.gov.gib.fpos.service.FPosService;
import tr.gov.gib.gibcore.object.response.GibResponse;
import tr.gov.gib.gibcore.object.reuest.GibRequest;

import java.math.BigDecimal;

@Service("FPosService")
public class FPosServiceImpl implements FPosService {
    @Value("${banka.servis.url}")
    private String BANK_ENDPOINT_PHYSICAL;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public FPosServiceImpl() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }
    private static final Logger logger = LoggerFactory.getLogger(FPosServiceImpl.class);

    @Override
    public GibResponse<OdemeServisResponse> processOdemeServisRequest(GibRequest<OdemeServisRequest> request) {
        // Log the request data
        logger.info("Processing OdemeServisRequest data: {}", request.getData());

        // Extract specific data from the request
        OdemeServisRequest odemeRequest = request.getData();
        String oid = odemeRequest.getOid();
        BigDecimal odenecekMiktar = odemeRequest.getOdenecekMiktar();
        String kartNo = odemeRequest.getKartNo();
        Integer ccv = odemeRequest.getCcv();
        Integer sonKullanimTarihiAy = odemeRequest.getSonKullanimTarihiAy();
        Integer sonKullanimTarihiYil = odemeRequest.getSonKullanimTarihiYil();
        String kartSahibi = odemeRequest.getKartSahibi();

        // Create BankaServerRequest to get card information
        BankaServerRequest bankaRequest = BankaServerRequest.builder()
                .oid(oid)
                .odenecekTutar(odenecekMiktar)
                .kartNo(kartNo)
                .ccv(ccv)
                .sonKullanimTarihiAy(sonKullanimTarihiAy)
                .sonKullanimTarihiYil(sonKullanimTarihiYil)
                .kartSahibi(kartSahibi)
                .build();

        // Get card information from the bank
        BankaServerResponse bankaResponse = sendToBankEndpoint(bankaRequest);

        // Create and return OdemeServisResponse with extracted values and bank response
        OdemeServisResponse response = new OdemeServisResponse();
        response.setOid(oid);
        response.setOdemeOid(odemeRequest.getOdemeOid());
        response.setDurum((short) 1);
        response.setPosId(bankaResponse.getPosId().toString());
        response.setAciklama(bankaResponse.getMessage());
        response.setBankaAdi(bankaResponse.getBankaAdi());

        // Wrap the response in a GibResponse
        GibResponse<OdemeServisResponse> gibResponse = new GibResponse<>();
        gibResponse.setData(response);

        return gibResponse;
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
                    .bankaAdi(jsonNode.path("banka_adi").asText(null))
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