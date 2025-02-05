package org.example;

import java.io.*;
import java.util.*;

public class FileHandler {
    public static List<String> readAndChunkFile(String fileName, int wordCount) {
        // لیستی برای نگهداری چانک‌ها درست می‌کنیم
        ArrayList<String> chunks = new ArrayList<>();

        try {
            // فایل رو باز می‌کنیم
            FileReader file = new FileReader(fileName);
            BufferedReader reader = new BufferedReader(file);

            // کل متن رو می‌خونیم
            String allText = "";
            String line = reader.readLine();
            while (line != null) {
                allText = allText + line + " ";
                line = reader.readLine();
            }

            // متن رو به کلمه‌ها تقسیم می‌کنیم
            String[] words = allText.split(" ");

            // کلمه‌ها رو گروه گروه به لیست اضافه می‌کنیم
            String chunk = "";
            int count = 0;

            for (String word : words) {
                chunk = chunk + word + " ";
                count++;

                if (count == wordCount) {
                    chunks.add(chunk.trim());
                    chunk = "";
                    count = 0;
                }
            }

            // اگه چند تا کلمه باقی مونده، اونا رو هم اضافه می‌کنیم
            if (!chunk.equals("")) {
                chunks.add(chunk.trim());
            }

            reader.close();

        } catch (Exception e) {
            System.out.println("خطا در خواندن فایل!");
        }

        return chunks;
    }
}
