/*
 * Copyright 2017 Azad Bolour
 * Licensed under MIT (https://github.com/azadbolour/benchmark/blob/master/LICENSE)
 */

package com.bolour.benchmark;

import com.bolour.util.ExceptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a type of user of the system under test.
 */
public abstract class AbstractBenchmarkPersona {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractBenchmarkPersona.class);

    protected final BenchmarkRunnerConfig runnerConfig;

    public AbstractBenchmarkPersona(BenchmarkRunnerConfig runnerConfig) {
        this.runnerConfig = runnerConfig;
    }

    /**
     * Get the initial state of this persona interacting with the application.
     */
    public abstract AbstractBenchmarkState getInitialState();

    /**
     * Simulate the next interaction with the system for this persona.
     * 
     * @param state
     *            The current state this persona is in within the simulated session.
     * @return The resulting state transition.
     */
    public abstract BenchmarkStateTransition executeInteraction(AbstractBenchmarkState state);

    /**
     * Get the required think time for this persona while in this state during the user session,
     * before the next interaction is initiated.
     * 
     * @param state The current state.
     */
    public abstract int getThinkTimeMillis(AbstractBenchmarkState state);

    /**
     * Run a series of user interactions for this persona.
     * 
     * @return The timing statistics for this session.
     */
    public BenchmarkStatistics executeUserSession() {
        // LOGGER.info(format("executing session for %s", this.getClass().getName()));
        BenchmarkStatistics statistics = new BenchmarkStatistics();
        AbstractBenchmarkState state = getInitialState();
        while (!state.isFinalState()) {
            int thinkTimeMillis = getThinkTimeMillis(state);
            // LOGGER.info(format("in session iteration: think time in millis: %s", thinkTimeMillis));
            try {
                Thread.sleep(thinkTimeMillis);
            } catch (InterruptedException ex) {
                LOGGER.warn("Thinking interrupted for persona.");
            }
            long startMillis = System.currentTimeMillis();
            BenchmarkStateTransition transition;
            try {
                transition = executeInteraction(state);
            } catch (Throwable th) {
                ExceptionUtil.logThrowableAndCauses(th, LOGGER);
                statistics.incrementExcepionalSessions();
                return statistics; // Abort this session.
            }
            if (!transition.interaction.success) {
                statistics.incrementUnexpectedSessions();
                return statistics;
            }

            long endMillis = System.currentTimeMillis();
            long latency = endMillis - startMillis;
            statistics.addLatency(state, transition.interaction, latency);
            state = transition.toState;
        }
        return statistics;
    }

}
