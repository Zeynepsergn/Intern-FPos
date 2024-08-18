package tr.gov.gib.fpos.object.request;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class BankaServerRequest {
    private String oid;
    private BigDecimal odenecekTutar;
    private String kartNo;
    private Integer ccv;
    private Integer sonKullanimTarihiAy;
    private Integer sonKullanimTarihiYil;
    private String kartSahibi;

    public BankaServerRequest(OdemeServisRequest request){
        this.oid = request.getOid();
        this.odenecekTutar = request.getOdenecekMiktar();
        this.kartNo = request.getKartNo();
        this.ccv = request.getCcv();
        this.sonKullanimTarihiAy = request.getSonKullanimTarihiAy();
        this.sonKullanimTarihiYil = request.getSonKullanimTarihiYil();
        this.kartSahibi = request.getKartSahibi();
    }
}

