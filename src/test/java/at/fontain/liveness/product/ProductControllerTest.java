package at.fontain.liveness.product;

import at.fontain.liveness.web.GlobalExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private ProductService service;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final UUID ID = UUID.fromString("00000000-0000-0000-0000-000000000001");

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new ProductController(service))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void getAll_returns200WithProductList() throws Exception {
        List<Product> products = List.of(new Product(ID, "Product 1", 10.0));
        when(service.getAll()).thenReturn(products);

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(ID.toString()))
                .andExpect(jsonPath("$[0].name").value("Product 1"))
                .andExpect(jsonPath("$[0].price").value(10.0));
    }

    @Test
    void getById_existingId_returns200() throws Exception {
        when(service.getById(ID)).thenReturn(new Product(ID, "Product 1", 10.0));

        mockMvc.perform(get("/products/{id}", ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Product 1"));
    }

    @Test
    void getById_unknownId_returns404() throws Exception {
        when(service.getById(ID)).thenThrow(new ProductNotFoundException(ID));

        mockMvc.perform(get("/products/{id}", ID))
                .andExpect(status().isNotFound());
    }

    @Test
    void create_validRequest_returns201WithLocation() throws Exception {
        ProductRequest request = new ProductRequest("New", 5.0);
        Product created = new Product(ID, "New", 5.0);
        when(service.create(any(ProductRequest.class))).thenReturn(created);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("New"))
                .andExpect(header().string("Location", "/products/" + ID));
    }

    @Test
    void create_blankName_returns400() throws Exception {
        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ProductRequest("", 5.0))))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_negativePrice_returns400() throws Exception {
        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ProductRequest("Valid Name", -1.0))))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_existingId_returns200() throws Exception {
        ProductRequest request = new ProductRequest("Updated", 15.0);
        Product updated = new Product(ID, "Updated", 15.0);
        when(service.update(eq(ID), any(ProductRequest.class))).thenReturn(updated);

        mockMvc.perform(put("/products/{id}", ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated"));
    }

    @Test
    void update_unknownId_returns404() throws Exception {
        when(service.update(eq(ID), any(ProductRequest.class))).thenThrow(new ProductNotFoundException(ID));

        mockMvc.perform(put("/products/{id}", ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ProductRequest("X", 1.0))))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_existingId_returns204() throws Exception {
        doNothing().when(service).delete(ID);

        mockMvc.perform(delete("/products/{id}", ID))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_unknownId_returns404() throws Exception {
        doThrow(new ProductNotFoundException(ID)).when(service).delete(ID);

        mockMvc.perform(delete("/products/{id}", ID))
                .andExpect(status().isNotFound());
    }
}
