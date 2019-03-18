package com.gkskfhdlstmapk.hanpinetree.bookapp;

public class BookItem {
    String txt;
    int readCount;

    public BookItem(String txt,int count) {
        this.txt = txt;
        this.readCount = count;
    }

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    public int getReadCount() {
        return readCount;
    }

    public void setReadCount(int readCount) {
        this.readCount = readCount;
    }
}