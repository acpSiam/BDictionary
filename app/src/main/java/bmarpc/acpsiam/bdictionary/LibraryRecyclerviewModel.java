package bmarpc.acpsiam.bdictionary;

import java.util.ArrayList;

public class LibraryRecyclerviewModel {

    ArrayList<String> enWords, bnWords;


    public LibraryRecyclerviewModel(ArrayList<String> enWords, ArrayList<String> bnWords) {
        this.enWords = enWords;
        this.bnWords = bnWords;
    }

    public ArrayList<String> getEnWords() {
        return enWords;
    }

    public void setEnWords(ArrayList<String> enWords) {
        this.enWords = enWords;
    }

    public ArrayList<String> getBnWords() {
        return bnWords;
    }

    public void setBnWords(ArrayList<String> bnWords) {
        this.bnWords = bnWords;
    }
}
