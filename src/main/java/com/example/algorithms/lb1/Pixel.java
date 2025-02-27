package com.example.algorithms.lb1;

public class Pixel {
    private final int x;
    private final int y;
    private final float brightness;

    public Pixel(int x, int y, float brightness) {
        this.x = x;
        this.y = y;
        this.brightness = brightness;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public float getBrightness() {
        return brightness;
    }
}