package com.rafaelsalazar.app.service;

import com.rafaelsalazar.app.bean.Car;
import com.rafaelsalazar.server.annotation.Autowire;

import java.util.ArrayList;
import java.util.List;

public class CarService {
    private List<Car> cars = new ArrayList<>();
    private static int index = 1;

    public List<Car> getCars() {
        return cars;
    }

    public Car getCar(int id) {
        for (Car car: cars) {
            if (car.getId().equals(id)) {
                return car;
            }
        }
        return null;
    }

    public Car addCar(Car car) {
        car.setId(index);
        index++;
        cars.add(car);
        return car;
    }

    public Car editCar(Car car, int id) {
        for (int i = 0; i < cars.size(); i++) {
            if (cars.get(i).getId().equals(id)) {
                cars.get(i).setBrand(car.getBrand());
                cars.get(i).setModel(car.getModel());
                cars.get(i).setColor(car.getColor());
                return cars.get(i);
            }
        }
        return null;
    }

    public Car deleteCar(int id) {
        for (int i = 0; i < cars.size(); i++) {
            if (cars.get(i).getId().equals(id)) {
                Car car = cars.get(i);
                cars.remove(i);
                return car;
            }
        }
        return null;
    }
}
