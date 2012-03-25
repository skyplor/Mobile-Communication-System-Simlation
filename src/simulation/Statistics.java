/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation;

/**
 *
 * @author Sky
 */
public class Statistics
{

    private int replicaId;
    private int droppedCalls;
    private int blockedCalls;
    private int totalCalls;
    private int handovers;
    private int endCalls;

    public Statistics(int replicaId, int droppedCalls, int blockedCalls, int totalCalls, int handovers, int endCalls)
    {
        this.replicaId = replicaId;
        this.droppedCalls = droppedCalls;
        this.blockedCalls = blockedCalls;
        this.totalCalls = totalCalls;
        this.handovers = handovers;
        this.endCalls = endCalls;
    }

    public double getDroppedCallsPercentage()
    {
        return (double) droppedCalls / handovers * 100;
    }

    public double getBlockedCallsPercentage()
    {
        return (double) blockedCalls / totalCalls * 100;
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

    public int getReplicaId()
    {
        return replicaId;
    }

    public void setReplicaId(int replicaId)
    {
        this.replicaId = replicaId;
    }

    public int getTotalCalls()
    {
        return totalCalls;
    }

    public void setTotalCalls(int totalCalls)
    {
        this.totalCalls = totalCalls;
    }

    public int getEndCalls()
    {
        return endCalls;
    }

    public void setEndCalls(int endCalls)
    {
        this.endCalls = endCalls;
    }

    public int getHandovers()
    {
        return handovers;
    }

    public void setHandovers(int handovers)
    {
        this.handovers = handovers;
    }
}
