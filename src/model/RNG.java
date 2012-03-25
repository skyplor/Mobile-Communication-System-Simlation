/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Random;

/**
 *
 * @author Sky
 */
public class RNG
{
    private double R1;
    private double R2;
    private Random rand;
    private boolean flag = true;

    public RNG(long seed) {
        this.rand = new Random(seed);
    }

    /**
     * Generates a uniform random number on the interval[0,1).
     * @return Uniformly distributed random number on the interval [0,1).
     */
    public synchronized double nextRand() {
        return rand.nextDouble();
    }

    /**
     * Generates the next event time which is exponentially distributed
     * @return The next event time.
     */
    public synchronized double nextExp(double mean) {
        double result = -mean * Math.log(nextRand());
        return result;
    }

    /**
     * Generated the next uniformly distributed event time on [0, b)
     * @param b Maximum range value
     * @return The next uniformly distributed value between 0 and b
     */
    public synchronized double nextUniform(double b) {
        double result = b * nextRand();
        return result;
    }

    /**
     * Generates a normally distributed random value with mean m and standard deviation stdev.
     * @param m The mean value
     * @param stdev The standard deviation
     * @return Normally distributed random number
     */
    public synchronized double nextNormal(double m, double stdev) {
        double result = 0;
        double Z = 0;
        if (flag) {
            R1 = nextRand();
            R2 = nextRand();
            Z = Math.sqrt(-2 * Math.log(R1)) * Math.cos(2 * Math.PI * R2);
        } else {
            Z = Math.sqrt(-2 * Math.log(R1)) * Math.sin(2 * Math.PI * R2);
        }
        flag = !flag;
        result = m + stdev * Z;
        return result;
    }
}
