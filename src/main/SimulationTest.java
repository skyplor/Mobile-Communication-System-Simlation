/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.RNG;
import simulation.Simulator;
import simulation.Statistics;

/**
 *
 * @author Sky
 */
public class SimulationTest
{

    public static void main(String[] args) {
        FileWriter fw = null;
        String newLine = System.getProperty("line.separator");
        String usage = "USAGE: java XPhone [seed] [length] [replication] [warm-up] [channel] [reserved]" + newLine
                + "seed: The seed for the simulation random number generators" + newLine
                + "length: The time length in seconds of each replication of the simulation" + newLine
                + "replication: The number of replications" + newLine
                + "warm-up: The warm-up period in seconds" + newLine
                + "channel: The number of channels in each base station" + newLine
                + "reserved: The number of reserved channels for handovers in each base station" + newLine;
        try {
            if (args.length != 6) {
                System.out.print(usage);
                System.exit(-1);
            }

            int i = 0;
            long seed = Long.parseLong(args[i++]);
            if (seed == -1) {
                seed = System.currentTimeMillis();
            }
            double length = Double.parseDouble(args[i++]);
            int replication = Integer.parseInt(args[i++]);
            double warmup = Double.parseDouble(args[i++]);
            int channels = Integer.parseInt(args[i++]);
            int reserved = Integer.parseInt(args[i++]);
            Properties prop = new Properties();

            prop.load(new FileInputStream("data/xphone.properties"));
            double hwLength = Double.parseDouble(prop.getProperty("highway-length"));
            int stations = Integer.parseInt(prop.getProperty("base-stations"));
            double bcQos = Double.parseDouble(prop.getProperty("blocked-call-qos"));
            double dcQos = Double.parseDouble(prop.getProperty("dropped-call-qos"));
            double expMean = Double.parseDouble(prop.getProperty("exp-mean"));
            double normalM = Double.parseDouble(prop.getProperty("normal-m"));
            double normalStdev = Double.parseDouble(prop.getProperty("normal-stdev"));
            double expIaMean = Double.parseDouble(prop.getProperty("exp-ia-mean"));

            String params = "Seed = " + seed + newLine;
            params += "Length = " + length + newLine;
            params += "Replications = " + replication + newLine;
            params += "Warm-up = " + warmup + newLine;
            params += "Channels = " + channels + newLine;
            params += "Reserved = " + reserved + newLine;
            params += "Call duration mean = " + expMean + newLine;
            params += "Speed mean = " + normalM + newLine;
            params += "Speed stdev = " + normalStdev + newLine;
            params += "Call interarrival mean = " + expIaMean + newLine;


            ArrayList<Future<Statistics>> statistics = new ArrayList<Future<Statistics>>();
            ExecutorService es = java.util.concurrent.Executors.newFixedThreadPool(replication);
            RNG rng = new RNG(seed);
            for (int j = 1; j <= replication; j++) {
                Callable<Statistics> sim = new Simulator(stations, hwLength, channels,
                        reserved, length, j, rng, warmup, normalM,
                        expMean, normalStdev, expIaMean);
                Future<Statistics> future = es.submit(sim);
                statistics.add(future);
            }

            DecimalFormat df = new DecimalFormat("####.####");
            int accDc = 0;
            int accBc = 0;
            int accTc = 0;
            double accBcp = 0;
            double accDcp = 0;
            ArrayList<Statistics> results = new ArrayList<Statistics>();
            String output = newLine + (new java.util.Date()).toString() + newLine;
            String console = "";
            output += params;
            for (Future<Statistics> future : statistics) {
                try {
                    Statistics s = future.get();
                    results.add(s);
                    accDc += s.getDroppedCalls();
                    accBc += s.getBlockedCalls();
                    accTc += s.getTotalCalls();
                    accBcp += s.getBlockedCallsPercentage();
                    accDcp += s.getDroppedCallsPercentage();
                    console += ("Replica #" + s.getReplicaId() + newLine);
                    console += ("Total calls: " + s.getTotalCalls() + newLine);
                    console += ("Dropped calls: " + s.getDroppedCalls() + newLine);
                    console += ("Blocked calls: " + s.getBlockedCalls() + newLine);
                    console += ("Dropped calls percentage: %" + df.format(s.getDroppedCallsPercentage()) + newLine);
                    console += ("Blocked calls percentage: %" + df.format(s.getBlockedCallsPercentage()) + newLine);
                    console += ("__________________________________" + newLine);
//                    output += ("Replica #" + s.getReplicaId() + newLine);
//                    output += ("Total calls: " + s.getTotalCalls() + newLine);
//                    output += ("Dropped calls: " + s.getDroppedCalls() + newLine);
//                    output += ("Blocked calls: " + s.getBlockedCalls() + newLine);
//                    output += ("Dropped calls percentage: %" + df.format(s.getDroppedCallsPercentage()) + newLine);
//                    output += ("Blocked calls percentage: %" + df.format(s.getBlockedCallsPercentage()) + newLine);
//                    output += ("__________________________________" + newLine);
                } catch (InterruptedException ex) {
                    Logger.getLogger(SimulationTest.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ExecutionException ex) {
                    Logger.getLogger(SimulationTest.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            double avgTc = (double) (accTc / replication);
            double avgDc = (double) (accDc / replication);
            double avgBc = (double) (accBc / replication);
            double tcVar = 0;
            double dcVar = 0;
            double bcVar = 0;

            for (Statistics s : results) {
                tcVar += Math.pow(avgTc - s.getTotalCalls(), 2);
                dcVar += Math.pow(avgDc - s.getDroppedCalls(), 2);
                bcVar += Math.pow(avgBc - s.getBlockedCalls(), 2);
            }
            tcVar /= (replication - 1);
            dcVar /= (replication - 1);
            bcVar /= (replication - 1);

            output += ("Average total calls: " + df.format(avgTc) + newLine);
            output += ("Variance of total calls: " + tcVar + newLine);
            output += ("Average total dropped calls: " + df.format(avgDc) + newLine);
            output += ("Variance of dropped calls: " + dcVar + newLine);
            output += ("Average total blocked calls: " + df.format(avgBc) + newLine);
            output += ("Variance of blocked calls: " + bcVar + newLine);
            output += ("Average dropped calls percentage: %" + df.format(accDcp / replication)
                    + ", QoS requirement (%" + (dcQos) + ") " + (dcQos >= (accDcp / replication) ? "SATISFIED" : "NOT SATISFIED") + newLine);
            output += ("Average blocked calls percentage: %" + df.format(accBcp / replication)
                    + ", QoS requirement (%" + (bcQos) + ") " + (bcQos >= (accBcp / replication) ? "SATISFIED" : "NOT SATISFIED") + newLine);
            System.out.println(console + output);
            fw = new FileWriter("data/output.txt", true);
            fw.write(output);
        } catch (IOException ex) {
            Logger.getLogger(SimulationTest.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fw.close();
            } catch (IOException ex) {
                Logger.getLogger(SimulationTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.exit(0);
    }
}
