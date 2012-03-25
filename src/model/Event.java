/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Sky
 */
public class Event
{
    private Car car;

    public Event(Car car) {
        this.car = car;
    }

    public Car getCar() {
        return car;
    }

    public void setCustomer(Car car) {
        this.car = car;
    }

    public void setTime(double time) {
        car.setTime(time);
    }

    public void setSpeed(double speed) {
        car.setSpeed(speed);
    }

    public void setPosition(double position) {
        car.setPosition(position);
    }

    public void setId(int id) {
        car.setId(id);
    }

    public void setDuration(double duration) {
        car.setDuration(duration);
    }

    public double getTime() {
        return car.getTime();
    }

    public double getSpeed() {
        return car.getSpeed();
    }

    public double getPosition() {
        return car.getPosition();
    }

    public int getId() {
        return car.getId();
    }

    public double getDuration() {
        return car.getDuration();
    }
}
