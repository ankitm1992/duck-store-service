package com.duckstore.warehouse.model;


import java.math.BigDecimal;

public record UpsertResult (Long id, String color, String size, BigDecimal price, Integer quantity, Boolean created) {}
