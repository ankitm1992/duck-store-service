package com.duckstore.warehouse.entity;

import com.duckstore.warehouse.enums.DuckColor;
import com.duckstore.warehouse.enums.DuckSize;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "ducks")
public class Duck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "color", nullable = false, length = 16)
    private DuckColor color;

    @Enumerated(EnumType.STRING)
    @Column(name = "size", nullable = false, length = 16)
    private DuckSize size;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private boolean deleted;

    protected Duck() {
    }

    public Duck(DuckColor color, DuckSize size, BigDecimal price, Integer quantity) {
        this.color = color;
        this.size = size;
        this.price = price;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DuckColor getColor() {
        return color;
    }

    public void setColor(DuckColor color) {
        this.color = color;
    }

    public DuckSize getSize() {
        return size;
    }

    public void setSize(DuckSize size) {
        this.size = size;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
