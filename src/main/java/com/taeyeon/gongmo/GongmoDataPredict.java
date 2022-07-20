package com.taeyeon.gongmo;


import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@Data
@NoArgsConstructor
public class GongmoDataPredict {

    @Id @GeneratedValue
    @Column(name = "predict_id")
    private Long id;

    private String name;
    private String predictingDate;
    private String predictCompetitionRate;

    @Builder
    public GongmoDataPredict(String name, String predictingDate, String predictCompetitionRate) {
        this.name = name;
        this.predictingDate = predictingDate;
        this.predictCompetitionRate = predictCompetitionRate;
    }

    @OneToOne(mappedBy = "gongmoDataPredict")
    private GongmoData gongmoData;
}
