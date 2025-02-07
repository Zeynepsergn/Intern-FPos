package tr.gov.gib.fpos.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tr.gov.gib.fpos.service.BankaClient;
import tr.gov.gib.fpos.service.OdemeResponseService;
import tr.gov.gib.fpos.service.TransactionService;
import tr.gov.gib.fpos.object.request.BankaServerRequest;
import tr.gov.gib.fpos.object.request.OdemeServisRequest;
import tr.gov.gib.fpos.object.response.BankaServerResponse;
import tr.gov.gib.fpos.object.response.OdemeServisResponse;
import tr.gov.gib.fpos.service.FPosService;
import tr.gov.gib.gibcore.exception.GibException;
import tr.gov.gib.gibcore.util.HashUtil;
import tr.gov.gib.gibcore.object.response.GibResponse;
import tr.gov.gib.gibcore.object.request.GibRequest;

@Service("FPosService")
public class FPosServiceImpl implements FPosService {

    private static final Logger logger = LoggerFactory.getLogger(FPosServiceImpl.class);

    private final BankaClient bankaClient;
    private final TransactionService transactionService;
    private final OdemeResponseService odemeResponseService;

    @Value("${banka.servis.salt}")
    private String salt;

    public FPosServiceImpl(BankaClient bankaClient, TransactionService transactionService, OdemeResponseService odemeResponseService) {
        this.bankaClient = bankaClient;
        this.transactionService = transactionService;
        this.odemeResponseService = odemeResponseService;
    }

    @Override
    public GibResponse<OdemeServisResponse> processOdemeServisRequest(GibRequest<OdemeServisRequest> request) {
        logger.info("Processing OdemeServisRequest data: {}", request.getData());

        OdemeServisRequest odemeRequest = request.getData();
        System.out.println("odemeRequest: " + odemeRequest);
        String generatedHash = HashUtil.generateSHA256(odemeRequest.getOid(), odemeRequest.getKartNo(), odemeRequest.getOdenecekMiktar().toString(),salt);
        System.out.println("Hash: " + generatedHash);

        BankaServerRequest bankaRequest = new BankaServerRequest(odemeRequest);

        BankaServerResponse bankaResponse = bankaClient.sendToBankEndpoint(bankaRequest);

        if (generatedHash.equals(bankaResponse.getHash())) {
            System.out.println("Banka Hash: " + bankaResponse.getHash());
            OdemeServisResponse response = odemeResponseService.createOdemeServisResponse().apply(odemeRequest, bankaResponse);
            transactionService.createFizikselPosData().apply(odemeRequest, bankaResponse);

            GibResponse<OdemeServisResponse> gibResponse = new GibResponse<>();
            gibResponse.setData(response);

            return gibResponse;
        } else {
            throw new GibException("Hash mismatch: Generated hash does not match the bank's hash.");
        }
    }
}