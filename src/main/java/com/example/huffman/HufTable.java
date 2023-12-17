package com.example.huffman;

public class HufTable {
    final String ascii;
    final String huffman;

    final int length;
    final int freq;

    public HufTable(String ascii, String huffman, int length, int freq) {
        this.ascii = ascii;
        this.huffman = huffman;
        this.length = length;
        this.freq = freq;
    }


    public String getAscii() {
        return ascii;
    }

    public String getHuffman() {
        return huffman;
    }

    public int getLength() {
        return length;
    }

    public int getFreq() {
        return freq;
    }
}
