package org.example;

import org.json.JSONObject;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class LanguageModelService {
    public static String getLanguageModelResponse(String url, String modelName, String prompt) throws IOException {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setDoOutput(true);
            connection.setConnectTimeout(120000);
            connection.setReadTimeout(120000);


            JSONObject requestData = new JSONObject();
            requestData.put("model", modelName);
            requestData.put("prompt", prompt);
            requestData.put("stream", true);

            System.out.println("Sending request to LLM with data: " + requestData.toString());

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = requestData.toString().getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int code = connection.getResponseCode();
            System.out.println("Response code from LLM: " + code); // اضافه کردن لاگ

            if (code == HttpURLConnection.HTTP_OK) {
                StringBuilder response = new StringBuilder();
                try (BufferedReader in = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    String line;
                    while ((line = in.readLine()) != null) {
                        System.out.println("Raw response line: " + line); // اضافه کردن لاگ
                        try {
                            JSONObject entry = new JSONObject(line);
                            String responseText = entry.optString("response", "");
                            response.append(responseText);
                            if (entry.optBoolean("done", false)) {
                                break;
                            }
                        } catch (Exception e) {
                            System.err.println("Error parsing JSON line: " + line);
                            e.printStackTrace();
                        }
                    }
                }
                return response.toString();
            } else {

                try (BufferedReader errorReader = new BufferedReader(
                        new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8))) {
                    StringBuilder errorResponse = new StringBuilder();
                    String line;
                    while ((line = errorReader.readLine()) != null) {
                        errorResponse.append(line);
                    }
                    System.err.println("Error response from LLM: " + errorResponse);
                }
                return null;
            }
        } catch (Exception e) {
            System.err.println("Exception in LLM call: " + e.getMessage());
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

}
