package com.bmarpc.acpsiam.bdictionarydev.otherclasses;

import java.util.ArrayList;

public class LibraryModel {

    public static ArrayList<String> allEnWordsArrayList = new ArrayList<>();
    public static ArrayList<String> idiomsArrayList = new ArrayList<>();
    public static ArrayList<String> proverbsArrayList = new ArrayList<>();
    public static ArrayList<String> prepositionsArrayList = new ArrayList<>();


    public ArrayList<String> getAllEnWordsArrayList() {
        return allEnWordsArrayList;
    }

    public void setAllEnWordsArrayList(ArrayList<String> allEnWordsArrayList) {
        LibraryModel.allEnWordsArrayList = allEnWordsArrayList;
    }

    public ArrayList<String> getIdiomsArrayList() {
        return idiomsArrayList;
    }

    public void setIdiomsArrayList(ArrayList<String> idiomsArrayList) {
        LibraryModel.idiomsArrayList = idiomsArrayList;
    }

    public ArrayList<String> getProverbsArrayList() {
        return proverbsArrayList;
    }

    public void setProverbsArrayList(ArrayList<String> proverbsArrayList) {
        LibraryModel.proverbsArrayList = proverbsArrayList;
    }

    public ArrayList<String> getPrepositionsArrayList() {
        return prepositionsArrayList;
    }

    public void setPrepositionsArrayList(ArrayList<String> prepositionsArrayList) {
        LibraryModel.prepositionsArrayList = prepositionsArrayList;
    }
}
