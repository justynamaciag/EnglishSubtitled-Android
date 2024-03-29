package com.justyna.englishsubtitled.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;


public class Translation implements Serializable, Parcelable {

    private String engWord;
    private String plWord;
    private int progress;
    private int fails;
    private Boolean dictionaryAdded;

    protected Translation(Parcel in) {
        engWord = in.readString();
        plWord = in.readString();
    }

    public String getEngWord() {
        return engWord;
    }

    public String getPlWord(){
        return plWord;
    }

    public void setProgress(int progress){
        this.progress = progress;
    }

    public int getProgress(){
        return progress;
    }

    public void addFailAnswer(){
        this.fails++;
    }

    public void setFails(int fails){
        this.fails = fails;
    }

    public int getFails(){
        return this.fails;
    }

    public Boolean getDictionaryAdded() {
        return this.dictionaryAdded;
    }

    public void setDictionaryAdded(Boolean dictionaryAdded){
        this.dictionaryAdded = dictionaryAdded;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(engWord);
        dest.writeString(plWord);
    }

    public static final Creator<Translation> CREATOR = new Creator<Translation>() {
        @Override
        public Translation createFromParcel(Parcel in) {
            return new Translation(in);
        }

        @Override
        public Translation[] newArray(int size) {
            return new Translation[size];
        }
    };
}
