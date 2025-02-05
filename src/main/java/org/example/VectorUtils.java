package org.example;

import java.util.*;

public class VectorUtils {
    // این تابع ضرب داخلی دو بردار رو حساب میکنه
    public static double calculateDotProduct(List<Float> vector1, List<Float> vector2) {
        double result = 0.0;

        // هر دو بردار رو با هم ضرب میکنیم
        int size = vector1.size();
        if (vector2.size() < size) {
            size = vector2.size();
        }

        for (int i = 0; i < size; i++) {
            double num1 = vector1.get(i);
            double num2 = vector2.get(i);
            result = result + (num1 * num2);
        }

        return result;
    }

    // این تابع بهترین چانک‌ها رو پیدا میکنه
    public static List<String> getTopChunks(List<String> chunks, List<Double> scores, int count) {
        // لیستی برای نگهداری ایندکس‌ها درست میکنیم
        ArrayList<String> result = new ArrayList<>();
        ArrayList<Integer> bestIndices = new ArrayList<>();

        // اول همه ایندکس‌ها رو میریزیم تو یه لیست
        for (int i = 0; i < scores.size(); i++) {
            bestIndices.add(i);
        }

        // ایندکس‌ها رو براساس نمره‌ها مرتب می‌کنیم (از بزرگ به کوچیک)
        for (int i = 0; i < bestIndices.size(); i++) {
            for (int j = i + 1; j < bestIndices.size(); j++) {
                if (scores.get(bestIndices.get(i)) < scores.get(bestIndices.get(j))) {
                    int temp = bestIndices.get(i);
                    bestIndices.set(i, bestIndices.get(j));
                    bestIndices.set(j, temp);
                }
            }
        }

        // به تعدادی که خواسته شده از بهترین‌ها رو برمیگردونیم
        int howMany = count;
        if (bestIndices.size() < count) {
            howMany = bestIndices.size();
        }

        for (int i = 0; i < howMany; i++) {
            result.add(chunks.get(bestIndices.get(i)));
        }

        return result;
    }
}
