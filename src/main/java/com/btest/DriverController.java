package com.btest;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DriverController {

    @Autowired
    private DriverService driverService;

    // Find
    @GetMapping("/drivers")
    List<Driver> findAll() {
        return driverService.findAll();
    }

    // Save
    @PostMapping("/drivers")
    //return 201 instead of 200
    @ResponseStatus(HttpStatus.CREATED)
    Driver newDriver(@RequestBody Driver newDriver) {
        return driverService.newDriver(newDriver);
    }

    // Find
    @GetMapping("/drivers/{id}")
    Driver findOne(@PathVariable Long id) {
        return driverService.findOne(id);
    }

    // Save or update
    @PutMapping("/drivers/{id}")
    Driver saveOrUpdate(@RequestBody Driver newDriver, @PathVariable Long id) {
        return driverService.saveOrUpdate(newDriver, id);
    }

    // update readyForRide only
    @PatchMapping("/drivers/{id}")
    Driver patch(@RequestBody Map<String, Object> update, @PathVariable Long id) {
        return driverService.patch(update, id);

    }

    @DeleteMapping("/drivers/{id}")
    void deleteDriver(@PathVariable Long id) {
    	driverService.deleteById(id);
    }

}
