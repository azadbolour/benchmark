/*
 * Copyright 2017 Azad Bolour
 * Licensed under MIT (https://github.com/azadbolour/benchmark/blob/master/LICENSE)
 */

package com.bolour.benchmark;

import com.bolour.util.math.WeightedDistribution;
import com.bolour.util.ExceptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

import static com.bolour.benchmark.BenchmarkRunnerConfig.computePersonaDistribution;
import static java.lang.String.format;

/**
 * Main benchmark runner - spawns client threads that run sessions for random persona.
 */
public final class BenchmarkRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(BenchmarkRunner.class);

    private final BenchmarkRunnerConfig runnerConfig;
    private final Function<Class<?>, AbstractBenchmarkPersona> personaFactory;
    private final WeightedDistribution personaDistribution;

    private boolean stopping = false;

    public BenchmarkRunner(BenchmarkRunnerConfig runnerConfig,
                           Function<Class<?>, AbstractBenchmarkPersona> personaFactory) {
        this.runnerConfig = runnerConfig;
        this.personaFactory = personaFactory;
        this.personaDistribution = computePersonaDistribution(runnerConfig.personas);
    }

    public Class<?> getRandomPersonaClass() {
        Class<?> personaClass = (Class<?>) personaDistribution.randomMember();
        return personaClass;
    }

    public void execute() {
        BenchmarkThread[] threads = new BenchmarkThread[0];

        try {
            int users = runnerConfig.users;
            threads = new BenchmarkThread[users];
            for (int i = 0; i < users; i++)
                threads[i] = new BenchmarkThread(personaFactory);
            for (BenchmarkThread th : threads)
                th.start();
        } catch (Throwable th) {
            ExceptionUtil.logThrowableAndCauses(th, LOGGER);
            LOGGER.error("Aborting.");
            System.exit(-1);
        }

        int durationSeconds = runnerConfig.durationSeconds;
        try {
            Thread.sleep((long) durationSeconds * 1000L);
        } catch (InterruptedException ex) {
            LOGGER.error("Benchmark run interrupted: " + ex.getMessage());
        }
        setStopping(true);
        for (Thread th : threads)
            try {
                th.join();
            } catch (InterruptedException ex) {
                // Since 'th' was interrupted, we assume it was about to die and not hang the process.
                LOGGER.warn("interrupted exception after stop request ignored");
            }
        computeStatistics(threads);
    }

    private synchronized boolean isStopping() {
        return stopping;
    }

    private synchronized void setStopping(boolean stopping) {
        this.stopping = stopping;
    }

    private void computeStatistics(BenchmarkThread[] threads) {
        BenchmarkStatistics totalStatistics = new BenchmarkStatistics();
        for (BenchmarkThread thread : threads)
            totalStatistics.mergeStatistics(thread.getStatistics());
        totalStatistics.report();
    }

    /**
     * Benchmark thread that repeatedly obtains a random person and runs a simulated session for
     * that persona.
     */
    class BenchmarkThread extends Thread {

        Function<Class<?>, AbstractBenchmarkPersona> personaFactory;

        private BenchmarkStatistics statistics = new BenchmarkStatistics();
        private int numExceptions = 0;
        private int numUnexpected = 0;

        public BenchmarkThread(Function<Class<?>, AbstractBenchmarkPersona> personaFactory) {
            this.personaFactory = personaFactory;
        }

        public void run() {
            LOGGER.info(format("thread %s running", Thread.currentThread().getId()));
            Class<?> personaClass = getRandomPersonaClass();
            AbstractBenchmarkPersona persona = personaFactory.apply(personaClass);
            while (!BenchmarkRunner.this.isStopping()) {
                try {
                    /*
                     * No think time between successive simulated user sessions. This thread must
                     * contribute a bona fide concurrent session at all times. Number of concurrent
                     * users is a benchmark parameter. Think times determine gaps in interaction
                     * within a session.
                     *
                     * Note. At least one session in each thread has to complete
                     * before the benchmark ends. Stopping is ignored while a session is running.
                     */
                    BenchmarkStatistics sessionStatistics = persona.executeUserSession();
                    statistics.mergeStatistics(sessionStatistics);
                    LOGGER.info("user session completed");
                } catch (Throwable ex) {
                    ExceptionUtil.logThrowableAndCauses(ex, LOGGER);
                    LOGGER.error("Continuing.");
                    numExceptions++;
                }
            }
        }

        public synchronized BenchmarkStatistics getStatistics() {
            return statistics;
        }

        public synchronized int getNumExceptions() {
            return numExceptions;
        }

        public synchronized int getUnexpected() {
            return numUnexpected;
        }
    }
}
