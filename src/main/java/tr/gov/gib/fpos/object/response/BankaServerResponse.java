package tr.gov.gib.fpos.object.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BankaServerResponse {
    //Bunun durması lazım. Request artık Odeme Servisi üzerinden gelecek.
    private String bankaAdi;
    private String message;
    private String oid;
    private Integer posId;
    private String status;
}
