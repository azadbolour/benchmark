/*
 * Copyright 2017 Azad Bolour
 * Licensed under MIT (https://github.com/azadbolour/benchmark/blob/master/LICENSE)
 */

package com.bolour.benchmark;

import com.bolour.util.math.WeightedDistribution;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static java.lang.String.format;

public class BenchmarkRunnerConfig {

    private static final Class<?> theClass = BenchmarkRunnerConfig.class;
    private static final Logger LOGGER = LoggerFactory.getLogger(theClass);

    public final int users;
    public final int durationSeconds;
    public final int thinkTimeMillis;
    public final List<PersonaConfig> personas;

    @JsonCreator
    public BenchmarkRunnerConfig(
      @JsonProperty("users") int users,
      @JsonProperty("durationSeconds") int durationSeconds,
      @JsonProperty("thinkTimeMillis") int thinkTimeMillis,
      @JsonProperty("personas") List<PersonaConfig> personas
    ) {
        this.users = users;
        this.durationSeconds = durationSeconds;
        this.thinkTimeMillis = thinkTimeMillis;
        this.personas = personas;
    }

    public static WeightedDistribution computePersonaDistribution(
      List<BenchmarkRunnerConfig.PersonaConfig> personaConfigs) {
        WeightedDistribution distribution = new WeightedDistribution();
        for (BenchmarkRunnerConfig.PersonaConfig pc : personaConfigs) {
            Class<?> personaClass;
            try {
                personaClass = Class.forName(pc.className);
            } catch (ClassNotFoundException e) {
                throw new IllegalArgumentException(format("unable to load person class %s - %s",
                  pc.className, e.getMessage()));
            }
            if (pc.weight <= 0)
                throw new IllegalArgumentException(format("invalid weigh %s - must be positive",
                  pc.weight));
            distribution.add(personaClass, (double) pc.weight);
        }
        distribution.complete();
        return distribution;
    }

    @Override
    public String toString() {
        return "BenchmarkRunnerConfig{" +
          "users=" + users +
          ", durationSeconds=" + durationSeconds +
          ", thinkTimeMillis=" + thinkTimeMillis +
          ", personas=" + personas +
          '}';
    }

    public static class PersonaConfig {
        public final String className;
        public final int weight;

        @JsonCreator
        public PersonaConfig(
          @JsonProperty("className") String className,
          @JsonProperty("weight") int weight
        ) {
            this.className = className;
            this.weight = weight;
        }

        @Override
        public String toString() {
            return "PersonaConfig{" +
              "className='" + className + '\'' +
              ", weight=" + weight +
              '}';
        }
    }
}
