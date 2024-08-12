package tr.gov.gib.fpos.service;


import tr.gov.gib.fpos.object.request.OdemeServisRequest;
import tr.gov.gib.fpos.object.response.OdemeServisResponse;
import tr.gov.gib.gibcore.object.response.GibResponse;
import tr.gov.gib.gibcore.object.reuest.GibRequest;

public interface FPosService {

    GibResponse<OdemeServisResponse> processOdemeServisRequest(GibRequest<OdemeServisRequest> request);
}