package tr.gov.gib.fpos.service;

import tr.gov.gib.fpos.object.request.BankaServerRequest;
import tr.gov.gib.fpos.object.response.BankaServerResponse;

public interface BankaClient {
    BankaServerResponse sendToBankEndpoint(BankaServerRequest request);
}