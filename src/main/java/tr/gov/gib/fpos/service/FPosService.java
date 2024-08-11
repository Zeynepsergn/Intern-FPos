package tr.gov.gib.fpos.service;

import tr.gov.gib.fpos.object.request.FPosKartBilgiRequest;
import tr.gov.gib.fpos.object.response.BankaServerResponse;

public interface FPosService {
    BankaServerResponse kartBilgileriniAl(FPosKartBilgiRequest request);
}