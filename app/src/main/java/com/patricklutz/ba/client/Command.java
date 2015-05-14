package com.patricklutz.ba.client;


import java.nio.ByteBuffer;

/**
 * Modelclass for one single Command to the Server
 *
 *
 * Created by privat-patrick on 12.05.2015.
 */
public class Command {

    private int veloLeft;
    private int veloRight;

    private boolean shot;

    public Command(int veloLeft, int veloRight, boolean shot) {
        this.veloLeft = veloLeft;
        this.veloRight = veloRight;
        this.shot = shot;
    }

    public void setVeloLeft(int veloLeft) {
        this.veloLeft = veloLeft;
    }
    public void setVeloRight(int veloRight) {
        this.veloRight = veloRight;
    }
    public void setShot(boolean shot) {
        this.shot = shot;
    }

    public int getVeloRight() {
        return veloRight;
    }

    public boolean isShot() {
        return shot;
    }

    public int getVeloLeft() {

        return veloLeft;
    }
}
