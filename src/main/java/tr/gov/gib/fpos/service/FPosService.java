package tr.gov.gib.fpos.service;

import tr.gov.gib.fpos.object.request.FPosKartBilgiRequest;
import tr.gov.gib.fpos.object.request.OdemeServisRequest;
import tr.gov.gib.fpos.object.response.BankaServerResponse;
import tr.gov.gib.fpos.object.response.FPosResponse;
import tr.gov.gib.fpos.object.response.OdemeServisResponse;
import tr.gov.gib.gibcore.object.response.GibResponse;
import tr.gov.gib.gibcore.object.reuest.GibRequest;

public interface FPosService {
    BankaServerResponse kartBilgileriniAl(FPosKartBilgiRequest request);

    GibResponse<FPosResponse> processOdemeServisRequest(GibRequest<OdemeServisRequest> request);
}