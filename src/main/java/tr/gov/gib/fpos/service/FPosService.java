package tr.gov.gib.fpos.service;
import org.springframework.stereotype.Service;

import tr.gov.gib.fpos.object.request.OdemeServisRequest;
import tr.gov.gib.fpos.object.response.OdemeServisResponse;
import tr.gov.gib.gibcore.object.response.GibResponse;
import tr.gov.gib.gibcore.object.request.GibRequest;

@Service
public interface FPosService {

    GibResponse<OdemeServisResponse> processOdemeServisRequest(GibRequest<OdemeServisRequest> request);
}