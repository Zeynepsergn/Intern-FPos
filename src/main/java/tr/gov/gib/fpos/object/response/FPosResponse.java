package tr.gov.gib.fpos.object.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class FPosResponse {
    private String oid;
    private Integer odemeOid;
    private Short durum;
    private String posIslemId;
    private String aciklama;
}