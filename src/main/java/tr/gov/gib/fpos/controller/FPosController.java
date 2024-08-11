package tr.gov.gib.fpos.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tr.gov.gib.fpos.object.request.FPosKartBilgiRequest;
import tr.gov.gib.fpos.object.request.OdemeServisRequest;
import tr.gov.gib.fpos.object.response.BankaServerResponse;
import tr.gov.gib.fpos.object.response.FPosResponse;
import tr.gov.gib.fpos.object.response.OdemeServisResponse;
import tr.gov.gib.fpos.service.FPosService;
import tr.gov.gib.gibcore.object.response.GibResponse;
import tr.gov.gib.gibcore.object.reuest.GibRequest;

@RestController
@RequestMapping("/fpos-server")
public class FPosController {

    private final FPosService fPosService;
    private static final Logger logger = LoggerFactory.getLogger(FPosController.class);

    public FPosController(FPosService fPosService) {
        this.fPosService = fPosService;
    }

    @PostMapping("/kart_bilgilerini_al")
    public ResponseEntity<BankaServerResponse> kartBilgileriniAl(@RequestBody FPosKartBilgiRequest request) {
        BankaServerResponse response = fPosService.kartBilgileriniAl(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PostMapping("/odeme_servis")
    public ResponseEntity<GibResponse<FPosResponse>> handleOdemeServisRequest(@RequestBody GibRequest<OdemeServisRequest> request) {
        logger.info("Received GibRequest data: {}", request.getData());
        GibResponse<FPosResponse> gibResponse = fPosService.processOdemeServisRequest(request);
        return new ResponseEntity<>(gibResponse, HttpStatus.OK);
    }
}
