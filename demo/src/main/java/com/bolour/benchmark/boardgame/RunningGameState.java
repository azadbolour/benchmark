/*
 * Copyright 2017 Azad Bolour
 * Licensed under MIT (https://github.com/azadbolour/benchmark/blob/master/LICENSE)
 */

package com.bolour.benchmark.boardgame;

public class RunningGameState extends BaseGameState {
    public RunningGameState() {
        super(GameBenchmarkStateId.RUNNING);
    }

    @Override
    public boolean isFinalState() {
        return false;
    }
}
