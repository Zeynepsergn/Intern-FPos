package tr.gov.gib.fpos.service.impl;

import org.springframework.stereotype.Service;
import tr.gov.gib.fpos.entity.FizikselPos;
import tr.gov.gib.fpos.object.request.OdemeServisRequest;
import tr.gov.gib.fpos.object.response.BankaServerResponse;
import tr.gov.gib.fpos.repository.FPosRepository;
import tr.gov.gib.fpos.service.TransactionService;

import java.util.Date;
import java.util.function.BiFunction;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final FPosRepository fPosRepository;

    public TransactionServiceImpl(FPosRepository fPosRepository) {
        this.fPosRepository = fPosRepository;
    }

    @Override
    public BiFunction<OdemeServisRequest, BankaServerResponse, FizikselPos> createFizikselPosData() {
        return (odemeRequest, bankaResponse) -> {
            FizikselPos fizikselPos = new FizikselPos();
            fizikselPos.setOid(odemeRequest.getOid());
            fizikselPos.setOdemeId(odemeRequest.getOdemeOid());
            fizikselPos.setKartSahibi(odemeRequest.getKartSahibi());
            fizikselPos.setKartBanka(bankaResponse.getBankaAdi());
            fizikselPos.setPosIslemId(bankaResponse.getPosId().toString());
            fizikselPos.setOptime(new Date());
            fizikselPos.setDurum((short) 0);
            fPosRepository.save(fizikselPos);
            return fizikselPos;
        };
    }
}