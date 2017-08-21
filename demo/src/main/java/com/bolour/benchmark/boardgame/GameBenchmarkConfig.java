/*
 * Copyright 2017 Azad Bolour
 * Licensed under MIT (https://github.com/azadbolour/benchmark/blob/master/LICENSE)
 */

package com.bolour.benchmark.boardgame;

import com.bolour.benchmark.BenchmarkRunnerConfig;
import com.bolour.util.YamlUtil;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;

import java.io.IOException;

import static com.bolour.util.FileUtil.readFileAsString;

public class GameBenchmarkConfig {

    private static final Class<?> theClass = GameBenchmarkConfig.class;
    private static final Logger LOGGER = LoggerFactory.getLogger(theClass);

    public final BenchmarkRunnerConfig runnerConfig;
    public final String gameServerUrl;
    public final float gameEndProbability;

    @JsonCreator
    public GameBenchmarkConfig(
      @JsonProperty("runnerConfig") BenchmarkRunnerConfig runnerConfig,
      @JsonProperty("gameServerUrl") String gameServerUrl,
      @JsonProperty("gameEndProbability") float gameEndProbability
    ) {
        this.runnerConfig = runnerConfig;
        this.gameServerUrl = gameServerUrl;
        this.gameEndProbability = gameEndProbability;
    }

    public static GameBenchmarkConfig readGameBenchmarkConfig(String configPath) throws IOException {
        String configString = readFileAsString(configPath);
        // Yaml yaml = new Yaml(new Constructor(theClass));
        // GameBenchmarkConfig config = (GameBenchmarkConfig) yaml.load(configString);
        GameBenchmarkConfig config = YamlUtil.yamlStringToImmutable(configString, GameBenchmarkConfig.class);
        LOGGER.info(config.toString());
        return config;
    }

    @Override
    public String toString() {
        return "GameBenchmarkConfig{" +
          "runnerConfig=" + runnerConfig +
          ", gameServerUrl='" + gameServerUrl + '\'' +
          ", gameEndProbability=" + gameEndProbability +
          '}';
    }
}
