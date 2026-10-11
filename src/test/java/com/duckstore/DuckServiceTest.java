package com.duckstore;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import com.duckstore.shared.ConflictException;
import com.duckstore.warehouse.entity.Duck;
import com.duckstore.warehouse.enums.DuckColor;
import com.duckstore.warehouse.enums.DuckSize;
import com.duckstore.warehouse.model.UpsertResult;
import com.duckstore.warehouse.model.WarehouseRequest;
import com.duckstore.warehouse.model.WarehouseResponse;
import com.duckstore.warehouse.model.WarehouseUpdateRequest;
import com.duckstore.warehouse.repository.WarehouseRepository;
import com.duckstore.warehouse.service.WarehouseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestcontainersConfig.class)
class DuckServiceTest {

    @Autowired
    WarehouseService service;
    @Autowired
    WarehouseRepository repository;

    @BeforeEach
    void clean() {
        repository.deleteAll();
    }

    private static WarehouseRequest req(DuckColor c, DuckSize s, String price, int qty) {
        return new WarehouseRequest(c, s, new BigDecimal(price), qty);
    }

    @Test
    void samePriceColorSizeMergesQuantities() {
        var first = service.addDucks(req(DuckColor.RED, DuckSize.LARGE, "10.00", 5));
        var second = service.addDucks(req(DuckColor.RED, DuckSize.LARGE, "10", 7));

        assertThat(first.created()).isTrue();
        assertThat(second.created()).isFalse();
        assertThat(second.id()).isEqualTo(first.id());
        assertThat(second.quantity()).isEqualTo(12);
        assertThat(repository.count()).isEqualTo(1);
    }

    @Test
    void differentPriceCreatesSeparateRecord() {
        service.addDucks(req(DuckColor.RED, DuckSize.LARGE, "10.00", 5));
        service.addDucks(req(DuckColor.RED, DuckSize.LARGE, "11.00", 5));
        assertThat(repository.count()).isEqualTo(2);
    }

    @Test
    void concurrentAddsOfSameDuckNeverDuplicate() throws Exception {
        int threads = 20;
        ExecutorService pool = Executors.newFixedThreadPool(threads);
        CountDownLatch start = new CountDownLatch(1);
        List<Future<?>> futures = new ArrayList<>();
        for (int i = 0; i < threads; i++) {
            futures.add(pool.submit(() -> {
                start.await();
                return service.addDucks(req(DuckColor.GREEN, DuckSize.SMALL, "2.50", 1));
            }));
        }
        start.countDown();
        for (Future<?> f : futures) {
            f.get(30, TimeUnit.SECONDS);
        }
        pool.shutdown();

        List<Duck> all = repository.findAll();
        assertThat(all).hasSize(1);
        assertThat(all.get(0).getQuantity()).isEqualTo(threads);
    }

    @Test
    void deleteIsLogicalAndHidesDuckFromListing() {
        UpsertResult duck = service.addDucks(req(DuckColor.BLACK, DuckSize.XSMALL, "1.00", 3));
        service.delete(duck.id());

        assertThat(service.listDucks(0, 10, "quantity", "DESC")).isEmpty();
        assertThat(repository.findAll()).singleElement().satisfies(d -> assertThat(d.isDeleted()).isTrue());
    }

    @Test
    void addingAfterDeleteCreatesFreshRecordInsteadOfResurrecting() {
        UpsertResult old = service.addDucks(req(DuckColor.BLACK, DuckSize.XSMALL, "1.00", 3));
        service.delete(old.id());
        var again = service.addDucks(req(DuckColor.BLACK, DuckSize.XSMALL, "1.00", 4));

        assertThat(again.created()).isTrue();
        assertThat(again.id()).isNotEqualTo(old.id());
        assertThat(again.quantity()).isEqualTo(4);
        assertThat(repository.count()).isEqualTo(2);
    }

    @Test
    void editChangesOnlyPriceAndQuantity() {
        UpsertResult duck = service.addDucks(req(DuckColor.YELLOW, DuckSize.MEDIUM, "4.00", 1));
        Duck updated = service.update(duck.id(), new WarehouseUpdateRequest(new BigDecimal("4.50"), 9));

        assertThat(updated.getColor()).isEqualTo(DuckColor.YELLOW);
        assertThat(updated.getSize()).isEqualTo(DuckSize.MEDIUM);
        assertThat(updated.getPrice()).isEqualByComparingTo("4.50");
        assertThat(updated.getQuantity()).isEqualTo(9);
    }

    @Test
    void editThatWouldDuplicateAnotherDuckIsRejected() {
        service.addDucks(req(DuckColor.RED, DuckSize.LARGE, "10.00", 1));
        UpsertResult other = service.addDucks(req(DuckColor.RED, DuckSize.LARGE, "12.00", 1));

        assertThatThrownBy(() -> service.update(other.id(), new WarehouseUpdateRequest(new BigDecimal("10.00"), 1)))
                .isInstanceOf(ConflictException.class);
    }

    @Test
    void listIsSortedByQuantity() {
        service.addDucks(req(DuckColor.RED, DuckSize.LARGE, "1.00", 5));
        service.addDucks(req(DuckColor.RED, DuckSize.SMALL, "1.00", 50));
        service.addDucks(req(DuckColor.RED, DuckSize.MEDIUM, "1.00", 20));

        assertThat(service.listDucks(0, 10, "quantity", "DESC")).extracting(WarehouseResponse::quantity).containsExactly(50, 20, 5);
        assertThat(service.listDucks(0, 10, "quantity", "ASC")).extracting(WarehouseResponse::quantity).containsExactly(5, 20, 50);
    }

    @Test
    void databaseItselfRejectsDuplicateLiveDucks() {
        service.addDucks(req(DuckColor.RED, DuckSize.LARGE, "10.00", 1));
        // bypass the merge logic: the partial unique index must still hold the invariant
        assertThatThrownBy(() -> repository.saveAndFlush(
                new Duck(DuckColor.RED, DuckSize.LARGE, new BigDecimal("10.00"), 1)))
                .isInstanceOf(org.springframework.dao.DataIntegrityViolationException.class);
    }
}
