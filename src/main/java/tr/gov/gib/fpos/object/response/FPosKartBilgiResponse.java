package tr.gov.gib.fpos.object.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FPosKartBilgiResponse {
    private String status;
}
