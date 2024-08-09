package tr.gov.gib.fpos.object.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FPosResponse {
    private String oid;
    private BigDecimal odenecekTutar;
    private String kartNo;
    private Integer ccv;
    private Integer sonKullanimTarihiAy;
    private Integer sonKullanimTarihiYil;
    private String kartSahibiAd;
    private String kartSahibiSoyad;
    private String message; // New field
    private String status;  //
}
