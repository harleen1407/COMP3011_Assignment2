# COMP3011 Assignment 2 — Performance Evaluation

## Purpose

Evaluate the video's frame-processing headroom when running without effects, with one effect, with repeated effects, and with all 11 available effects. These are observations from the console output, **not** a benchmark against the original single-threaded starter application.

## Test environment and method

- Operating system: Windows (as shown by the command prompt)
- Java: 25 (Maven compiler configured with release 25)
- Build/run: Maven with JavaFX Maven plugin 0.0.8
- Test file: `simpsons.mp4`, 1920 × 800 H.264, approximately 23.98 frames/second, duration 2:17
- Nominal frame interval: 41,708 microseconds (41.708 ms)
- Each test launched from the project root with `mvn javafx:run "-Djavafx.args=..."`.
- A sample is counted as late when logged frame headroom is negative. Statistics below are calculated from **only the supplied console samples**; the sampling method and total frames processed are not established.

## Commands

```cmd
mvn compile
mvn javafx:run "-Djavafx.args=simpsons.mp4"
mvn javafx:run "-Djavafx.args=-w simpsons.mp4"
mvn javafx:run "-Djavafx.args=-nssw simpsons.mp4"
mvn javafx:run "-Djavafx.args=-nsfwyvdjmbp simpsons.mp4"
```

## Observed results

| Scenario | Effects | Logged samples | Late samples | Mean logged headroom | Minimum logged headroom | Maven result |
|---|---|---:|---:|---:|---:|---|
| 1 | None | 19 | 2 (10.5%) | +18.40 ms | -54.91 ms | BUILD SUCCESS |
| 2 | `-w` | 16 | 6 (37.5%) | -35.85 ms | -334.76 ms | BUILD SUCCESS |
| 3 | `-nssw` | 26 | 12 (46.2%) | -11.54 ms | -209.69 ms | BUILD SUCCESS |
| 4 | `-nsfwyvdjmbp` | 18 | 18 (100%) | -18,721.47 ms | -33,049.73 ms | BUILD SUCCESS |

**Interpretation:** Positive headroom means work was completed ahead of the logged deadline; negative headroom means it was late. The exact headroom instrumentation should be checked before interpreting these numbers as actual on-screen frame latency. The four scenarios were run separately and have different sample counts; this is not a controlled statistical comparison.

## Findings

1. **No effects:** Most sampled frames had positive headroom. Two of 19 logged measurements were late.
2. **One effect:** Adding black-and-white processing reduced available headroom and produced several late samples.
3. **Repeated/multiple effects:** The `-nssw` run included 12 late samples out of 26. A later separate `-nssw` run also produced 13 negative samples, indicating variability and a recurring timing problem; that run is not merged into the table above.
4. **All 11 effects:** All 18 reported samples were late. Logged headroom worsened from about -3.8 seconds to -33.0 seconds, consistent with processing falling increasingly behind the target schedule. This combination did not demonstrate real-time performance.
5. **Shutdown:** After a shutdown-related change, all four runs ended with Maven `BUILD SUCCESS`. An earlier version had thrown `RejectedExecutionException` when closing the application. The later tests did not show that exception.

## Architecture and responsiveness

The modified application uses a background executor for video work and the JavaFX Application Thread for interface events. This separation is intended to reduce blocking of UI events, but **it does not by itself establish parallel processing of effects across CPU cores**. A single background worker can still become overloaded. Quantitative GUI response-time measurements and CPU utilisation were not collected.

## Correctness and limitations

- The Pause button was reported to work.
- The FFmpeg message `avformat_open_input rejected some options: pixel_format, bgr24` appeared during runs, but playback proceeded.
- Successful process exit does not prove every effect looks correct or that every frame was shown.
- The all-effects scenario had a major real-time performance limitation.
- No baseline measurements of the unmodified single-threaded starter were collected, so no speedup percentage is claimed.
- No profiler, CPU-use trace, dropped-frame counter, or automated timing test was recorded.

## Recommended future work

- Profile the most expensive effects and consider bounded producer/consumer queues or carefully designed parallel frame processing that preserves display order and stateful effect semantics.
- Investigate late-frame handling and whether stale frames should be skipped.
- Benchmark the starter and modified versions using the same video, duration, machine, and effect combinations before claiming a speedup.
- Add automated tests for command-line parsing, effect selection, and shutdown lifecycle.

## Conclusion

All four tested effect combinations launched and exited successfully after the shutdown fix. Background processing is implemented, but the measured frame headroom becomes substantially worse with heavier effect combinations. In particular, the all-effects test did not meet the nominal real-time frame schedule. The results support a working prototype with significant performance limitations, not a verified performance improvement over the starter application.
