package tr.gov.gib.fpos.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tr.gov.gib.fpos.object.request.OdemeServisRequest;
import tr.gov.gib.fpos.object.response.OdemeServisResponse;
import tr.gov.gib.fpos.service.FPosService;
import tr.gov.gib.gibcore.object.response.GibResponse;
import tr.gov.gib.gibcore.object.request.GibRequest;

@RestController
@RequestMapping("/fpos-server")
public class FPosController {

    private final FPosService fPosService;
    private static final Logger logger = LoggerFactory.getLogger(FPosController.class);

    public FPosController(FPosService fPosService) {
        this.fPosService = fPosService;
    }

    @PostMapping("/odeme_servis")
    public ResponseEntity<GibResponse<OdemeServisResponse>> handleOdemeServisRequest(@RequestBody GibRequest<OdemeServisRequest> request) {
        logger.info("Received GibRequest data: {}", request.getData());
        GibResponse<OdemeServisResponse> gibResponse = fPosService.processOdemeServisRequest(request);
        return new ResponseEntity<>(gibResponse, HttpStatus.OK);
    }
}
