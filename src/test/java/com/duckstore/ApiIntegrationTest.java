package com.duckstore;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.duckstore.warehouse.repository.WarehouseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@Import(TestcontainersConfig.class)
@AutoConfigureMockMvc
class ApiIntegrationTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    WarehouseRepository repository;

    @BeforeEach
    void clean() {
        repository.deleteAll();
    }

    private void addDuck(String color, String size, String price, int qty) throws Exception {
        mvc.perform(post("/api/warehouse/addDucks").contentType(MediaType.APPLICATION_JSON)
                .content("{\"color\":\"%s\",\"size\":\"%s\",\"price\":%s,\"quantity\":%d}".formatted(color, size, price, qty)))
                .andExpect(status().is2xxSuccessful());
    }

    private String order(String color, String size, int qty, String country, String mode) {
        return "{\"color\":\"%s\",\"size\":\"%s\",\"quantity\":%d,\"country\":\"%s\",\"shippingMode\":\"%s\"}"
                .formatted(color, size, qty, country, mode);
    }

    @Test
    void createReturns201ThenMergeReturns200() throws Exception {
        String body = "{\"color\":\"Red\",\"size\":\"XLarge\",\"price\":200,\"quantity\":10}";
        mvc.perform(post("/api/warehouse/addDucks").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.quantity").value(10));
        mvc.perform(post("/api/warehouse/addDucks").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk()).andExpect(jsonPath("$.quantity").value(20));
    }

    @Test
    void invalidDuckIsRejectedWith400() throws Exception {
        mvc.perform(post("/api/warehouse/addDucks").contentType(MediaType.APPLICATION_JSON)
                .content("{\"color\":\"Purple\",\"size\":\"Large\",\"price\":1,\"quantity\":1}"))
                .andExpect(status().isBadRequest());
        mvc.perform(post("/api/warehouse/addDucks").contentType(MediaType.APPLICATION_JSON)
                .content("{\"color\":\"Red\",\"size\":\"Large\",\"price\":-1,\"quantity\":0}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details.price").exists())
                .andExpect(jsonPath("$.details.quantity").exists());
    }

    @Test
    void deleteHidesDuckAndSecondDeleteIs404() throws Exception {
        addDuck("Red", "Large", "5.00", 1);
        long id = repository.findAll().get(0).getId();
        mvc.perform(delete("/api/warehouse/delete/" + id)).andExpect(status().isNoContent());
        mvc.perform(get("/api/warehouse/listDucks")).andExpect(jsonPath("$").isEmpty());
        mvc.perform(delete("/api/warehouse/delete/" + id)).andExpect(status().isNotFound());
    }

    @Test
    void orderReturnsPackagingAndItemizedPrice() throws Exception {
        addDuck("Yellow", "Medium", "5.00", 50);
        mvc.perform(post("/api/orders").contentType(MediaType.APPLICATION_JSON)
                .content(order("Yellow", "Medium", 10, "USA", "Air")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.packageType").value("Cardboard"))
                .andExpect(jsonPath("$.protections[0]").value("Polystyrene balls"))
                .andExpect(jsonPath("$.totalToPay").value(358.5))
                .andExpect(jsonPath("$.breakdown.length()").value(4));
    }

    @Test
    void orderUsesCheapestLotThatCoversQuantity() throws Exception {
        addDuck("Green", "Small", "9.00", 100);
        addDuck("Green", "Small", "3.00", 5);   // cheaper but too small for the order
        addDuck("Green", "Small", "4.00", 100);
        mvc.perform(post("/api/orders").contentType(MediaType.APPLICATION_JSON)
                .content(order("Green", "Small", 10, "Spain", "Sea")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.unitPrice").value(4.0));
    }

    @Test
    void orderErrors() throws Exception {
        mvc.perform(post("/api/orders").contentType(MediaType.APPLICATION_JSON)
                .content(order("Red", "Large", 1, "USA", "Sea"))).andExpect(status().isNotFound());
        addDuck("Red", "Large", "5.00", 2);
        mvc.perform(post("/api/orders").contentType(MediaType.APPLICATION_JSON)
                .content(order("Red", "Large", 3, "USA", "Sea"))).andExpect(status().isConflict());
        mvc.perform(post("/api/orders").contentType(MediaType.APPLICATION_JSON)
                .content(order("Red", "Large", 1, " ", "Sea"))).andExpect(status().isBadRequest());
        mvc.perform(post("/api/orders").contentType(MediaType.APPLICATION_JSON)
                .content(order("Red", "Large", 1, "USA", "Rocket"))).andExpect(status().isBadRequest());
    }
}
