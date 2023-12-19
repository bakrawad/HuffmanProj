package com.example.huffman;

import java.io.Serializable;

public class Data implements Comparable<Data> ,Serializable {
    int freq;
    char ch;


    public Data(int freq, char ch) {
        this.freq = freq;
        this.ch = ch;
    }
    public Data(char ch) {
        this.ch = ch;
    }
    public Data() {

    }
    public int getFreq() {
        return freq;
    }

    public void setFreq(int freq) {
        this.freq = freq;
    }

    public char getCh() {
        return ch;
    }

    public void setCh(char ch) {
        this.ch = ch;
    }

    @Override
    public String toString() {
        return "Data{" +
                "freq=" + freq +
                ", ch=" + ch +
                '}';
    }

    @Override
    public int compareTo(Data o) {
        return 0;
    }
}