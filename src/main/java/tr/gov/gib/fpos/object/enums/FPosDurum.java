package tr.gov.gib.fpos.object.enums;

public enum FPosDurum {
    HATA_OLUSTU("Hata oluştu", (short) 0),
    BASARISIZ_ODEME("Ödeme Başarısız", (short) 1),
    BASARILI_ODEME("Ödeme Başarılı", (short) 2),
    ODEME_IPTAL_EDILDI("Ödeme İşlemi İptal Edildi", (short) 3);

    private String fPosDurumu;
    private Short fPosDurumKodu;

    FPosDurum(String fPosDurumu, Short fPosDurumKodu) {
        this.fPosDurumu = fPosDurumu;
        this.fPosDurumKodu = fPosDurumKodu;
    }

    public Short getfPosDurumKodu() {
        return fPosDurumKodu;
    }

    public String getfPosDurumu() {
        return fPosDurumu;
    }
}

