package com.ingemark.infranstructure.persistence.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "product")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 10, nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(name = "price_in_eur")
    private BigDecimal priceInEur;

    @Column(name = "price_in_usd")
    private BigDecimal priceInUsd;

    @Column(name = "is_available")
    private boolean available;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public BigDecimal getPriceInEur() { return priceInEur; }
    public void setPriceInEur(BigDecimal priceInEur) { this.priceInEur = priceInEur; }

    public BigDecimal getPriceInUsd() { return priceInUsd; }
    public void setPriceInUsd(BigDecimal priceInUsd) { this.priceInUsd = priceInUsd; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
}
