package com.btest;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.btest.error.DriverNotFoundException;
import com.btest.error.DriverUnSupportedFieldPatchException;


@Service
public class DriverService {

    @Autowired
    private DriverRepository repository;

    // Find
    public List<Driver> findAll() {
        return repository.findAll();
    }

    // Save
    public Driver newDriver(Driver newDriver) {
        return repository.save(newDriver);
    }

    // Find
    public Driver findOne(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new DriverNotFoundException(id));
    }

    // Save or update
    Driver saveOrUpdate(Driver newDriver, Long id) {

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
    Driver patch(Map<String, Object> update, Long id) {

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
    
    void deleteById(Long id) {
        repository.deleteById(id);
    }
}
