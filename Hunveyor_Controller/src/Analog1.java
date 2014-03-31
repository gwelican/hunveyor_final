import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

import java.io.IOException;

/**
 * Created by gwelican on 2014.02.03..
 */
public class Analog1 {
    private static float ReadI2CData(int address, int data) {

        // System.out.println("Reading from: "+address+" with command: "+data);
        I2CBus i2cBus;
        try {
            i2cBus = I2CFactory.getInstance(I2CBus.BUS_1);
            I2CDevice temp = i2cBus.getDevice(address);
            temp.write((byte) data);
            // unsigned char
            byte[] buffer = new byte[2];
            boolean read_done = true;
            do {

                try {
                    temp.read(buffer, 0, 2);
                    read_done = true;
                } catch (Exception e) {
                    read_done = false;
                }
            } while (!read_done);
            int val = ((buffer[1] << 8) & 0x0000ff00) | (buffer[0] & 0x000000ff);
            return (float) val / 1024;
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return 0xFF;
        }
    }
    public static void SwitchDust(boolean status) {
        if (status) {
            ReadI2CData(0x8, 0x24);
        } else {
            ReadI2CData(0x8, 0x25);
        }
    }

    public static void SwitchPressure(boolean status) {
        if (status) {
            ReadI2CData(0x8, 0x28);
        } else {
            ReadI2CData(0x8, 0x29);
        }
    }

    public static void SwitchGas(boolean status) {
        if (status) {
            ReadI2CData(0x8, 0x26);
        } else {
            ReadI2CData(0x8, 0x27);
        }
    }
    public static float ReadGas() {
        return ReadI2CData(0x8, 0x22);
    }

    public static float ReadPressure() {
        float val = ReadI2CData(0x8, 0x21);
        return val;

    }

    public static float ReadHumidity() {
        float val = ReadI2CData(0x8, 0x20);
        return val;
    }

    public static float ReadUV() {
        float val = ReadI2CDataAverage(0x7, 0x26);
        return val;
    }

    public static float ReadGreen() {
        float val = ReadI2CDataAverage(0x7, 0x25);
        return val;
    }

    public static float ReadYellow() {
        float val = ReadI2CDataAverage(0x7, 0x24);
        return val;
    }

    public static float ReadRed() {
        float val = ReadI2CDataAverage(0x7, 0x23);
        return val;
    }
    private static float ReadI2CDataAverage(int address, int command) {
        float[] values = new float[40];

        for(int i = 0; i < 40; i++) {
            values[i] = ReadI2CData(address, command);
        }
        float min=values[0],max=values[0];
        for(int i = 0; i < 40; i++) {
            if (min > values[i]) min = values[i];
            if (max < values[i]) max = values[i];
        }
        return (max-min)/2;

    }
}