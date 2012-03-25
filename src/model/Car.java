/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Sky
 */
public class Car
{
    private int id;
    private double time;
    private double position;
    private double duration;
    private double speed;

    public Car(int id, double callArrivalTime, double callPosition, double callDuration, double drivingSpeed) {
        this.id = id;
        this.time = callArrivalTime;
        this.position = callPosition * 1000;
        this.duration = callDuration;
        this.speed = drivingSpeed * 1000 / 3600; //keep it in meters/second

    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPosition() {
        return position;
    }

    public void setPosition(double position) {
        this.position = position;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Car{" + "id=" + id + ", time=" + time + ", position=" + position + ", duration=" + duration + ", speed=" + speed + '}';
    }
}
