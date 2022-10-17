package com.btest;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

//@WebMvcTest(DriverController.class)
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class DriverControllerTest {

    private static final ObjectMapper om = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DriverRepository mockRepository;

    @Before
    public void init() {
        Driver driver = new Driver(1L, "Boscoa", "Addressa1", Boolean.TRUE);
        when(mockRepository.findById(1L)).thenReturn(Optional.of(driver));
    }

    @Test
    public void find_driverId_OK() throws Exception {

        mockMvc.perform(get("/drivers/1"))
                /*.andDo(print())*/
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Boscoa")))
                .andExpect(jsonPath("$.address", is("Addressa1")))
                .andExpect(jsonPath("$.readyForRide", is(Boolean.TRUE)));

        verify(mockRepository, times(1)).findById(1L);

    }

    @Test
    public void find_alldriver_OK() throws Exception {

        List<Driver> drivers = Arrays.asList(
                new Driver(1L, "Driver A", "Address A", Boolean.TRUE),
                new Driver(2L, "Driver B", "Address B", Boolean.FALSE));

        when(mockRepository.findAll()).thenReturn(drivers);

        mockMvc.perform(get("/drivers"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Driver A")))
                .andExpect(jsonPath("$[0].address", is("Address A")))
                .andExpect(jsonPath("$[0].readyForRide", is(Boolean.TRUE)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Driver B")))
                .andExpect(jsonPath("$[1].address", is("Address B")))
                .andExpect(jsonPath("$[1].readyForRide", is(Boolean.FALSE)));

        verify(mockRepository, times(1)).findAll();
    }

    @Test
    public void find_driverIdNotFound_404() throws Exception {
        mockMvc.perform(get("/drivers/5")).andExpect(status().isNotFound());
    }

    @Test
    public void save_driver_OK() throws Exception {

        Driver newDriver = new Driver(1L, "Driver A", "Address A", Boolean.TRUE);
        when(mockRepository.save(any(Driver.class))).thenReturn(newDriver);

        mockMvc.perform(post("/drivers")
                .content(om.writeValueAsString(newDriver))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                /*.andDo(print())*/
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Driver A")))
                .andExpect(jsonPath("$.address", is("Address A")))
                .andExpect(jsonPath("$.readyForRide", is(Boolean.TRUE)));

        verify(mockRepository, times(1)).save(any(Driver.class));

    }

    @Test
    public void update_driver_OK() throws Exception {

        Driver updateDriver = new Driver(1L, "Driver B", "Address B", Boolean.TRUE);
        when(mockRepository.save(any(Driver.class))).thenReturn(updateDriver);

        mockMvc.perform(put("/drivers/1")
                .content(om.writeValueAsString(updateDriver))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Driver B")))
                .andExpect(jsonPath("$.address", is("Address B")))
                .andExpect(jsonPath("$.readyForRide", is(Boolean.TRUE)));


    }

    @Test
    public void patch_driverReadyForRide_OK() throws Exception {

        when(mockRepository.save(any(Driver.class))).thenReturn(new Driver());
        String patchInJson = "{\"readyForRide\":true}";

        mockMvc.perform(patch("/drivers/1")
                .content(patchInJson)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        verify(mockRepository, times(1)).findById(1L);
        verify(mockRepository, times(1)).save(any(Driver.class));

    }

    @Test
    public void patch_driverAddress_AddreessA() throws Exception {

        String patchInJson = "{\"address\":\"Addreess A\"}";

        mockMvc.perform(patch("/drivers/1")
                .content(patchInJson)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed());

        verify(mockRepository, times(1)).findById(1L);
        verify(mockRepository, times(0)).save(any(Driver.class));
    }

    @Test
    public void delete_driver_OK() throws Exception {

        doNothing().when(mockRepository).deleteById(1L);

        mockMvc.perform(delete("/drivers/1"))
                /*.andDo(print())*/
                .andExpect(status().isOk());

        verify(mockRepository, times(1)).deleteById(1L);
    }

    private static void printJSON(Object object) {
        String result;
        try {
            result = om.writerWithDefaultPrettyPrinter().writeValueAsString(object);
            System.out.println(result);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}
