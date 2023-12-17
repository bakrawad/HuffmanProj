package com.example.huffman;

import java.io.IOException;

import java.io.OutputStream;

public class BitOutputStream extends OutputStream {
    private final OutputStream output;

    private int buffer;
    private int bitsRemaining;

    public BitOutputStream(OutputStream output) {
        this.output = output;
        this.buffer = 0;
        this.bitsRemaining = 32;

    }


    @Override
    public void write(int bit) throws IOException {//this method to write byte's in file
        if (bit != 0 && bit != 1) {
            throw new IllegalArgumentException("Bit must be 0 or 1");
        }

        buffer = (buffer << 1) | bit;//to shift the number in buffer to left
        bitsRemaining--;

        if (bitsRemaining == 0) {
            writeIntToStream(buffer, output);
            buffer = 0;
            bitsRemaining = 32;
        }
    }

    @Override
    public void flush() throws IOException {
        while (bitsRemaining < 32) {//to write the remaining bit's into the 4 byte
            write(0); // Pad with zeros
        }
        output.flush();
    }


    private void writeIntToStream(int value, OutputStream output) throws IOException {
        String s = "";
        for (int i = 3; i >= 0; i--) {
            s += ((value >> (i * 8)) & 0xFF)+" ";
            output.write((value >>> (i * 8)) & 0xFF);
        }//this method work when the 4 byte's are full of data and write the 4 byte's into the file
    }
}
