/*
 * Starter code supplied for Adelaide University COMP3011 Assignment 2.
 * Students are free to modify this file for assessment purposes.
 * 
 * Authors:
 *   1. Simon Ratcliffe, in collaboration with GPT-5.6 Terra
 *   2. <student name and student number insert here upon modification>
 *
 * Copyright 2026 Simon Ratcliffe
 */
package comp3011;

import java.nio.ByteBuffer;
import java.util.Random;

import org.bytedeco.javacv.Frame;

/**
 * Varies a frame's brightness to simulate film-projector flicker. Because of slight variations in timing and lamp
 * incandescence, old movie projectors would cause the movies to flicker, hence movies became known as 'flicks'.
 *
 * <p>
 * The media name and frame number seed a deterministic random brightness
 * scale, so the same frame always receives the same variation.
 * {@link #dimFrame(ByteBuffer, double)} applies that scale to each pixel.
 * </p>
 */
public class FrameFlickerer extends FrameProcessor {
    private static final double FLICKER_PERCENTAGE_SD = 5.0;

    private static int nextInstanceIndex;

    private final int instanceIndex;
    private int mediaSeed;

    public FrameFlickerer() {
        super();
        instanceIndex = nextInstanceIndex++;
    }

    @Override
    public void initialise(InfoVideo videoInfo) throws Exception {
        super.initialise(videoInfo);
        mediaSeed = saltedMediaSeed(videoInfo.mediaName(), instanceIndex);
    }


	/**
	 * Checks that the supplied frame uses a supported pixel format.
	 *
	 * @param frame the frame to validate
	 * @throws Exception if the frame format is unsupported
	 */
	private void validateFrame(Frame frame) throws Exception {
	    if (videoInfo.imageDepth() != Frame.DEPTH_UBYTE
	            && videoInfo.imageDepth() != Frame.DEPTH_BYTE) {
	        throw new Exception("FrameFlickerer requires 8-bit image frames");
	    }
	
	    if (videoInfo.imageChannels() < 1) {
	        throw new Exception("FrameFlickerer requires at least one image channel");
	    }
	
	    if (!(frame.image[0] instanceof ByteBuffer)) {
	        throw new Exception("FrameFlickerer requires byte-backed image frames");
	    }
	}


	@Override
	public void process(Frame frame, InfoFrame info) throws Exception {
	    if (frame.image == null
	            || frame.image.length == 0
	            || frame.image[0] == null) {
	        return;
	    }
	
	    validateFrame(frame);
	
	    ByteBuffer pixels = (ByteBuffer) frame.image[0];
	
	    Random random = new Random(
	            (long) mediaSeed + info.frameNumber());
	
	    double dimmingPercentage =
	            Math.abs(random.nextGaussian()) * FLICKER_PERCENTAGE_SD;
	
	    double brightnessScale =
	            Math.max(0.0, 100.0 - dimmingPercentage) / 100.0;
	
	    dimFrame(pixels, brightnessScale);
	}



	/**
	 * Applies a deterministic brightness adjustment to every visible
	 * pixel in the frame.
	 *
	 * Uses a lookup table to avoid repeated floating-point calculations
	 * for individual pixels.
	 *
	 * @param pixels the frame's writable pixel buffer
	 * @param brightnessScale brightness multiplier between 0.0 and 1.0
	 */
	private void dimFrame(ByteBuffer pixels, double brightnessScale) {
	    final byte[] brightnessLookup = new byte[256];
	
	    for (int value = 0; value < brightnessLookup.length; value++) {
	        brightnessLookup[value] =
	                (byte) Math.round(value * brightnessScale);
	    }
	
	    final int height = videoInfo.imageHeight();
	    final int widthBytes =
	            videoInfo.imageWidth() * videoInfo.imageChannels();
	    final int stride = videoInfo.imageStride();
	
	    for (int y = 0; y < height; y++) {
	        final int rowStart = y * stride;
	        final int rowEnd = rowStart + widthBytes;
	
	        for (int index = rowStart; index < rowEnd; index++) {
	            final int originalValue = pixels.get(index) & 0xFF;
	            pixels.put(index, brightnessLookup[originalValue]);
	        }
	    }
	}
}
