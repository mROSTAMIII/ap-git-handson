package org.example;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class EmbeddingService {
    public static List<Float> getEmbedding(String text) throws IOException {
        URL url = new URL("http://localhost:11434/api/embeddings");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        JSONObject requestBody = new JSONObject();
        requestBody.put("model", "nomic-embed-text");
        requestBody.put("prompt", text);

        OutputStream os = connection.getOutputStream();
        byte[] input = requestBody.toString().getBytes(StandardCharsets.UTF_8);
        os.write(input, 0, input.length);
        os.close();

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            br.close();

            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONArray embeddingArray = jsonResponse.getJSONArray("embedding");

            List<Float> embedding = new ArrayList<>();
            for (int i = 0; i < embeddingArray.length(); i++) {
                embedding.add((float) embeddingArray.getDouble(i));
            }
            return embedding;
        }
        return null;
    }
}
