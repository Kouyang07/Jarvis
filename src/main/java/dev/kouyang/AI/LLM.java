package dev.kouyang.AI;

import dev.kouyang.Config;
import dev.kouyang.OpenAI.GPT.GPT;
import dev.kouyang.OpenAI.GPT.Messages;
import dev.kouyang.OpenAI.GPT.Model;
import dev.kouyang.OpenAI.GPT.Request;
import dev.kouyang.Settings;

import java.io.*;
import java.util.ArrayList;

import static dev.kouyang.OpenAI.GPT.Model.GPT_4;

public class LLM {
    public static String interact(String prompt){
        String JarvisPrompt = "You are a helpful personal assistant named Jarvis. You will be sent a transcript of a person's request. Your job is to figure out and return what you should do. Write a python script that does what the user wants which will be run on the user's computer. You have full access including permission to install libraries etc. The output of the python script will be converted to speech for the user in order to answer their question/request. If there is an error, just say \"ERROR\". If there is nothing to say, consider saying a very short explanation of what the code does. Only return the code. **DO NOT SAY ANYTHING ELSE**\n" +
                "\n" +
                "The user is currently on Windows 11\n" +
                "\n" +
                "Prompt: ";
        Config.API_KEY = Settings.API_KEY;
        GPT gpt = new GPT();
        Messages[] messages = new Messages[2];
        messages[0] = new Messages("user", JarvisPrompt);
        messages[1] = new Messages("user", prompt);
        Request requestBody = new Request(messages, "gpt-4-1106-preview", 1000, 1);
        return gpt.chat(requestBody).getResponse();
    }

    public static String process(String result){
        System.out.println(Settings.Logging.info + "Processing python script");
        result = result.replace("```python", "");
        result = result.replace("```", "");

        File file = new File("temp.py");
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(result);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<String> script = new ArrayList<>();
        script.add("python");
        script.add("temp.py");
        return cmd(script);
    }
    public static String cmd(ArrayList<String> script){
        StringBuilder stringBuilder = new StringBuilder();
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command(script);
        try {
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while((line = reader.readLine()) != null){
                stringBuilder.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }
}
