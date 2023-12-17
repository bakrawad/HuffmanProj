package com.example.huffman;

import java.io.IOException;
import java.io.InputStream;

public class BitInputStream extends InputStream {
    private final InputStream input;
    private int buffer;
    private int bitsRemaining;

    public BitInputStream(InputStream input) {
        this.input = input;
        this.buffer = 0;
        this.bitsRemaining = 0;
    }

    @Override
    public int read() throws IOException {
        if (bitsRemaining == 0) {
            buffer = input.read();
            if (buffer == -1) {
                return -1;
            }
            bitsRemaining = 8;
        }
        int bit = (buffer >>> (bitsRemaining - 1)) & 1;
        bitsRemaining--;
        return bit;
    }

    public void skipLines(int linesToSkip) throws IOException {
        int linesSkipped = 0;
        while (linesSkipped < linesToSkip) {
            int data = input.read();
            if (data == -1) {
                break; // Reached end of file
            } else if (data == '\n') {
                linesSkipped++; // Increment lines skipped upon encountering a newline character
            }
        }
    }
}
