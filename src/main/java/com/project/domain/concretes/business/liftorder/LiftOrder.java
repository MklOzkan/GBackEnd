package com.project.domain.concretes.business.liftorder;


import com.project.domain.concretes.business.Order;
import com.project.domain.concretes.business.abstracts.BaseOperation;
import com.project.domain.concretes.business.liftorder.boyavepaket.BoyaVePaketleme;
import com.project.domain.concretes.business.liftorder.kalitekontrol.KaliteKontrol;
import com.project.domain.concretes.business.liftorder.liftmontajamiri.LiftMontaj;
import com.project.domain.concretes.business.liftorder.polisajamiri.PolisajImalat;
import com.project.domain.concretes.business.liftorder.talasliimalatamiri.TalasliImalat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "lift_orders")
@NoArgsConstructor
public class LiftOrder extends BaseOperation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Order order;

    @OneToMany(mappedBy = "liftOrder", cascade = CascadeType.ALL)
    private List<TalasliImalat> talasliImalatOperations;

    @OneToMany(mappedBy = "liftOrder", cascade = CascadeType.ALL)
    private List<PolisajImalat> polisajImalatOperations;

    @OneToMany(mappedBy = "liftOrder", cascade = CascadeType.ALL)
    private List<LiftMontaj> liftMontajOperations;

    @OneToMany(mappedBy = "liftOrder", cascade = CascadeType.ALL)
    private List<BoyaVePaketleme> boyaVePaketlemeOperations;

    @OneToMany(mappedBy = "liftOrder", cascade = CascadeType.ALL)
    private List<KaliteKontrol> kaliteKontrolOperations;




}
