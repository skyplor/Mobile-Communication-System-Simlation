/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation;

import java.io.FileWriter;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import model.*;

/**
 *
 * @author Sky
 */
public class Simulator implements Callable<Statistics>
{

    private int stations;
    private double highwayLength;
    private int channels;
    private int reserved;
    private double mean;
    private double iaMean;
    private double m;
    private double stdev;
    private double length;
    private double warmup;
    private int id;
    private RNG rng;
    private double coverage;
    private double clock;
    private int totalCalls;
    private ArrayList<BaseStation> baseStations;
    private TreeMap<Double, Event> fel;
    private BaseStation responsibleStation = null;

    public Simulator(int stations, double highwayLength, int channels,
            int reserved, double length, double warmup, int id, RNG rng,
            double m, double mean, double stdev, double iaMean)
    {
        this.stations = stations;
        this.highwayLength = highwayLength;
        this.channels = channels - reserved;
        this.reserved = reserved;
        this.length = length;
        this.warmup = warmup;
        this.id = id;
        this.rng = rng;
        this.m = m;
        this.mean = mean;
        this.stdev = stdev;
        this.iaMean = iaMean;
        clock = 0;
        totalCalls = 0;
        baseStations = new ArrayList<>();
        coverage = highwayLength / stations;
        for (int i = 0; i < stations; i++)
        {
            double pos = ((i + 1) * coverage) - 1;
            baseStations.add(new BaseStation(i + 1, this.channels, this.reserved, pos, coverage));
        }
        this.fel = new TreeMap<>();

    }

    private Event createCall()
    {
        CallInitiation result = new CallInitiation(new Car(++totalCalls, clock + rng.nextExp(iaMean),
                rng.nextUniform(highwayLength), rng.nextExp(mean),
                rng.nextNormal(m, stdev)));
        return result;
    }

    @Override
    public Statistics call() throws Exception
    {
        int calls = 0;
        int droppedCalls = 0;
        int blockedCalls = 0;
        int handovers = 0;
        int endCalls = 0;
        int windowSize = 10;

        ArrayList<WarmupStatistics> ws = new ArrayList<>();
        try (FileWriter fw = new FileWriter("data/output" + this.id + ".txt", false))
        {
            double accumTime;
            double accumDc;
            double accumBc;
            NumberFormat nf = NumberFormat.getInstance();
            nf.setGroupingUsed(false);
            nf.setMinimumFractionDigits(4);
            nf.setMinimumIntegerDigits(4);

            Event firstEvent = createCall();
            fel.put(firstEvent.getTime(), firstEvent);
            boolean stopSimulation = false;

            while (!fel.isEmpty())
            {

                double key = fel.firstKey();
                Event e = fel.remove(key);
                clock = e.getTime();

                if (ws.size() == windowSize)
                {
                    ws.remove(0);
                }
                ws.add(new WarmupStatistics(clock, blockedCalls, droppedCalls));
                accumTime = 0;
                accumDc = 0;
                accumBc = 0;
                for (WarmupStatistics s : ws)
                {
                    accumTime += s.getTime();
                    accumDc += s.getDroppedCalls();
                    accumBc += s.getBlockedCalls();
                }

                fw.write(nf.format((double) accumTime / ws.size()) + "\t" + nf.format((double) accumDc / ws.size()) + "\t" + nf.format((double) accumBc / ws.size()) + "\n");

                if (clock >= length)
                {
                    stopSimulation = true;
                }

                if (e instanceof CallInitiation)
                {
                    if (clock <= warmup)
                    {
                    }
                    else
                    {
                        calls++;
                    }
                    if (!stopSimulation)
                    {
                        Event ne = createCall();
                        fel.put(ne.getTime(), ne);
                    }
                    assignResponsible(e);
                    Event next = responsibleStation.initiateCall(e.getCar());
                    fel.put(next.getTime(), next);
                }
                else if (e instanceof CallHandOver)
                {
                    if (clock <= warmup)
                    {
                    }
                    else
                    {
                        handovers++;
                    }
                    CallHandOver h = (CallHandOver) e;
                    responsibleStation = h.getFrom();

                    /*
                     * To make the road a close loop so that all calls will be able to end as expected.
                     * For example, a car starts from the last BaseStation and requires a handover. Without this close loop, the call will have to terminate unexpectedly.
                     *
                     * UPDATED: Assignment requires us to terminate the call on reaching the end. This is handled at the BaseStation class
                     * Once the call is initiated, the function manipulateCall() will check whether to add a handover event or termination event.
                     * Hence mod (highwayLength * 1000) can be removed.
                     */
                    h.setPosition(h.getPosition()); // % (highwayLength * 1000));
                    Event next = responsibleStation.passHandover(e.getCar(), baseStations.get(responsibleStation.getId() % stations));
                    fel.put(next.getTime(), next);
                }
                else if (e instanceof CallTermination)
                {
                    if (clock <= warmup)
                    {
                    }
                    else
                    {
                        endCalls++;
                    }
                    CallTermination ec = (CallTermination) e;
                    ec.getAt().endCall(e.getCar());
                }
                else if (e instanceof DropCall)
                {
                    if (clock <= warmup)
                    {
                    }
                    else
                    {
                        droppedCalls++;
                    }
                    DropCall dc = (DropCall) e;
                    dc.getAt().dropCall();
                }
                else if (e instanceof BlockCall)
                {
                    if (clock <= warmup)
                    {
                    }
                    else
                    {
                        blockedCalls++;
                    }
                    BlockCall bc = (BlockCall) e;
                    bc.getAt().blockCall();
                }
            }

        }

        int dc = 0, bc = 0;
        String newLine = System.getProperty("line.separator");
        //System.out.println("Replication #" + id);
        for (BaseStation bs : baseStations)
        {
            dc += bs.getDroppedCalls();
            bc += bs.getBlockedCalls();
            //System.out.println("BaseStation " + bs.getId() + ":" + newLine + "Dropped Calls: " + dc + newLine + "Blocked Calls: " + bc);

        }
        return new Statistics(id, droppedCalls, blockedCalls, calls, handovers, endCalls);
    }

    private void assignResponsible(Event e)
    {
        for (BaseStation bs : baseStations)
        {
            if (e.getPosition() >= bs.getCoverageStart() && e.getPosition() < bs.getCoverageEnd())
            {
                responsibleStation = bs;
                break;
            }
        }
    }
}
