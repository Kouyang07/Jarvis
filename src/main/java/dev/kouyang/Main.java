package dev.kouyang;

import dev.kouyang.Voice.VoiceAssistant;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        VoiceAssistant voiceAssistant = new VoiceAssistant();

        while(System.in.available() == 0){
            String request = voiceAssistant.request();
            System.out.println(Settings.Logging.info + "Request: " + request);
        }

    }
}