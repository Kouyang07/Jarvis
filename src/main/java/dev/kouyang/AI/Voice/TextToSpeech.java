package dev.kouyang.AI.Voice;

import dev.kouyang.Util;

import java.util.ArrayList;

public class TextToSpeech {
    public static void TTS(String text){
        ArrayList<String> responseLLM = new ArrayList<>();
        responseLLM.add("python");
        responseLLM.add("TTS.py");
        responseLLM.add("\"" + text + "\"");
        Util.cmd(responseLLM);
    }
}
