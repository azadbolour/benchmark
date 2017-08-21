/*
 * Copyright 2017 Azad Bolour
 * Licensed under MIT (https://github.com/azadbolour/benchmark/blob/master/LICENSE)
 */

package com.bolour.benchmark.boardgame;


import com.bolour.benchmark.*;

import static java.lang.Math.random;
// import com.bolour.benchmark.boardgame.BaseGameState.GameBenchmarkStateMarker;

public class PlayerPersona extends AbstractBenchmarkPersona {

    private final GameBenchmarkConfig config;

    public PlayerPersona(GameBenchmarkConfig config) {
        super(config.runnerConfig);
        this.config = config;
    }

    @Override
    public AbstractBenchmarkState getInitialState() {
        return new InitialGameState();
    }

    @Override
    public BenchmarkStateTransition executeInteraction(AbstractBenchmarkState state) {
        BaseGameState s = (BaseGameState) state;
        BaseGameState.GameBenchmarkStateId marker = s.getStateId();
        switch (marker) {
            case INITIAL:
                return initialTransition(s);
            case RUNNING:
                return runTransition(s);
            case FINAL:
                return BenchmarkStateTransition.noTransition(s);
        }
        return null;
    }

    private BenchmarkStateTransition initialTransition(BaseGameState state) {
        BenchmarkInteraction interaction = new BenchmarkInteraction(
          new BenchmarkInteractionType("game", "start"));
        BaseGameState nextState = new RunningGameState();
        return new BenchmarkStateTransition(state, nextState, interaction);
    }

    private BenchmarkStateTransition runTransition(BaseGameState state) {
        double endProbability = config.gameEndProbability;
        double rand = random();
        if (rand < endProbability) {
            BenchmarkInteraction interaction = new BenchmarkInteraction(
              new BenchmarkInteractionType("game", "end"));
            BaseGameState nextState = new FinalGameState();
            return new BenchmarkStateTransition(state, nextState, interaction);
        }
        else {
            BenchmarkInteraction interaction = new BenchmarkInteraction(
              new BenchmarkInteractionType("game", "run"));
            BaseGameState nextState = new RunningGameState();
            return new BenchmarkStateTransition(state, nextState, interaction);
        }
    }

    private BenchmarkStateTransition runToEnd(BaseGameState state) {
        BenchmarkInteraction interaction = new BenchmarkInteraction(
          new BenchmarkInteractionType("game", "end"));
        BaseGameState nextState = new FinalGameState();
        return new BenchmarkStateTransition(state, nextState, interaction);
    }

    @Override
    public int getThinkTimeMillis(AbstractBenchmarkState state) {
        int baseThinkTime = config.runnerConfig.thinkTimeMillis;
        double rand = random();
        int thinkTime = (int) Math.round(rand * baseThinkTime);
        return thinkTime;
    }
}
