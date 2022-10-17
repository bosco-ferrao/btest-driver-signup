package com.btest;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // for restTemplate
@ActiveProfiles("test")
public class DriverControllerRestTemplateTest {

    private static final ObjectMapper om = new ObjectMapper();

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private DriverRepository mockRepository;

    @Before
    public void init() {
        Driver driver = new Driver(1L, "Driver Name", "Address", Boolean.TRUE);
        when(mockRepository.findById(1L)).thenReturn(Optional.of(driver));
    }

    @Test
    public void find_driverId_OK() throws JSONException {

        String expected = "{id:1,name:\"Driver Name\",address:\"Address\",readyForRide:true}";

        ResponseEntity<String> response = restTemplate.getForEntity("/drivers/1", String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON_UTF8, response.getHeaders().getContentType());

        JSONAssert.assertEquals(expected, response.getBody(), false);

        verify(mockRepository, times(1)).findById(1L);

    }

    @Test
    public void find_alldriver_OK() throws Exception {

        List<Driver> drivers = Arrays.asList(
                new Driver(1L, "Driver A", "Address1", Boolean.TRUE),
                new Driver(2L, "Driver B", "Address2", Boolean.TRUE));

        when(mockRepository.findAll()).thenReturn(drivers);

        String expected = om.writeValueAsString(drivers);

        ResponseEntity<String> response = restTemplate.getForEntity("/drivers", String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        JSONAssert.assertEquals(expected, response.getBody(), false);

        verify(mockRepository, times(1)).findAll();
    }

    @Test
    public void find_driverIdNotFound_404() throws Exception {

        String expected = "{status:404,error:\"Not Found\",message:\"Driver id not found : 5\",path:\"/drivers/5\"}";

        ResponseEntity<String> response = restTemplate.getForEntity("/drivers/5", String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        JSONAssert.assertEquals(expected, response.getBody(), false);

    }

    @Test
    public void save_driver_OK() throws Exception {

        Driver newDriver = new Driver(1L, "Driver A", "Address A", Boolean.TRUE);
        when(mockRepository.save(any(Driver.class))).thenReturn(newDriver);

        String expected = om.writeValueAsString(newDriver);

        ResponseEntity<String> response = restTemplate.postForEntity("/drivers", newDriver, String.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        JSONAssert.assertEquals(expected, response.getBody(), false);

        verify(mockRepository, times(1)).save(any(Driver.class));

    }

    @Test
    public void update_driver_OK() throws Exception {

        Driver updateDriver = new Driver(1L, "ABC", "btest1", Boolean.TRUE);
        when(mockRepository.save(any(Driver.class))).thenReturn(updateDriver);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(om.writeValueAsString(updateDriver), headers);

        ResponseEntity<String> response = restTemplate.exchange("/drivers/1", HttpMethod.PUT, entity, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        JSONAssert.assertEquals(om.writeValueAsString(updateDriver), response.getBody(), false);

        verify(mockRepository, times(1)).findById(1L);
        verify(mockRepository, times(1)).save(any(Driver.class));

    }

    @Test
    public void patch_driverAddress_OK() {

        when(mockRepository.save(any(Driver.class))).thenReturn(new Driver());
        String patchInJson = "{\"address\":\"Address A\"}";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(patchInJson, headers);

        ResponseEntity<String> response = restTemplate.exchange("/drivers/1", HttpMethod.PUT, entity, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(mockRepository, times(1)).findById(1L);
        verify(mockRepository, times(1)).save(any(Driver.class));

    }

    @Test
    public void patch_driverAddress_MethodNotAllowed() throws JSONException {

        String expected = "{status:405,error:\"Method Not Allowed\",message:\"Field [address] update is not allow.\"}";

        String patchInJson = "{\"address\":\"Address C\"}";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(patchInJson, headers);

        ResponseEntity<String> response = restTemplate.exchange("/drivers/1", HttpMethod.PATCH, entity, String.class);

        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, response.getStatusCode());
        JSONAssert.assertEquals(expected, response.getBody(), false);

        verify(mockRepository, times(1)).findById(1L);
        verify(mockRepository, times(0)).save(any(Driver.class));
    }

    @Test
    public void delete_driver_OK() {

        doNothing().when(mockRepository).deleteById(1L);

        HttpEntity<String> entity = new HttpEntity<>(null, new HttpHeaders());
        ResponseEntity<String> response = restTemplate.exchange("/drivers/1", HttpMethod.DELETE, entity, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

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
