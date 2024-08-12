package tr.gov.gib.fpos.object.request;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
@Data
@Builder
public class BankaServerRequest {
    //OdemeResponse içerisindeki veri geliyor aslında. Set etme kısmını yap burayı silme.
    private String oid;
    private BigDecimal odenecekTutar;
    private String kartNo;
    private Integer ccv;
    private Integer sonKullanimTarihiAy;
    private Integer sonKullanimTarihiYil;
    private String kartSahibi;
}
