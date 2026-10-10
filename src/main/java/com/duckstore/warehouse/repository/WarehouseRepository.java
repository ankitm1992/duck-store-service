package com.duckstore.warehouse.repository;

import java.math.BigDecimal;
import java.util.Optional;

import com.duckstore.warehouse.entity.Duck;
import com.duckstore.warehouse.enums.DuckColor;
import com.duckstore.warehouse.enums.DuckSize;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WarehouseRepository extends JpaRepository<Duck, Integer> {

    Page<Duck> findByDeletedFalse(Pageable pageable);

    Optional<Duck> findByIdAndDeletedFalse(Integer id);

    Optional<Duck> findByColorAndSizeAndPriceAndDeletedFalse(DuckColor color, DuckSize size, BigDecimal price);

    boolean existsByColorAndSizeAndDeletedFalse(DuckColor color, DuckSize size);

    Optional<Duck> findFirstByColorAndSizeAndDeletedFalseAndQuantityGreaterThanEqualOrderByPriceAsc(
            DuckColor color, DuckSize size, int quantity);

    @Query(value = """
            INSERT INTO ducks (color, size, price, quantity, deleted)
            VALUES (:color, :size, :price, :qty, FALSE)
            ON CONFLICT (color, size, price) WHERE deleted = FALSE
            DO UPDATE SET quantity = ducks.quantity + EXCLUDED.quantity
            RETURNING id,color,size,price,quantity,(xmax = 0) AS created
            """, nativeQuery = true)
    DuckUpsertProjection upsert(
            @Param("color") String color,
            @Param("size") String size,
            @Param("price") BigDecimal price,
            @Param("qty") Integer qty
    );

    interface DuckUpsertProjection {
        Long getId();
        String getColor();
        String getSize();
        BigDecimal getPrice();
        Integer getQuantity();
        Boolean getCreated();
    }
}
