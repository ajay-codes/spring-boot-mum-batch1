package com.example;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "boys")
public class Boy {

    @Id
    private Long id;
    private String name;

    @OneToOne(fetch = jakarta.persistence.FetchType.EAGER)
    @JoinColumn(name = "girlfriend_id")
    private Girl girlfriend;

}
