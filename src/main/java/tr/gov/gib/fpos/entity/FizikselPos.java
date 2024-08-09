package tr.gov.gib.fpos.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "fiziksel_pos", schema = "gsths")
public class FizikselPos {
    @Id
    @Size(max = 10)
    @SequenceGenerator(name = "fiziksel_pos_id_gen", sequenceName = "odeme_odeme_id_seq", allocationSize = 1)
    @Column(name = "oid", nullable = false, length = 10)
    private String oid;

    @Column(name = "odeme_id")
    private String odeme;

    @Size(max = 100)
    @Column(name = "kart_sahibi", length = 100)
    private String kartSahibi;

    @Size(max = 100)
    @Column(name = "kart_banka", length = 100)
    private String kartBanka;

    @Size(max = 10)
    @Column(name = "pos_islem_id", length = 10)
    private String posIslemId;

    @Column(name = "optime")
    private OffsetDateTime optime;

    @Column(name = "durum")
    private Short durum;

}