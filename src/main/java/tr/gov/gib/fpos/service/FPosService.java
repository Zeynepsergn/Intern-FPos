package tr.gov.gib.fpos.service;

import tr.gov.gib.fpos.object.request.FPosSorguRequest;
import tr.gov.gib.fpos.object.response.FPosResponse;
import tr.gov.gib.gibcore.object.response.GibResponse;
import tr.gov.gib.gibcore.object.reuest.GibRequest;

import java.util.List;

public interface FPosService {
    GibResponse<FPosResponse> kartBilgileriniAl(FPosSorguRequest request);

}
