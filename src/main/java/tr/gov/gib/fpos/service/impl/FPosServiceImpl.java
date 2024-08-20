package tr.gov.gib.fpos.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tr.gov.gib.fpos.service.BankaClient;
import tr.gov.gib.fpos.service.OdemeServisResponseService;
import tr.gov.gib.fpos.object.request.BankaServerRequest;
import tr.gov.gib.fpos.object.request.OdemeServisRequest;
import tr.gov.gib.fpos.object.response.BankaServerResponse;
import tr.gov.gib.fpos.object.response.OdemeServisResponse;
import tr.gov.gib.fpos.service.FPosService;
import tr.gov.gib.gibcore.util.HashUtil;
import tr.gov.gib.gibcore.object.response.GibResponse;
import tr.gov.gib.gibcore.object.request.GibRequest;

@Service("FPosService")
public class FPosServiceImpl implements FPosService {

    private static final Logger logger = LoggerFactory.getLogger(FPosServiceImpl.class);

    private final BankaClient bankaClient;
    private final OdemeServisResponseService odemeServisResponseService;

    public FPosServiceImpl(BankaClient bankaClient, OdemeServisResponseService odemeServisResponseService) {
        this.bankaClient = bankaClient;
        this.odemeServisResponseService = odemeServisResponseService;
    }

    @Override
    public GibResponse<OdemeServisResponse> processOdemeServisRequest(GibRequest<OdemeServisRequest> request) {
        logger.info("Processing OdemeServisRequest data: {}", request.getData());

        OdemeServisRequest odemeRequest = request.getData();

        String generatedHash = HashUtil.generateSHA256(odemeRequest.getOid(), odemeRequest.getKartNo(), odemeRequest.getOdenecekMiktar().toString());
        System.out.println("Hash: " + generatedHash);

        BankaServerRequest bankaRequest = new BankaServerRequest(odemeRequest);

        BankaServerResponse bankaResponse = bankaClient.sendToBankEndpoint(bankaRequest);

        if (generatedHash.equals(bankaResponse.getHash())) {
            OdemeServisResponse response = odemeServisResponseService.createOdemeServisResponse().apply(odemeRequest, bankaResponse);
            odemeServisResponseService.createFizikselPos().apply(odemeRequest, bankaResponse);

            GibResponse<OdemeServisResponse> gibResponse = new GibResponse<>();
            gibResponse.setData(response);

            return gibResponse;
        } else {
            throw new RuntimeException("Hash mismatch: Generated hash does not match the bank's hash.");
        }
    }
}