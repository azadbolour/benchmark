/*
 * Copyright 2017 Azad Bolour
 * Licensed under MIT (https://github.com/azadbolour/benchmark/blob/master/LICENSE)
 */

package com.bolour.benchmark;

/**
 * The state of a client being simulated in a benchmark.
 */
public abstract class AbstractBenchmarkState {

    public AbstractBenchmarkState() {
    }

    public abstract boolean isFinalState();

    public abstract String getStateName();

}
