package org.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter your question:");
        String prompt = scanner.nextLine();

        String knowledgeFilePath = "D:\\knowledge.txt";
        List<String> chunks = FileHandler.readAndChunkFile(knowledgeFilePath, 10);

        if (chunks.isEmpty()) {
            System.out.println("No knowledge chunks found. Exiting.");
            return;
        }

        List<Float> promptEmbedding = EmbeddingService.getEmbedding(prompt);
        if (promptEmbedding == null) {
            System.out.println("Failed to calculate embedding for the prompt. Exiting.");
            return;
        }

        List<List<Float>> chunkEmbeddings = new ArrayList<>();
        for (String chunk : chunks) {
            List<Float> embedding = EmbeddingService.getEmbedding(chunk);
            if (embedding != null) {
                chunkEmbeddings.add(embedding);
            }
        }

        List<Double> similarities = new ArrayList<>();
        for (List<Float> chunkEmbedding : chunkEmbeddings) {
            similarities.add(VectorUtils.calculateDotProduct(promptEmbedding, chunkEmbedding));
        }
        String selectedChunk = VectorUtils.getTopChunks(chunks, similarities, 1).get(0);

        String combinedPrompt = "knowledge:" + selectedChunk + "question:" + prompt;
        System.out.println("Combined Prompt to be sent to LLM:" + combinedPrompt);

        String llmUrl = "http://localhost:11434/api/generate";
        String modelName = "llama3.2";

        String completeResponse = LanguageModelService.getLanguageModelResponse(llmUrl, modelName, combinedPrompt);
        if (completeResponse != null ) {
            System.out.println("Complete Response from LLM: " + completeResponse);
        } else {
            System.out.println("Failed to retrieve a response from the LLM.");
        }
    }
}
