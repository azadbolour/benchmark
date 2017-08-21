/*
 * Copyright 2017 Azad Bolour
 * Licensed under MIT (https://github.com/azadbolour/benchmark/blob/master/LICENSE)
 */

package com.bolour.benchmark.boardgame;

public class FinalGameState extends BaseGameState {
    public FinalGameState() {
        super(GameBenchmarkStateId.FINAL);
    }

    @Override
    public boolean isFinalState() {
        return true;
    }
}
