/*
 * Copyright 2017 Azad Bolour
 * Licensed under MIT (https://github.com/azadbolour/benchmark/blob/master/LICENSE)
 */

package com.bolour.benchmark;

/**
 * A state transition affected by a user interaction.
 */
public class BenchmarkStateTransition {

    /**
     * The state from which the persona transitioned.
     */
    public final AbstractBenchmarkState fromState;

    /**
     * The state to which the persona transitioned.
     */
    public final AbstractBenchmarkState toState;

    /**
     * The persona interaction leading to this state transition.
     */
    public final BenchmarkInteraction interaction;

    public static BenchmarkStateTransition noTransition(AbstractBenchmarkState state) {
        return new BenchmarkStateTransition(state, state, BenchmarkInteraction.NO_INTERACTION);
    }

    public BenchmarkStateTransition(AbstractBenchmarkState fromState, AbstractBenchmarkState toState, BenchmarkInteraction interaction) {
        this.fromState = fromState;
        this.toState = toState;
        this.interaction = interaction;
    }
}
