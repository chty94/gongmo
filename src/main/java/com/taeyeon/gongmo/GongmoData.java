package com.taeyeon.gongmo;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GongmoData {

    @Id @GeneratedValue
    @Column(name = "gongmo_id")
    private Long id;
    private String name;
    private String date;
    private String finalPrice;
    private String hopePrice;
    private String competitionRate;
    private String securites;

    @Builder
    public GongmoData(String name, String date, String finalPrice, String hopePrice, String competitionRate, String securites) {
        this.name = name;
        this.date = date;
        this.finalPrice = finalPrice;
        this.hopePrice = hopePrice;
        this.competitionRate = competitionRate;
        this.securites = securites;
    }

    @OneToOne
    @JoinColumn(name = "predict_id")
    private GongmoDataPredict gongmoDataPredict;
}
