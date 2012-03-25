/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation;

/**
 *
 * @author Sky
 */
public class WarmupStatistics
{

    private double time;
    private int blockedCalls;
    private int droppedCalls;

    public WarmupStatistics(double time, int blockedCalls, int droppedCalls)
    {
        this.time = time;
        this.blockedCalls = blockedCalls;
        this.droppedCalls = droppedCalls;
    }

    public int getBlockedCalls()
    {
        return blockedCalls;
    }

    public void setBlockedCalls(int blockedCalls)
    {
        this.blockedCalls = blockedCalls;
    }

    public int getDroppedCalls()
    {
        return droppedCalls;
    }

    public void setDroppedCalls(int droppedCalls)
    {
        this.droppedCalls = droppedCalls;
    }

    public double getTime()
    {
        return time;
    }

    public void setTime(double time)
    {
        this.time = time;
    }

    @Override
    public String toString()
    {
        return time + "\t" + blockedCalls + "\t" + droppedCalls;
    }
}
