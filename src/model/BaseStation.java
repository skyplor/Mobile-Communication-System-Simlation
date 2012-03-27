/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;

/**
 *
 * @author Sky
 */
public class BaseStation
{

    private int id;
    private ArrayList<Car> channels;
    private int freeChannels;
    private ArrayList<Car> handoverChannels;
    private int freeHandoverChannels;
    private double position;
    private double coverageStart;
    private double coverageEnd;
    private double coverage;
    private int droppedCalls;
    private int blockedCalls;

    public BaseStation(int id, int channels, int handoverChannels, double position, double coverage)
    {
        this.id = id;
        this.channels = new ArrayList<>();
        this.handoverChannels = new ArrayList<>();
        this.position = position * 1000;
        this.coverage = coverage * 1000;
        this.coverageStart = this.position - this.coverage / 2;
        this.coverageEnd = this.position + this.coverage / 2;
        this.freeChannels = channels;
        this.freeHandoverChannels = handoverChannels;
        this.droppedCalls = 0;
        this.blockedCalls = 0;
    }

    public Event initiateCall(Car c)
    {
        Event result;
        if (freeChannels > 0)
        {
            result = manipulateCall(c);
            channels.add(c);
            freeChannels--;
        }
        else
        {
            result = new BlockCall(c, this);
        }
        return result;
    }

    public Event passHandover(Car c, BaseStation b)
    {
        Event result = null;
        if (channels.contains(c))
        {
            channels.remove(c);
            freeChannels++;
            result = b.receiveHandover(c);
        }
        else if (handoverChannels.contains(c))
        {
            handoverChannels.remove(c);
            freeHandoverChannels++;
            result = b.receiveHandover(c);
        }
        return result;
    }

    private Event receiveHandover(Car c)
    {
        Event result;
        if (freeChannels > 0)
        {
            result = manipulateCall(c);
            channels.add(c);
            freeChannels--;
        }
        else if (freeHandoverChannels > 0)
        {
            result = manipulateCall(c);
            handoverChannels.add(c);
            freeHandoverChannels--;
        }
        else
        {
            result = new DropCall(c, this);
        }
        return result;

    }

    public void endCall(Car c)
    {
        if (channels.contains(c))
        {
            channels.remove(c);
            freeChannels++;
        }
        else if (handoverChannels.contains(c))
        {
            handoverChannels.remove(c);
            freeHandoverChannels++;
        }
    }

    private Event manipulateCall(Car c)
    {
        Event result;
        double speed = c.getSpeed();
        double callPosition = c.getPosition();
        double duration = c.getDuration();
        double arrivalTime = c.getTime();
        double callDistance = speed * duration;

        if ((callPosition + callDistance > coverageEnd) && (callPosition + callDistance < 40000))
        {
            duration -= (coverageEnd - callPosition) / speed;
            arrivalTime += (coverageEnd - callPosition) / speed;
            c.setDuration(duration);
            c.setTime(arrivalTime);
            c.setPosition(coverageEnd);
            result = new CallHandOver(c, this);
        }
        else
        {
            arrivalTime += duration;
            c.setTime(arrivalTime);
            result = new CallTermination(c, this);
        }
        return result;
    }

    public void dropCall()
    {
        droppedCalls++;
    }

    public void blockCall()
    {
        blockedCalls++;
    }

    public int getBlockedCalls()
    {
        return blockedCalls;
    }

    public void setBlockedCalls(int blockedCalls)
    {
        this.blockedCalls = blockedCalls;
    }

    public ArrayList<Car> getChannels()
    {
        return channels;
    }

    public void setChannels(ArrayList<Car> channels)
    {
        this.channels = channels;
    }

    public double getCoverage()
    {
        return coverage;
    }

    public void setCoverage(double coverage)
    {
        this.coverage = coverage;
    }

    public int getDroppedCalls()
    {
        return droppedCalls;
    }

    public void setDroppedCalls(int droppedCalls)
    {
        this.droppedCalls = droppedCalls;
    }

    public int getFreeChannels()
    {
        return freeChannels;
    }

    public void setFreeChannels(int freeChannels)
    {
        this.freeChannels = freeChannels;
    }

    public int getFreeHandoverChannels()
    {
        return freeHandoverChannels;
    }

    public void setFreeHandoverChannels(int freeHandoverChannels)
    {
        this.freeHandoverChannels = freeHandoverChannels;
    }

    public ArrayList<Car> getHandoverChannels()
    {
        return handoverChannels;
    }

    public void setHandoverChannels(ArrayList<Car> handoverChannels)
    {
        this.handoverChannels = handoverChannels;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public double getPosition()
    {
        return position;
    }

    public void setPosition(double position)
    {
        this.position = position;
    }

    public double getCoverageEnd()
    {
        return coverageEnd;
    }

    public void setCoverageEnd(double coverageEnd)
    {
        this.coverageEnd = coverageEnd;
    }

    public double getCoverageStart()
    {
        return coverageStart;
    }

    public void setCoverageStart(double coverageStart)
    {
        this.coverageStart = coverageStart;
    }

    @Override
    public String toString()
    {
        return "BaseStation{" + "id=" + id + ", freeChannels=" + freeChannels + ", freeHandoverChannels=" + freeHandoverChannels + '}';
    }
}
