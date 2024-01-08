package dev.kouyang.Voice;

import dev.kouyang.Settings;

import javax.sound.sampled.*;

public class VoiceAssistant {
    public static TargetDataLine microphone;
    public static AudioFormat format = new AudioFormat(16000, 16, 1, true, false);
    public static DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

    public String request(){
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

    public void delete() {
        microphone.close();
    }
}
