package tr.gov.gib.fpos.service;

import tr.gov.gib.fpos.entity.FizikselPos;
import tr.gov.gib.fpos.object.request.OdemeServisRequest;
import tr.gov.gib.fpos.object.response.BankaServerResponse;

import java.util.function.BiFunction;

public interface TransactionService {
    BiFunction<OdemeServisRequest, BankaServerResponse, FizikselPos> createFizikselPosData();
}