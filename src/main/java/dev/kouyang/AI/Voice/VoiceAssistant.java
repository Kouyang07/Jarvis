package dev.kouyang.AI.Voice;

import dev.kouyang.Settings;
import dev.kouyang.AI.LLM;

import javax.sound.sampled.*;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class VoiceAssistant {
    public static TargetDataLine microphone;
    public static AudioFormat format = new AudioFormat(16000, 16, 1, true, false);
    public static DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);


    public static void run(){

        try {
            while (System.in.available() == 0) {
                    String request = request();
                    System.out.println(Settings.Logging.info + "Sending request: " + request);
                    String response = LLM.interact(request);
                    System.out.println(Settings.Logging.info + "Response received");
                    System.out.println(Settings.Logging.info + "Response: " + response);
                    String responseText = LLM.process(response);
                    System.out.println(Settings.Logging.info + "Response processed");
                    TextToSpeech.TTS(responseText);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void interact() {
        try {
            while (System.in.available() == 0) {
                String request = request();
                System.out.println(Settings.Logging.info + "Sending request: " + request);
                ArrayList<String> responseLLM = new ArrayList<>();
                responseLLM.add("python");
                responseLLM.add("Interactor.py");
                responseLLM.add("\"" + request + "\"");
                LLM.cmd(responseLLM);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String request(){
        if(WakeWordListener.listen()){
            SpeechToText.startRecording();
            delete();
            return SpeechToText.transcribe();
        }
        return "";
    }

    public static void initMicrophone(){
        try {
            if (!AudioSystem.isLineSupported(info)) {
                System.err.println("Microphone not supported");
                System.exit(1);
            }

            microphone = (TargetDataLine) AudioSystem.getLine(info);
            microphone.open(format);
        } catch (LineUnavailableException e) {
            System.out.println(Settings.Logging.error + "Microphone unavailable");
            e.printStackTrace();
        }
    }

    public static void delete() {
        microphone.close();
    }
}
