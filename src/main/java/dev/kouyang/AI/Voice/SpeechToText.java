package dev.kouyang.AI.Voice;

import ai.picovoice.leopard.*;
import dev.kouyang.Settings;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static dev.kouyang.AI.Voice.VoiceAssistant.microphone;

public class SpeechToText {
    private static ByteArrayOutputStream out;

    public static void startRecording() {
        VoiceAssistant.initMicrophone();
        microphone.start();
        try {
            out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int numBytesRead;
            long silenceStartTime = System.currentTimeMillis();

            System.out.println(Settings.Logging.info + "Recording...");
            while (System.in.available() == 0) {
                // Read from the microphone
                numBytesRead = microphone.read(buffer, 0, buffer.length);
                if (numBytesRead <= 0) {
                    System.out.println(Settings.Logging.error + "Failed to read audio data from microphone " + SpeechToText.class.getName());
                    continue;
                }
                out.write(buffer, 0, numBytesRead);

                // Check if the current frame is silence
                if (isSilence(buffer)) {
                    // Duration for silence (in milliseconds)
                    int silenceDuration = 1500;
                    if (System.currentTimeMillis() - silenceStartTime >= silenceDuration) {
                        microphone.close();
                        break; // Break if silence is long enough
                    }
                } else {
                    silenceStartTime = System.currentTimeMillis(); // Reset silence timer
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        microphone.close();
    }

    private static boolean isSilence(byte[] audioData) {
        // Calculate the average amplitude of the audio frame
        long sum = 0;
        for (int i = 0; i < audioData.length; i += 2) {
            short amplitude = (short) ((audioData[i + 1] & 0xFF) << 8 | (audioData[i] & 0xFF));
            sum += Math.abs(amplitude);
        }
        double average = sum / (audioData.length / 2.0);

        // Convert amplitude to decibels
        double level = 20 * Math.log10(average / Short.MAX_VALUE);

        // Check if the level is below the silence threshold
        // Threshold for silence detection in dB
        int silenceThreshold = -50;
        return level < silenceThreshold;
    }

    public static String transcribe() {
        try {
            byte[] audioData = out.toByteArray();

            // Convert byte array to short array
            short[] shortAudioBuffer = new short[audioData.length / 2];
            ByteBuffer.wrap(audioData).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(shortAudioBuffer);

            Leopard leopard = new Leopard.Builder().setAccessKey(Settings.accessKey).build();
            System.out.println(Settings.Logging.info + "Transcribing...");
            if (shortAudioBuffer.length < 512) {
                System.out.println("Insufficient audio data for transcription. Minimum of 512 samples required.");
                return "Error: Insufficient audio data";
            }
            String transcription = leopard.process(shortAudioBuffer).getTranscriptString();
            leopard.delete();

            return transcription;
        } catch (LeopardException e) {
            e.printStackTrace();
            return null;
        }
    }
}
