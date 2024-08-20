package tr.gov.gib.fpos.service.impl;

import org.springframework.stereotype.Service;
import tr.gov.gib.fpos.entity.FizikselPos;
import tr.gov.gib.fpos.object.request.OdemeServisRequest;
import tr.gov.gib.fpos.object.response.BankaServerResponse;
import tr.gov.gib.fpos.object.response.OdemeServisResponse;
import tr.gov.gib.fpos.repository.FPosRepository;
import tr.gov.gib.fpos.service.OdemeServisResponseService;
import tr.gov.gib.gibcore.object.enums.FposSposNakitDurum;

import java.util.Date;
import java.util.function.BiFunction;

@Service
public class OdemeResponseServiceImpl implements OdemeServisResponseService {

    private final FPosRepository fPosRepository;

    public OdemeResponseServiceImpl(FPosRepository fPosRepository) {
        this.fPosRepository = fPosRepository;
    }

    @Override
    public BiFunction<OdemeServisRequest, BankaServerResponse, OdemeServisResponse> createOdemeServisResponse() {
        return (odemeRequest, bankaResponse) -> {
            OdemeServisResponse response = new OdemeServisResponse();
            response.setOid(odemeRequest.getOid());
            response.setOdemeOid(odemeRequest.getOdemeOid());
            response.setDurum(FposSposNakitDurum.BASARILI_ODEME.getSposFposNakitDurumKodu());
            response.setPosId(bankaResponse.getPosId().toString());
            response.setAciklama(bankaResponse.getMessage());
            response.setBankaAdi(bankaResponse.getBankaAdi());
            return response;
        };
    }

    @Override
    public BiFunction<OdemeServisRequest, BankaServerResponse, FizikselPos> createFizikselPos() {
        return (odemeRequest, bankaResponse) -> {
            FizikselPos fizikselPos = new FizikselPos();
            fizikselPos.setOid(odemeRequest.getOid());
            fizikselPos.setOdemeId(odemeRequest.getOdemeOid());
            fizikselPos.setKartSahibi(odemeRequest.getKartSahibi());
            fizikselPos.setKartBanka(bankaResponse.getBankaAdi());
            fizikselPos.setPosIslemId(bankaResponse.getPosId().toString());
            fizikselPos.setOptime(new Date());
            fizikselPos.setDurum(FposSposNakitDurum.BASARILI_ODEME.getSposFposNakitDurumKodu());
            fPosRepository.save(fizikselPos);
            return fizikselPos;
        };
    }
}