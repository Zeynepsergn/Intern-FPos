package tr.gov.gib.fpos.service;

import tr.gov.gib.fpos.entity.FizikselPos;
import tr.gov.gib.fpos.object.request.OdemeServisRequest;
import tr.gov.gib.fpos.object.response.BankaServerResponse;
import tr.gov.gib.fpos.object.response.OdemeServisResponse;

import java.util.function.BiFunction;

public interface OdemeServisResponseService {
    BiFunction<OdemeServisRequest, BankaServerResponse, OdemeServisResponse> createOdemeServisResponse();
    BiFunction<OdemeServisRequest, BankaServerResponse, FizikselPos> createFizikselPos();
}