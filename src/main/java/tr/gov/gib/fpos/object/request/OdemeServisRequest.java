package tr.gov.gib.fpos.object.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


//Ödeme servisinden fiziksel pos servisine gelecek olan istek yapısı????
import java.math.BigDecimal;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OdemeServisRequest {

    //Odeme Response içerisindeki veri geliyor buraya.
    private String oid;
    private Integer odemeOid;
    private String kartSahibi;
    private BigDecimal odenecekMiktar;
    private String tckn;
    private String kartNo;
    private Integer ccv;
    private Integer sonKullanimTarihiAy;
    private Integer sonKullanimTarihiYil;
}
