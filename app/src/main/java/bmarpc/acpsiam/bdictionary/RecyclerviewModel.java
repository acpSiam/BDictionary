package bmarpc.acpsiam.bdictionary;

import java.util.ArrayList;

public class RecyclerviewModel {

    ArrayList<String> enWords, bnWords;
    static String viewType;


    public RecyclerviewModel(ArrayList<String> enWords, ArrayList<String> bnWords, String viewType) {
        this.enWords = enWords;
        this.bnWords = bnWords;
        this.viewType = viewType;
    }


    public String getViewType() {
        return viewType;
    }

    public void setViewType(String viewType) {
        this.viewType = viewType;
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
