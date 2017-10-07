package com.example.dainq.smilenotes.common;


import java.util.ArrayList;
import java.util.List;

public class Utility {

    public static List<String> createList(int n, String string) {
        List<String> list = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            list.add(string + " " + i);
        }

        return list;
    }

    public static boolean isEmptyString(String string) {
        return string.trim().isEmpty();
    }
}
