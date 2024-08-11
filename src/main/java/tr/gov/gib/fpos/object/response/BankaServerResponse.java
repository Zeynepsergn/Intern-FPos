package tr.gov.gib.fpos.object.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
@Data
@Builder
public class BankaServerResponse {
    private String bankaAdi;
    private String message;
    private String oid;
    private Integer posId;
    private String status;
}
