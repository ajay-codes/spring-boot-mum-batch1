package com.example;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "girls")
public class Girl {

    @Id
    private Long id;
    private String name;

    @OneToOne(mappedBy = "girlfriend")
    private Boy boyfriend;

}
