package hunveyor;

import java.io.IOException;
import java.math.BigInteger;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

public class Temperature {
	public static float ReadTemperature(int address) {
		try {
			I2CBus i2cBus;
			i2cBus = I2CFactory.getInstance(I2CBus.BUS_1);
			I2CDevice temp = i2cBus.getDevice(address);
			temp.write((byte) 0x51);
			Thread.sleep(50);
			temp.write((byte) 0xAA);

			byte[] buffer = new byte[2];
			temp.read(buffer, 0, 2);
			byte msb = buffer[0];
			byte lsb = buffer[1];

			if (BigInteger.valueOf(msb).testBit(7)) { // sign bit
				msb -= 256;
			}
			return (float) (msb + lsb * 0.00625);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1.0f;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -2.0f;
		}
	}

}
