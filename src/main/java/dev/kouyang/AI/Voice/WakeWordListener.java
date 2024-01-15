package dev.kouyang.AI.Voice;

import ai.picovoice.porcupine.*;
import dev.kouyang.Settings;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;


public class WakeWordListener {

    private static Porcupine porcupine;

    public static boolean listen() {
        VoiceAssistant.initMicrophone();
        VoiceAssistant.microphone.start();
        try {
            porcupine = new Porcupine.Builder()
                    .setAccessKey(Settings.accessKey)
                    .setKeywordPaths(Settings.Porcupine.keywordPaths)
                    .build();
        } catch (PorcupineException e) {
            e.printStackTrace();
            System.out.println(Settings.Logging.error + "Porcupine failed to initialize");
        }
        System.out.println(Settings.Logging.info + "Listening for wake word...");
        // Buffer to hold audio data
        byte[] buffer = new byte[porcupine.getFrameLength() * 2];
        short[] shortBuffer = new short[buffer.length / 2];
        try {
            while (System.in.available() == 0) {

                // Read from the microphone
                int numBytesRead = VoiceAssistant.microphone.read(buffer, 0, buffer.length);
                if (numBytesRead <= 0) {
                    VoiceAssistant.microphone.close();
                    listen();
                    continue;
                }

                // Convert bytes to shorts
                ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(shortBuffer);

                // Process audio frame with Porcupine
                int result = porcupine.process(shortBuffer);
                if (result >= 0) {
                    System.out.println(Settings.Logging.info + "Wake word detected");
                    VoiceAssistant.microphone.close();
                    return true;
                }
            }
        } catch (PorcupineException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}
