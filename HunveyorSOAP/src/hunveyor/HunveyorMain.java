package hunveyor;
/**
 * Created by gwelican on 2014.02.03..
 */

import java.io.IOException;
import java.math.BigInteger;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

public class HunveyorMain {

	final static GpioController gpio;

	final static GpioPinDigitalOutput[] gpio_data_pins;

	final static GpioPinDigitalOutput toggle_bit;
	final static GpioPinDigitalOutput[] gpio_port_pins;
	static {
		gpio = GpioFactory.getInstance();
		gpio_data_pins = new GpioPinDigitalOutput[] {
				gpio.provisionDigitalOutputPin(RaspiPin.GPIO_07, "0 bit",
						PinState.LOW),
				gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, "1 bit",
						PinState.LOW),
				gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, "2 bit",
						PinState.LOW),
				gpio.provisionDigitalOutputPin(RaspiPin.GPIO_12, "3 bit",
						PinState.LOW),
				gpio.provisionDigitalOutputPin(RaspiPin.GPIO_13, "4 bit",
						PinState.LOW),
				gpio.provisionDigitalOutputPin(RaspiPin.GPIO_14, "5 bit",
						PinState.LOW),
				gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04, "6 bit",
						PinState.LOW),
				gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05, "7 bit",
						PinState.LOW) };
		toggle_bit = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_11,
				"toggle bit", PinState.HIGH);
		gpio_port_pins = new GpioPinDigitalOutput[] {
				gpio.provisionDigitalOutputPin(RaspiPin.GPIO_10, "0 bit",
						PinState.LOW),
				gpio.provisionDigitalOutputPin(RaspiPin.GPIO_06, "1 bit",
						PinState.LOW) };

	}


	public static void ReadWindDirection(int address) throws IOException {
		I2CBus i2cBus = I2CFactory.getInstance(I2CBus.BUS_1);
		I2CDevice temp = i2cBus.getDevice(address);
		byte[] buffer = new byte[2];
		// temp.write((byte)0x00);
		temp.read(buffer, 0, 2);
		for (byte b : buffer) {
			System.out.println("Address: " + address + " Data : " + b
					+ " hex 0x" + Integer.toHexString((b & 0xff)));
		}

	}

	// for (byte b : buffer) {
	// System.out.println("Debug: "+address+":"+data+" Data : "+ b
	// +" hex 0x"+Integer.toHexString((b & 0xff)));
	// }

	// String format = String.format("Data is: %10f 0x%2s 0x%2s",
	// (float) val / 1024 * 5,
	// Integer.toHexString((buffer[0] & 0xFF)),
	// Integer.toHexString((buffer[1] & 0xFF)));
	// System.out.println(format);
	// Thread.sleep(100);

	// }

	public static void printProgBar(int percent) {
		StringBuilder bar = new StringBuilder("[");

		for (int i = 0; i < 50; i++) {
			if (i < (percent / 2)) {
				bar.append("=");
			} else if (i == (percent / 2)) {
				bar.append(">");
			} else {
				bar.append(" ");
			}
		}

		bar.append("]   ").append(percent).append("%     ");
		System.out.print("\r" + bar.toString());
	}

	public static void ResetPins(GpioPinDigitalOutput[] pins) {

		for (GpioPinDigitalOutput pin : pins) {
			pin.low();
		}
	}

	public static void ResetParallelPort() throws InterruptedException {
		ParallelPort(0, 0);
		Thread.sleep(50);
		ParallelPort(0, 1);
		Thread.sleep(50);
		ParallelPort(0, 2);
		Thread.sleep(50);
		ParallelPort(0, 3);
		Thread.sleep(50);
	}

	public static boolean StartPosition(int motor) {
		boolean pos = false;
		// if
		return pos;
	}

	public static void MoveMotor(int port, int direction, int degree)
			throws InterruptedException {
		int moves_req = (int) (degree * 2.3138);
		int moves = 0;
		int[] pattern = { 1, 3, 2, 6, 4, 12, 8, 9 };
		int i = 0;
		while (moves < moves_req) {
			if (direction == 1) {
				i++;
				if (i > 7) i = 0;
			} else {
				
				i--;
				if (i < 0) i=7;
			}
			moves++;
			ParallelPort(pattern[i], port);
			Thread.sleep(50);
		}

	}

	public static void ParallelPort(int data, int port) {

		// gpio.provisionDigitalOutputPin(RaspiPin.GPIO_10, PinState.HIGH);
		toggle_bit.high();
		switch (port) {
		case 1:
			gpio_port_pins[0].low();
			gpio_port_pins[0].high();
			break;
		case 2:
			gpio_port_pins[0].low();
			gpio_port_pins[1].high();
			break;
		case 3:
			gpio_port_pins[0].high();
			gpio_port_pins[1].high();
			break;
		}
		ResetPins(gpio_data_pins);
		for (int i = 0; i < 8; i++) {
			if (BigInteger.valueOf(data).testBit(i)) {
				gpio_data_pins[i].high();
			}
		}
		// int[] gpiopins = {4,17,22,10,9,11,23,24,25,8};
		toggle_bit.low();
	}

	public static void main(String[] args) throws IOException,
			InterruptedException {

		ResetParallelPort();
		//MoveMotor(0, 0, 1);
		MoveMotor(2, 0, 40);
		Thread.sleep(50);
		ResetParallelPort();
		//
		if (true)
			return;
		// ReadTemperature(75);
		// ReadTemperature(76);

		// TODO Auto-generated method stub
		// int i;
		// ReadI2CData(0x8,0x24);
		// ReadI2CData(0x8,0x26);
		// ReadI2CData(0x8,0x28);
		// i = 0;
		// System.out.println("Start: " + System.nanoTime());
		// System.out.println("Start: " + System.currentTimeMillis());
		// long data = System.currentTimeMillis();
		// long data2 = System.nanoTime();
		// while (true) {
		// if (i > 50)
		// break;
		// System.out.print("TEMP: ");
		// float temp = ReadI2CData(0x7, 0x20);
		// printProgBar((int)temp*100);
		// System.out.print("Sound: ");
		// ReadI2CData(0x7, 0x21);
		// System.out.print("Light: ");
		// ReadI2CData(0x7, 0x22);
		System.out.print("Lightning" + 0 +" \n");
		System.out.print("RED: " + Analog1.ReadRed() + "\n");
		System.out.print("Yellow: " + Analog1.ReadYellow() + "\n");
		System.out.print("Green: " + Analog1.ReadGreen() + "\n");
		System.out.print("UV: " + Analog1.ReadUV() + "\n");
		System.out.print("Humidity: " + Analog1.ReadHumidity() + "\n");
		System.out.print("Pressure: " + Analog1.ReadPressure() + "\n");
		System.out.print("Gas: " + Analog1.ReadGas() + "\n");

		// ReadI2CData(0x7, 0x23);
		// ReadI2CDataAverage(0x7, 0x23);
		// System.out.print("YELLOW: ");
		// ReadI2CData(0x7, 0x24);
		// System.out.print("GREEN: ");
		// ReadI2CData(0x7, 0x25);
		// System.out.print("UV: ");
		// ReadI2CData(0x7, 0x26);
		// System.out.print("Humidity");
		// ReadI2CData(0x8, 0x20);
		// System.out.print("Pressure");
		// ReadI2CData(0x8, 0x21);
		// System.out.print("Gas");
		// ReadI2CData(0x8, 0x22);
		// for(i=0x20;i<0x26;i++) {
		// ReadI2CData(0x7, i);
		// }
		// i++;
		// System.out.println("#################################################################");
		// System.in.read();
		// Thread.sleep(1000);
		// }
		// System.out.println("Stop millis:" + (System.currentTimeMillis() -
		// data));
		// System.out.println("Stop nano:" + (System.nanoTime() - data2));
		// ReadI2CData(0x7,0x20);
		// ReadI2CData(0x7,0x22);
		// ReadI2CData(0x7,0x23);

		// ReadTemperature(0x7);
		// ReadTemperature(75);
		// ReadWindDirection(0x03);
		/*
		 * for(int i=0;i<80;i++) { try { ReadTemperature(i); }
		 * catch(java.io.IOException e) {
		 * //System.out.println("Address: "+i+" nem nyert"); } }
		 */
		/*
		 * I2CDevice device = i2cBus.getDevice(0x4B);
		 * System.out.println(i2cBus); tempSensor.write((byte)0xAA);
		 * 
		 * byte[] buffer = new byte[2]; int amountBytes = tempSensor.read(1,
		 * buffer, 0, 2); System.out.println("Amount of byte read : " +
		 * amountBytes); for(byte b : buffer) { System.out.println("Data : "+ b
		 * +" hex 0x"+Integer.toHexString((b & 0xff))); }
		 */

	}
	// ReadI2CData(0x8,0x24);
	// ReadI2CData(0x8,0x26);
	// ReadI2CData(0x8,0x28);

}