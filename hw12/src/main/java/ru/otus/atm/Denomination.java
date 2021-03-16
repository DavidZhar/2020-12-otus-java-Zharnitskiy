package ru.otus.atm;

public enum Denomination {
    m10 (10),
    m50 (50),
    m100 (100),
    m500 (500),
    m1000 (1000),
    m5000 (5000);

    private final int v;

    Denomination(int v) {
        this.v = v;
    }

    public int getValue() {
        return v;
    }
}
