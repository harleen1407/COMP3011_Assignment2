# COMP3011 Assignment 2 - Java Local Project

## Overview

This project implements a Java-based video player using JavaFX, JavaCV and Maven. It supports video playback, audio output, seeking, pausing and configurable visual effects.

The application is designed to apply multiple effects in a deterministic order while preserving the correctness of the displayed video frames.

## Requirements

- Java Development Kit compatible with the project's `pom.xml`
- Apache Maven
- JavaFX and JavaCV dependencies configured through Maven

## Building the Project

Open a terminal in the project directory containing `pom.xml`.

```bash
mvn clean verify
```

```bash
mvn compile
```

This command compiles the application and runs the configured verification steps.

## Run

Without effects:

```bash
mvn javafx:run "-Djavafx.args=simpsons.mp4"
```
With effects:

```bash
mvn javafx:run "-Djavafx.args=-nssw simpsons.mp4"
```

## Running the Application

Run the application using the entry point and arguments specified in the assignment instructions.

The player accepts a video file and optional effect flags.

For example, the following effect characters are supported:

| Flag | Effect |
|---|---|
| `n` | Frame numbering |
| `s` | Film scratches |
| `f` | Projector flicker |
| `w` | Black and white |
| `y` | Yellow tint |
| `v` | Vignette |
| `d` | Dust |
| `j` | Jitter |
| `m` | Mottle |
| `b` | Bleed |
| `p` | Pepper |

Multiple effects can be combined. Effects are applied in the order requested.

## Implementation

### Frame Processing

Each visual effect extends the `FrameProcessor` abstract class.

Processors are initialised using video metadata and apply their effects directly to decoded frame buffers.

### Deterministic Effects

Effects use video metadata and frame numbers to produce repeatable output. This supports consistent playback, seeking and automated visual verification.

### Playback Scheduling

The application uses a background playback executor to coordinate frame decoding, effect processing and playback timing.

JavaFX interface updates are dispatched through the JavaFX application thread.

### Current Concurrency Design

The current implementation uses a single playback worker to coordinate processing. Further parallelisation of independent frames is a possible future improvement.

## Testing

The application has been evaluated using the TITAN assessment environment.

Reported results:

- Part 1: 38 of 38 automated checks passed
- Part 2: 87.07% relative performance
- Startup speed: 102% of reference
- All reported visual frame comparisons passed
- No missing displayed frames reported

## Limitations

Processing all enabled effects can reduce the achievable playback frame rate.

The current playback pipeline does not parallelise effect processing across multiple frames.

## Future Improvements

Potential improvements include:

- Parallel processing of independent decoded frames
- Bounded frame-processing queues
- Ordered delivery of completed frames
- Further optimisation of computationally expensive pixel effects
- Additional automated performance and concurrency testing

## Attribution

The project builds upon the COMP3011 Assignment 2 starter code provided by Simon Ratcliffe. Modifications include command-line effects selection, background processing infrastructure, and associated documentation.

AI assistance was used for research and during debugging. 


