package com.rafaelsalazar.app.controller;

import com.rafaelsalazar.app.bean.Car;
import com.rafaelsalazar.app.service.CarService;
import com.rafaelsalazar.server.annotation.*;

import java.util.List;

@RestController("/cars")
public class CarController {

    @Autowire
    private CarService carService;

    @POST
    public Car create(@Body Car car) {
        return carService.addCar(car);
    }

    @GET
    public List<Car> read() {
        return carService.getCars();
    }

    @GET("/{id}")
    public Car read(int id) {
        return carService.getCar(id);
    }

    @PUT("/{id}")
    public Car update(@Body Car car, int id) {
        return carService.editCar(car, id);
    }

    @DELETE("/{id}")
    public Car delete(int id) {
        return carService.deleteCar(id);
    }
}
