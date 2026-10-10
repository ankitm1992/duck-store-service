package com.duckstore.warehouse.controller;

import java.util.List;

import com.duckstore.warehouse.model.UpsertResult;
import com.duckstore.warehouse.model.WarehouseRequest;
import com.duckstore.warehouse.model.WarehouseResponse;
import com.duckstore.warehouse.model.WarehouseUpdateRequest;
import com.duckstore.warehouse.service.WarehouseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/warehouse")
public class WarehouseController {

    private final WarehouseService service;

    public WarehouseController(WarehouseService service) {
        this.service = service;
    }


    @GetMapping("/listDucks")
    public ResponseEntity<List<WarehouseResponse>> list(@RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "20") int size,
                                                        @RequestParam(defaultValue = "id") String sortBy,
                                                        @RequestParam(defaultValue = "asc") String sortDir) {
        List<WarehouseResponse> response = service.listDucks(page, size, sortBy, sortDir);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/addDucks")
    public ResponseEntity<WarehouseResponse> add(@Valid @RequestBody WarehouseRequest request) {
        UpsertResult result = service.addDucks(request);
        return ResponseEntity.status(result.created() ? HttpStatus.CREATED : HttpStatus.OK)
                .body(WarehouseResponse.from(result));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<WarehouseResponse> update(@PathVariable Integer id, @Valid @RequestBody WarehouseUpdateRequest request) {
        WarehouseResponse response = WarehouseResponse.from(service.update(id, request));
        return ResponseEntity.ok(response);

    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }
}
