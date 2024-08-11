package tr.gov.gib.fpos.object.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class FPosResponse {
    private String oid;
    private String status;
    private String message;
    private String bankaAdi;
    private Integer posId;
    private BigDecimal odenecekTutar;
    private String kartNo;
    private Integer ccv;
    private Integer sonKullanimTarihiAy;
    private Integer sonKullanimTarihiYil;
    private String kartSahibiAd;
    private String kartSahibiSoyad;
}