package tr.gov.gib.fpos.object.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

//Ödeme servisinden fiziksel pos servisine gelecek olan istek yapısı????
import java.math.BigDecimal;
@Data
@AllArgsConstructor
@Builder
public class OdemeServisRequest {
    private String oid;
    private Integer odemeOid;
    private String kartSahibi;
    private BigDecimal odenecekMiktar;
    private String kartNo;
    private Integer ccv;
    private Integer sonKullanimTarihiAy;
    private Integer sonKullanimTarihiYil;
}

