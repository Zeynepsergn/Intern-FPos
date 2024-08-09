package tr.gov.gib.fpos.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tr.gov.gib.fpos.object.request.FPosSorguRequest;
import tr.gov.gib.fpos.object.response.FPosResponse;
import tr.gov.gib.fpos.service.FPosService;
import tr.gov.gib.gibcore.object.response.GibResponse;
import tr.gov.gib.gibcore.object.reuest.GibRequest;

import java.util.List;

@RestController
@RequestMapping("/fpos-server")
public class FPosController {

    private final FPosService fPosService;

    public FPosController(FPosService fPosService) {
        this.fPosService = fPosService;
    }

    @PostMapping("/kart_bilgilerini_al")
    public ResponseEntity<GibResponse<FPosResponse>> sorgula(@RequestBody GibRequest<FPosSorguRequest> request) {
        GibResponse<FPosResponse> mukellefBorcs = fPosService.kartBilgileriniAl(request.getData());
        return new ResponseEntity<>(mukellefBorcs, HttpStatus.OK);
    }
}
