package tr.gov.gib.fpos.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
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
import tr.gov.gib.fpos.entity.FizikselPos;
import tr.gov.gib.fpos.object.enums.FPosDurum;
import tr.gov.gib.fpos.object.request.BankaServerRequest;
import tr.gov.gib.fpos.object.request.OdemeServisRequest;
import tr.gov.gib.fpos.object.response.BankaServerResponse;
import tr.gov.gib.fpos.object.response.OdemeServisResponse;
import tr.gov.gib.fpos.repository.FPosRepository;
import tr.gov.gib.fpos.service.FPosService;
import tr.gov.gib.gibcore.util.HashUtil;
import tr.gov.gib.gibcore.object.response.GibResponse;
import tr.gov.gib.gibcore.object.reuest.GibRequest;

import java.math.BigDecimal;
import java.util.Date;

@Service("FPosService")
public class FPosServiceImpl implements FPosService {

    @Value("${banka.servis.url}")
    private String BANK_ENDPOINT_PHYSICAL;
    private static final Logger logger = LoggerFactory.getLogger(FPosServiceImpl.class);

    private final RestTemplate restTemplate;
    private final FPosRepository fPosRepository;

    public FPosServiceImpl(FPosRepository fPosRepository) {
        this.restTemplate = new RestTemplate();
        this.fPosRepository = fPosRepository;
    }


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


        String generatedHash = HashUtil.generateSHA256(oid, kartNo, odenecekMiktar.toString());
        System.out.println("Hash: " + generatedHash);
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
        // Compare the generated hash with the hash returned from the bank
        if (generatedHash.equals(bankaResponse.getHash())) {
            // Create and return OdemeServisResponse with extracted values and bank response
            OdemeServisResponse response = new OdemeServisResponse();
            response.setOid(oid);
            response.setOdemeOid(odemeRequest.getOdemeOid());
            response.setDurum(FPosDurum.BASARILI_ODEME.getfPosDurumKodu());
            response.setPosId(bankaResponse.getPosId().toString());
            response.setAciklama(bankaResponse.getMessage());
            response.setBankaAdi(bankaResponse.getBankaAdi());

            FizikselPos fizikselPos = new FizikselPos();
            fizikselPos.setOid(oid);
            fizikselPos.setOdemeId(odemeRequest.getOdemeOid());
            fizikselPos.setKartSahibi(kartSahibi);
            fizikselPos.setKartBanka(bankaResponse.getBankaAdi());
            fizikselPos.setPosIslemId(bankaResponse.getPosId().toString());
            fizikselPos.setOptime(new Date());
            fizikselPos.setDurum((short) 0);
            fPosRepository.save(fizikselPos);

            // Wrap the response in a GibResponse
            GibResponse<OdemeServisResponse> gibResponse = new GibResponse<>();
            gibResponse.setData(response);

            return gibResponse;
        } else {
            // Handle hash mismatch case
            throw new RuntimeException("Hash mismatch: Generated hash does not match the bank's hash.");
        }
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
                    .hash(jsonNode.path("hash").asText(null))
                    .build();

            logger.info("Processing OdemeServisRequest data: {}", bankaResponse);
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