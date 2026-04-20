package com.example.entity;

import java.util.Date;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    @Column(name = "customer_id")
    private int id;
    @Column(name = "customer_name")
    private String name;
    @Column(name = "customer_gender")
    @Enumerated(EnumType.ORDINAL)
    private Gender gender;
    @Column(name = "customer_birth_date")
    @Temporal(jakarta.persistence.TemporalType.DATE)
    private Date birthDate;
    @Column(name = "customer_profile")
    @Lob
    private String profile;
    @Column(name = "customer_profile_picture")
    @Lob
    private byte[] profilePicture;
    @ElementCollection
    @CollectionTable(name = "customer_addresses", joinColumns = @jakarta.persistence.JoinColumn(name = "customer_id"))
    private List<Address> addressList;

    @OneToMany(mappedBy = "customer", cascade = jakarta.persistence.CascadeType.ALL)
    private List<Order> orderList;

}
