package com.duckstore.warehouse.service;

import java.math.BigDecimal;
import java.util.List;

import com.duckstore.warehouse.entity.Duck;
import com.duckstore.warehouse.enums.DuckColor;
import com.duckstore.warehouse.enums.DuckSize;
import com.duckstore.shared.ConflictException;
import com.duckstore.shared.NotFoundException;
import com.duckstore.warehouse.model.UpsertResult;
import com.duckstore.warehouse.model.WarehouseRequest;
import com.duckstore.warehouse.model.WarehouseResponse;
import com.duckstore.warehouse.model.WarehouseUpdateRequest;
import com.duckstore.warehouse.repository.WarehouseRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class WarehouseService {

    private final WarehouseRepository repository;

    public WarehouseService(WarehouseRepository repository) {
        this.repository = repository;
    }

    public List<WarehouseResponse> listDucks(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        List<Duck> duckList = repository.findByDeletedFalse(pageable).getContent();
        return duckList.stream().map(WarehouseResponse::from).toList();
    }

    public UpsertResult addDucks(WarehouseRequest request) {
        BigDecimal price = request.price().setScale(2);
        WarehouseRepository.DuckUpsertProjection projection =
                repository.upsert(request.color().name(), request.size().name(), price, request.quantity());
        return new UpsertResult(projection.getId(), projection.getColor(), projection.getSize(), projection.getPrice(), projection.getQuantity(), projection.getCreated());
    }

    @Transactional
    public Duck update(Integer id, WarehouseUpdateRequest request) {
        Duck duck = repository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException("Duck id: " + id + " not found"));

        duck.setPrice(request.price().setScale(2));
        duck.setQuantity(request.quantity());

        try {
            return repository.saveAndFlush(duck);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("Another active duck with color: " + duck.getColor()
                    + ", size: " + duck.getSize() + ", and price: " + duck.getPrice() + " already exists.");
        }
    }

    @Transactional
    public void delete(Integer id) {
        Duck duck = repository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException("Duck id: " + id + " not found"));
        duck.setDeleted(true);
    }

    public Duck findCheapestAvailable(DuckColor color, DuckSize size, int quantity) {
        return repository
                .findFirstByColorAndSizeAndDeletedFalseAndQuantityGreaterThanEqualOrderByPriceAsc(color, size, quantity)
                .orElseThrow(() -> repository.existsByColorAndSizeAndDeletedFalse(color, size)
                        ? new ConflictException("Insufficient stock: " + quantity
                        + " (" + color.getLabel() + ", " + size.getLabel() + ") ducks not available in the warehouse")
                        : new NotFoundException(color.getLabel() + ", " + size.getLabel() + " ducks not     available in the warehouse"));
    }
}
