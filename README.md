# COMP3011 Assignment 2 - Java Local Project

## Overview

A JavaFX video player extended with command-line video effects and background video processing.

## Requirements

- Java 25
- Maven
- JavaFX and JavaCV dependencies managed through Maven

## Build

```bash
mvn compile
```

## Run

Without effects:

```bash
mvn javafx:run "-Djavafx.args=simpsons.mp4"
```

With effects:

```bash
mvn javafx:run "-Djavafx.args=-nssw simpsons.mp4"
```

## Visual effects

| Option | Effect |
|---|---|
| `-n` | Frame numbering |
| `-s` | Scratches |
| `-f` | Flicker |
| `-w` | Black and white |
| `-y` | Yellow tint |
| `-v` | Vignette |
| `-d` | Dust |
| `-j` | Jitter |
| `-m` | Mottling |
| `-b` | Bleeding |
| `-p` | Pepper |

Effects can be combined, and repeated options are supported.

## Architecture

The video player uses an MVC structure. Video decoding and effects processing are assigned to a dedicated background executor, while JavaFX handles the interface.

## Known issue



## Attribution

The project builds upon the COMP3011 Assignment 2 starter code provided by Simon Ratcliffe. Modifications include command-line effects selection, background processing infrastructure, and associated documentation.

AI assistance was used for research and during debugging. 


