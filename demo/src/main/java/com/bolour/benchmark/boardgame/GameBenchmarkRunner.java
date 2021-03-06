/*
 * Copyright 2017 Azad Bolour
 * Licensed under MIT (https://github.com/azadbolour/benchmark/blob/master/LICENSE)
 */

package com.bolour.benchmark.boardgame;

import com.bolour.benchmark.AbstractBenchmarkPersona;
import com.bolour.benchmark.BenchmarkRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

import static com.bolour.benchmark.boardgame.GameBenchmarkConfig.readGameBenchmarkConfig;
import static java.lang.String.format;

public class GameBenchmarkRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameBenchmarkRunner.class);

    private GameBenchmarkRunner() {}

    private static void usage() {
        System.err.println("Usage: java " + GameBenchmarkRunner.class.getName() + " yaml-benchmark-config-path");
        System.exit(-1);
    }

    public static void main(String[] args) {
        if (args.length == 0 || args.length > 1)
            usage();
        String configPath = args[0];

        try {
            GameBenchmarkConfig config = readGameBenchmarkConfig(configPath);
            LOGGER.info(format("configuration: %s", config));
            Function<Class<?>, AbstractBenchmarkPersona> factory = (Class<?> personaClass) -> personaFactory(config, personaClass);
            BenchmarkRunner runner = new BenchmarkRunner(config.runnerConfig, factory);
            runner.execute();
        } catch (Throwable ex) {
            LOGGER.error(format("", ex.getMessage()), ex);
            System.exit(-1);
        }
        LOGGER.info("benchmark completed");
    }

    private static AbstractBenchmarkPersona personaFactory(GameBenchmarkConfig config, Class<?> personaClass) {
        if (personaClass == PlayerPersona.class)
            return new PlayerPersona(config);
        else
            throw new IllegalArgumentException(format("unknown persona class %s", personaClass.getName()));
    }


}
