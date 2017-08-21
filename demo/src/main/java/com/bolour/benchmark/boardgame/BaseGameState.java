/*
 * Copyright 2017 Azad Bolour
 * Licensed under MIT (https://github.com/azadbolour/benchmark/blob/master/LICENSE)
 */

package com.bolour.benchmark.boardgame;

import com.bolour.benchmark.AbstractBenchmarkState;

/**
 * Base class for game states.
 */
public abstract class BaseGameState extends AbstractBenchmarkState {

    public enum GameBenchmarkStateId {
        INITIAL, RUNNING, FINAL
    }

    private final GameBenchmarkStateId stateId;

    public BaseGameState(GameBenchmarkStateId stateId) {
        super();
        this.stateId = stateId;
    }

    public GameBenchmarkStateId getStateId() {
        return stateId;
    }

    @Override
    public String getStateName() {
        return stateId.name();
    }

}
