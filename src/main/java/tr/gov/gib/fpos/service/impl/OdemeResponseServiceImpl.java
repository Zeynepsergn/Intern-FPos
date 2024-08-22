package tr.gov.gib.fpos.service.impl;

import org.springframework.stereotype.Service;
import tr.gov.gib.fpos.object.request.OdemeServisRequest;
import tr.gov.gib.fpos.object.response.BankaServerResponse;
import tr.gov.gib.fpos.object.response.OdemeServisResponse;
import tr.gov.gib.fpos.service.OdemeResponseService;
import tr.gov.gib.gibcore.object.enums.FposSposNakitDurum;
import java.util.function.BiFunction;

@Service
public class OdemeResponseServiceImpl implements OdemeResponseService {

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
}