package com.project.domain.concretes.business.process;


import com.project.domain.concretes.business.Order;
import com.project.domain.concretes.business.abstracts.BaseOperation;
import com.project.domain.concretes.business.process.blokliftmontajamiri.BlokLiftMontaj;
import com.project.domain.concretes.business.process.boyavepaket.BoyaVePaketleme;
import com.project.domain.concretes.business.process.kalitekontrol.KaliteKontrol;
import com.project.domain.concretes.business.process.liftmontajamiri.LiftMontaj;
import com.project.domain.concretes.business.process.polisajamiri.PolisajImalat;
import com.project.domain.concretes.business.process.talasliimalatamiri.TalasliImalat;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "prodiction_process")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductionProcess extends BaseOperation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @OneToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Order order;//sipariş

    @OneToMany(mappedBy = "productionProcess", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TalasliImalat> talasliOperations = new ArrayList<>();//talaslı işlemler

    @OneToMany(mappedBy = "productionProcess", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LiftMontaj> liftOperations = new ArrayList<>();//lift montaj işlemleri

    @OneToMany(mappedBy = "productionProcess", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<KaliteKontrol> kaliteKontrolOperations = new ArrayList<>();//kalite kontrol işlemleri

    @OneToMany(mappedBy = "productionProcess", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoyaVePaketleme> boyaPaketOperations = new ArrayList<>();//boya ve paketleme işlemleri

    @OneToMany(mappedBy = "productionProcess", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BlokLiftMontaj> blokLiftOperations = new ArrayList<>();//blok lift montaj işlemleri

    @OneToOne(mappedBy = "productionProcess", cascade = CascadeType.ALL, orphanRemoval = true)
    private PolisajImalat polisajOperation;//polisaj işlemi
}
