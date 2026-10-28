# Performance Evaluation

## 1. Objective

The objective of this evaluation is to assess playback correctness, processing performance, startup speed and CPU utilisation.

## 2. Test Environment

The player was evaluated using the TITAN assessment environment.

The test included normal video playback and playback with all supported effects enabled.

## 3. Results

| Metric | Result |
|---|---|
| Part 1 automated checks | 38/38 passed |
| Normal playback frame rate | 24.000 fps |
| All-effects playback frame rate | 20.764 fps |
| Reference all-effects frame rate | 23.847 fps |
| Relative performance | 87.07% |
| Startup speed | 102% |
| CPU core utilisation, all effects | 2.29 of 8 |
| Reference CPU core utilisation | 2.74 |
| Missing displayed frames | None reported |
| Visual comparison | All reported checks passed |

## 4. Performance Analysis

Normal playback achieved 24.000 frames per second, matching the real-time frame rate reported by TITAN.

With all effects enabled, the player achieved 20.764 frames per second, compared with the reference implementation's 23.847 frames per second.

This represents approximately 87.07% of reference performance.

The lower frame rate suggests that applying multiple image effects introduces significant computational overhead.

## 5. Correctness

The TITAN results reported no missing displayed frames and successful visual comparisons.

This indicates that the tested implementation preserved the expected visual output and frame sequence.

## 6. Concurrency Analysis

The current player uses a single background playback executor to coordinate decoding, processing and playback scheduling.

Although the assessment reported utilisation equivalent to 2.29 CPU cores, this does not establish that the application's effect-processing pipeline runs frames in parallel. Other runtime and decoding threads may contribute to CPU usage.

A future implementation could use a bounded worker pool to process independent frames concurrently while preserving display order.

## 7. Limitations

The main performance limitation is that frame processing is sequential within the current playback worker.

Additional effects increase per-frame processing time, potentially causing playback to fall behind real time.

## 8. Conclusion

The player demonstrated strong functional correctness and achieved 87.07% of the reference implementation's performance with all effects enabled.

Future optimisation should focus on computationally expensive effects and safe parallel processing of independent frames without compromising deterministic visual output.
