
# Bolour Computing Benchmarking Framework

## The Model

Different user personas interacting with a system under test have different 
interaction characteristics. The interaction profile of a persona is coded 
specifically for each type of persona in what is called a _user session_ for
that persona.

A user session consists of a series of interactions. An interaction starts in 
some _state_ does some work, and transitions the user to another state. 
States, of course, may include state variables that affect the next interaction
and state transition. This allows, for example, the next user interaction 
to utilize the data returned by the previous interaction.

The franework provides the following:

- Concrete generic classes for the concepts of _interaction_ and _state transition_. 

- Abstract base classes for the concepts of _persona_, and  _state_. 
  These are speciialized to the personas and states used in 
  modeling the interactions with a specific system under test.

- A benchmark runner that runs the benchmark.

- A benchmark runner configuration class for conguring the benchmark runner.
  Benchmarks are configured by using YAML configuration files.

## Configuration

Here is an example of a configuraiton file for a game benchmark:

```
  runnerConfig:
    users: 3
    durationSeconds: 5
    thinkTimeMillis: 10
    personas:
      - className: 'com.bolour.benchmark.boardgame.PlayerPersona'
        weight: 100
  gameServerUrl: 'http://localhost:6587'
  gameEndProbability: 0.2
```

Generic paramaters that apply to all benchmark are provided in the
_runnerConfig_ section. Specific parameters may also be provided 
to drive the specific interactions of a given benchmark.

- users: The number of concurrent users to simulate.

- durrationSeconds: The benchmark's duration.

- thinkTimeMillis: a generic think time

- personas: list of personas (their class names) and for each its relative weight 
  (relative probability of choosing that persona's session for execution).

## Getting Started

This project is dependent on the com.bolour.util.util library which is 
built in a separate project. Until this dependency makes it to public
maven repositories, the source for it needs to be cloned and built locally.

- Create specific personas and states and provide code teh interactions
  available from each state for each persona.

- build.sh

- run.sh

To create a benchmark for your system under test: you will need to create the
following specific classes:

- A number of states - and in particular an initial and final state.

- One or more personas.

- A main program that reads the configuration file and invokes the 
  benchmark runner.

See the demo sample for details.

## Road Map

### Version 1.0

- Report 90'th percentile of latencies in each category.

- Add a parameter for warm up time, and exclude measurements in that period.



