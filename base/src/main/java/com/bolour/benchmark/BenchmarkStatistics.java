/*
 * Copyright 2017 Azad Bolour
 * Licensed under MIT (https://github.com/azadbolour/benchmark/blob/master/LICENSE)
 */

package com.bolour.benchmark;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Timing data about the execution of a user session in a benchmark.
 */
public class BenchmarkStatistics {

    private int exceptionalSessions;
    private int unexpectedSessions;

    /**
     * Two-dimensional map: stateName x interactionTypeName -> list of latencies:
     * Map<AbstractBenchmarkState.stateName: String, Map<BenchmarkInteractionType.fullName: String,
     * List<latency: long>>>.
     */
    private Map<String, Map<String, List<Long>>> latenciesByStateAndInteraction //
    = new HashMap<String, Map<String, List<Long>>>();

    /**
     * Add the latency of a single interaction/state transition to the statistics.
     * 
     * @param state
     *            The state of origin.
     * @param interaction
     *            The interaction.
     * @param latency
     *            The latency of the interaction in milliseconds.
     */
    public void addLatency(AbstractBenchmarkState state, BenchmarkInteraction interaction, long latency) {
        // Latency only makes sense if there was an interaction.
        if (interaction == null)
            return;
        String stateName = state.getStateName();
        String interactionName = interaction.fullName;
        addLatency(stateName, interactionName, latency);
    }

    private void addLatency(String stateName, String interactionName, long latency) {
        List<Long> latencyList = null;
        Map<String, List<Long>> latenciesByInteraction = latenciesByStateAndInteraction.get(stateName);
        if (latenciesByInteraction == null) {
            latenciesByInteraction = new HashMap<String, List<Long>>();
            latenciesByStateAndInteraction.put(stateName, latenciesByInteraction);
        }
        latencyList = latenciesByInteraction.get(interactionName);
        if (latencyList == null) {
            latencyList = new ArrayList<Long>();
            latenciesByInteraction.put(interactionName, latencyList);
        }
        latencyList.add(latency);
    }

    /**
     * Unsafe getter for latency data - returns reference and does not do safe copy.
     * 
     * @return The latency data.
     */
    public Map<String, Map<String, List<Long>>> getLatenciesByInteraction() {
        return latenciesByStateAndInteraction;
    }

    /**
     * Merge another statistics into this one.
     * 
     * @param other
     *            The other statistics.
     */
    public void mergeStatistics(BenchmarkStatistics other) {
        for (String stateName : other.latenciesByStateAndInteraction.keySet()) {
            Map<String, List<Long>> latenciesByInteraction = other.latenciesByStateAndInteraction.get(stateName);
            for (String interactionName : latenciesByInteraction.keySet())
                for (long latency : latenciesByInteraction.get(interactionName))
                    this.addLatency(stateName, interactionName, latency);
        }
        this.exceptionalSessions += other.exceptionalSessions;
        this.unexpectedSessions += other.unexpectedSessions;
    }

    public int getExceptionalSessions() {
        return exceptionalSessions;
    }

    public int getUnexpectedSessions() {
        return unexpectedSessions;
    }

    /**
     * Increment exceptional sessions: there was an exception.
     */
    public void incrementExcepionalSessions() {
        exceptionalSessions++;
    }

    /**
     * Increment unexpected sessions: there was an assertion failure.
     */
    public void incrementUnexpectedSessions() {
        unexpectedSessions++;
    }

    /**
     * Report statistics.
     */
    public void report() {
        if (exceptionalSessions == 0)
            System.out.println("No exceptions.");
        else
            System.out.println("Number of exceptions: " + exceptionalSessions + "; see logs for details.");
        if (unexpectedSessions == 0)
            System.out.println("No unexpected results.");
        else
            System.out.println("Number of unexpected results: " + unexpectedSessions + "; see logs for details.");

        for (String stateName : latenciesByStateAndInteraction.keySet()) {
            Map<String, List<Long>> latenciesByInteraction = latenciesByStateAndInteraction.get(stateName);
            for (String interactionName : latenciesByInteraction.keySet()) {
                System.out.println(stateName + "-" + interactionName + ": latencies -");
                int total = 0;
                List<Long> latencyList = latenciesByInteraction.get(interactionName);
                for (long latency : latencyList) {
                    System.out.print(latency + " ");
                    total += latency;
                }
                System.out.println();

                int average = total / latencyList.size();
                System.out.println(stateName + "-" + interactionName + ": average latency - " + average);
            }
        }
    }
}
