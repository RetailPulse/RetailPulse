package com.retailpulse.controller;

import com.retailpulse.entity.BusinessEntity;
import com.retailpulse.service.BusinessEntityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class BusinessEntityControllerTest {

    @Mock
    private BusinessEntityService businessEntityService;

    @InjectMocks
    private BusinessEntityController businessEntityController;
    
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(businessEntityController).build();
    }

    @Test
    void testGetAllBusinessEntities() throws Exception {
        BusinessEntity businessEntity1 = new BusinessEntity();
        businessEntity1.setId(1L);
        businessEntity1.setName("BusinessEntity 1");
        businessEntity1.setLocation("Location 1");
        businessEntity1.setType("Type 1");

        BusinessEntity businessEntity2 = new BusinessEntity();
        businessEntity2.setId(2L);
        businessEntity2.setName("BusinessEntity 2");
        businessEntity2.setLocation("Location 2");
        businessEntity2.setType("Type 2");

        List<BusinessEntity> businessEntities = Arrays.asList(businessEntity1, businessEntity2);
        when(businessEntityService.getAllBusinessEntities()).thenReturn(businessEntities);

        mockMvc.perform(get("/api/businessEntity"))
              .andExpect(status().isOk())
              .andExpect(content().json("[{'id': 1, 'name': 'BusinessEntity 1', 'location': 'Location 1', 'type': 'Type 1'}, {'id': 2, 'name': 'BusinessEntity 2', 'location': 'Location 2', 'type': 'Type 2'}]"));

        verify(businessEntityService, times(1)).getAllBusinessEntities();
    }

    @Test
    void testGetBusinessEntityById() throws Exception {
        BusinessEntity businessEntity = new BusinessEntity();
        businessEntity.setId(1L);
        businessEntity.setName("BusinessEntity 1");
        businessEntity.setLocation("Location 1");
        businessEntity.setType("Type 1");

        when(businessEntityService.getBusinessEntityById(1L)).thenReturn(Optional.of(businessEntity));

        mockMvc.perform(get("/api/businessEntity/1"))
               .andExpect(status().isOk())
               .andExpect(content().json("{'id': 1, 'name': 'BusinessEntity 1', 'location': 'Location 1', 'type': 'Type 1'}"));

        verify(businessEntityService, times(1)).getBusinessEntityById(1L);
    }

    @Test
    void testCreateBusinessEntity() throws Exception {
        BusinessEntity businessEntity = new BusinessEntity();
        businessEntity.setId(1L);
        businessEntity.setName("BusinessEntity 1");
        businessEntity.setLocation("Location 1");
        businessEntity.setType("Type 1");

        when(businessEntityService.saveBusinessEntity(any(BusinessEntity.class))).thenReturn(businessEntity);

        mockMvc.perform(post("/api/businessEntity")
              .contentType(MediaType.APPLICATION_JSON)
              .content("{\"name\": \"BusinessEntity 1\", \"location\": \"Location 1\", \"type\": \"Type 1\"}"))
              .andExpect(status().isOk())
              .andExpect(content().json("{'id': 1, 'name': 'BusinessEntity 1', 'location': 'Location 1', 'type': 'Type 1'}"));

        verify(businessEntityService, times(1)).saveBusinessEntity(any(BusinessEntity.class));
    }

    @Test
    void testUpdateBusinessEntity() throws Exception {
        BusinessEntity businessEntity = new BusinessEntity();
        businessEntity.setId(1L);
        businessEntity.setName("Updated BusinessEntity");
        businessEntity.setLocation("Updated Location");
        businessEntity.setType("Updated Type");

        when(businessEntityService.updateBusinessEntity(anyLong(), any(BusinessEntity.class))).thenReturn(businessEntity);

        mockMvc.perform(put("/api/businessEntity/1")
              .contentType(MediaType.APPLICATION_JSON)
              .content("{\"name\": \"Updated BusinessEntity\", \"location\": \"Updated Location\", \"type\": \"Updated Type\"}"))
              .andExpect(status().isOk())
              .andExpect(content().json("{'id': 1, 'name': 'Updated BusinessEntity', 'location': 'Updated Location', 'type': 'Updated Type'}"));

        verify(businessEntityService, times(1)).updateBusinessEntity(anyLong(), any(BusinessEntity.class));
    }

    @Test
    void testDeleteBusinessEntity() throws Exception {
        mockMvc.perform(delete("/api/businessEntity/1"))
              .andExpect(status().isOk());

        verify(businessEntityService, times(1)).deleteBusinessEntity(1L);
    }
}
