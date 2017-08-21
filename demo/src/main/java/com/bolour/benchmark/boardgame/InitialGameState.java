/*
 * Copyright 2017 Azad Bolour
 * Licensed under MIT (https://github.com/azadbolour/benchmark/blob/master/LICENSE)
 */

package com.bolour.benchmark.boardgame;

public class InitialGameState extends BaseGameState {
    public InitialGameState() {
        super(GameBenchmarkStateId.INITIAL);
    }

    @Override
    public boolean isFinalState() {
        return false;
    }
}
