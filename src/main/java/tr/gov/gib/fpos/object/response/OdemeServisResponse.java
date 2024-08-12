package tr.gov.gib.fpos.object.response;

import lombok.Data;

@Data
public class OdemeServisResponse {
    //Odeme servisisine en son dönecek ver.
    private String oid;
    private Integer odemeOid;
    private Short durum;
    private String posId;
    private String aciklama;
    private String bankaAdi;
}

