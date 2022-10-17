package com.btest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import com.btest.error.DriverNotFoundException;
import com.btest.error.DriverUnSupportedFieldPatchException;

import java.util.List;
import java.util.Map;

@RestController
public class DriverController {

    @Autowired
    private DriverRepository repository;

    // Find
    @GetMapping("/drivers")
    List<Driver> findAll() {
        return repository.findAll();
    }

    // Save
    @PostMapping("/drivers")
    //return 201 instead of 200
    @ResponseStatus(HttpStatus.CREATED)
    Driver newDriver(@RequestBody Driver newDriver) {
        return repository.save(newDriver);
    }

    // Find
    @GetMapping("/drivers/{id}")
    Driver findOne(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new DriverNotFoundException(id));
    }

    // Save or update
    @PutMapping("/drivers/{id}")
    Driver saveOrUpdate(@RequestBody Driver newDriver, @PathVariable Long id) {

        return repository.findById(id)
                .map(x -> {
                    x.setName(newDriver.getName());
                    x.setAddress(newDriver.getAddress());
                    x.setReadyForRide(newDriver.getReadyForRide());
                    return repository.save(x);
                })
                .orElseGet(() -> {
                    newDriver.setId(id);
                    return repository.save(newDriver);
                });
    }

    // update readyForRide only
    @PatchMapping("/drivers/{id}")
    Driver patch(@RequestBody Map<String, Object> update, @PathVariable Long id) {

        return repository.findById(id)
                .map(x -> {

                    Boolean readyForRide = (Boolean) update.get("readyForRide");
                    if (readyForRide != null) {
                        x.setReadyForRide(readyForRide);

                        // better create a custom method to update a value = :newValue where id = :id
                        return repository.save(x);
                    } else {
                        throw new DriverUnSupportedFieldPatchException(update.keySet());
                    }

                })
                .orElseGet(() -> {
                    throw new DriverNotFoundException(id);
                });

    }

    @DeleteMapping("/drivers/{id}")
    void deleteDriver(@PathVariable Long id) {
        repository.deleteById(id);
    }

}
