package com.patricklutz.ba.client;

/**
 * Modelclass for one single Command to the Robot
 *
 * Created by privat-patrick on 12.05.2015.
 */
public class Command {

    private static final int size = 6;

    private byte[] command;

    public Command() {
        command = new byte[size];
        command[0] = Byte.valueOf("0xFF");

        for (int i=0; i<size; i++) {
            command[i] = Byte.valueOf("0x00");
        }
    }

    public void setCamera(byte cam) {
        command[1] = cam;
    }

    public void setKick(byte kick) {
        command[2] = kick;
    }

    public void setKick(boolean kick) {
        if (kick) {
            command[2] = Byte.valueOf("0x01");
        } else {
            command[2] = Byte.valueOf("0x00");
        }
    }

    public void setMLeft(byte motor) {
        command[3] = motor;
    }

    public void setMLeft(int motor) {

    }

    public void setMRight(byte motor) {
        command[4] = motor;
    }

    public void setMRight(int motor) {

    }

    public void setChecksum(byte checksum) {
        command[5] = checksum;
    }

}
