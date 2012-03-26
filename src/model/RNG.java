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

    private Random rand;

    public RNG(long seed)
    {
        this.rand = new Random(seed);
    }

    /**
     * Generates a uniform random number on the interval[0,1).
     *
     * @return Uniformly distributed random number on the interval [0,1).
     */
    public synchronized double nextRand()
    {
        return rand.nextDouble();
    }

    /**
     * Generates the next event time which is exponentially distributed
     *
     * @return The next event time.
     */
    public synchronized double nextExp(double mean)
    {
        double result = -mean * Math.log(nextRand());
        return result;
    }

    /**
     * Generates the next uniformly distributed value for the position of the car
     *
     * @param b Maximum range value
     * @return The next uniformly distributed value between 0 and b
     */
    public synchronized double nextUniform(double b)
    {
        double result = b * nextRand();
        return result;
    }

    /**
     * Generates a normally distributed random value with mean m and standard deviation stdev for the speed of the car
     *
     * @param m The mean value
     * @param stdev The standard deviation
     * @return Normally distributed random number
     */
    public synchronized double nextNormal(double m, double stdev)
    {
        double result;

        result = m + stdev * rand.nextGaussian();
        
        return result;
    }
}
