/*
 * Copyright 2017 Azad Bolour
 * Licensed under MIT (https://github.com/azadbolour/benchmark/blob/master/LICENSE)
 */

package com.bolour.benchmark;

/**
 * Representation of an actual user interaction with the system under test.
 * The concept of an interaction is necessary because benchmarks
 * are about gathering timing specific information for different types of
 * interactions.
 */
public class BenchmarkInteraction {

    private static final Object[] NO_ARGS = new Object[] {};

    public final BenchmarkInteractionType type;
    public final Object[] arguments;
    public final Object result;
    public final boolean success;
    public final String fullName;

    public static final BenchmarkInteraction NO_INTERACTION =
      new BenchmarkInteraction(BenchmarkInteractionType.NO_INTERACTION_TYPE);

    public BenchmarkInteraction(BenchmarkInteractionType type) {
        this(type, NO_ARGS, null, true);
    }

    public BenchmarkInteraction(BenchmarkInteractionType type, Object[] arguments, Object result, boolean success) {
        this.type = type;
        this.arguments = arguments;
        this.result = result;
        this.success = success;
        this.fullName = this.type.fullName;
    }
}
