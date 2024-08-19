// src/main/java/tr/gov/gib/fpos/service/impl/FPosServiceImpl.java
package tr.gov.gib.fpos.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tr.gov.gib.fpos.service.BankaClient;
import tr.gov.gib.fpos.entity.FizikselPos;
import tr.gov.gib.gibcore.object.enums.FposSposNakitDurum;
import tr.gov.gib.fpos.object.request.BankaServerRequest;
import tr.gov.gib.fpos.object.request.OdemeServisRequest;
import tr.gov.gib.fpos.object.response.BankaServerResponse;
import tr.gov.gib.fpos.object.response.OdemeServisResponse;
import tr.gov.gib.fpos.repository.FPosRepository;
import tr.gov.gib.fpos.service.FPosService;
import tr.gov.gib.gibcore.util.HashUtil;
import tr.gov.gib.gibcore.object.response.GibResponse;
import tr.gov.gib.gibcore.object.request.GibRequest;

import java.util.Date;

@Service("FPosService")
public class FPosServiceImpl implements FPosService {

    private static final Logger logger = LoggerFactory.getLogger(FPosServiceImpl.class);

    private final BankaClient bankaClient;
    private final FPosRepository fPosRepository;

    public FPosServiceImpl(BankaClient bankaClient, FPosRepository fPosRepository) {
        this.bankaClient = bankaClient;
        this.fPosRepository = fPosRepository;
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
            OdemeServisResponse response = new OdemeServisResponse();
            response.setOid(odemeRequest.getOid());
            response.setOdemeOid(odemeRequest.getOdemeOid());
            response.setDurum(FposSposNakitDurum.BASARILI_ODEME.getSposFposNakitDurumKodu());
            response.setPosId(bankaResponse.getPosId().toString());
            response.setAciklama(bankaResponse.getMessage());
            response.setBankaAdi(bankaResponse.getBankaAdi());

            FizikselPos fizikselPos = new FizikselPos();
            fizikselPos.setOid(odemeRequest.getOid());
            fizikselPos.setOdemeId(odemeRequest.getOdemeOid());
            fizikselPos.setKartSahibi(odemeRequest.getKartSahibi());
            fizikselPos.setKartBanka(bankaResponse.getBankaAdi());
            fizikselPos.setPosIslemId(bankaResponse.getPosId().toString());
            fizikselPos.setOptime(new Date());
            fizikselPos.setDurum((short) 0);
            fPosRepository.save(fizikselPos);

            GibResponse<OdemeServisResponse> gibResponse = new GibResponse<>();
            gibResponse.setData(response);

            return gibResponse;
        } else {
            throw new RuntimeException("Hash mismatch: Generated hash does not match the bank's hash.");
        }
    }
}