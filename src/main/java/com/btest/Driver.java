package com.btest;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Driver {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String address;
    private Boolean readyForRide;

    // avoid this "No default constructor for entity"
    public Driver() {
    }

    public Driver(Long id, String name, String address, Boolean readyForRide) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.readyForRide = readyForRide;
    }

    public Driver(String name, String address, Boolean readyForRide) {
        this.name = name;
        this.address = address;
        this.readyForRide = readyForRide;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Boolean getReadyForRide() {
		return readyForRide;
	}

	public void setReadyForRide(Boolean readyForRide) {
		this.readyForRide = readyForRide;
	}

	@Override
    public String toString() {
        return "Driver{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", readyForRide=" + readyForRide +
                '}';
    }

}
