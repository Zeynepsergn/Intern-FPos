package tr.gov.gib.fpos.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tr.gov.gib.fpos.object.request.FPosKartBilgiRequest;
import tr.gov.gib.fpos.object.response.BankaServerResponse;
import tr.gov.gib.fpos.service.FPosService;

@RestController
@RequestMapping("/fpos-server")
public class FPosController {

    private final FPosService fPosService;

    public FPosController(FPosService fPosService) {
        this.fPosService = fPosService;
    }

    @PostMapping("/kart_bilgilerini_al")
    public ResponseEntity<BankaServerResponse> kartBilgileriniAl(@RequestBody FPosKartBilgiRequest request) {
        BankaServerResponse response = fPosService.kartBilgileriniAl(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}