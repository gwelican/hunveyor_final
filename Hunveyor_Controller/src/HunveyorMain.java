/**
 * Created by gwelican on 2014.02.03..
 */
import java.io.IOException;
import java.math.BigInteger;

//import com.github.sarxos.webcam.Webcam;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

public class HunveyorMain {

	public static float ReadTemperature(int address) throws IOException,
			InterruptedException {
		I2CBus i2cBus = I2CFactory.getInstance(I2CBus.BUS_1);
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

		// System.out.println("Temp: " + (msb + lsb * 0.00625));
		return (float) (msb + lsb * 0.00625);
		// for (byte b : buffer) {
		// System.out.println("0x03Address: " + address + " Data : " + b
		// + " hex 0x" + Integer.toHexString((b & 0xff)));
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

	public static void main(String[] args) throws IOException,
			InterruptedException {
		// System.out.println(Webcam.getWebcams());
		// Webcam.
		// ListPins();
		// ResetParallelPort();
		// MoveMotor(0, 0, 1);
		// MoveMotor(2, 0, 40);
		// Thread.sleep(50);
		// ResetParallelPort();
		//
		// ParallelPort.ResetParallelPort();
		// ParallelPort.Led(true);
		ParallelPort.PutData(0, 1); // lampa
		// ParallelPort.MoveMotor(1, 1, 45);
		// ParallelPort.ResetParallelPort();
		// Thread.sleep(3500);
		// ParallelPort.Led(false);

		// ListPins();
		System.out.println("Debugging...");
//		ParallelPort.resetCameraPosition(0);
		ParallelPort.verticalMove(1);
		ParallelPort.ResetParallelPort();
		for (;;) {
			ParallelPort.positionFeedback();
//			System.out.println(ParallelPort.positionFeedback());
			Thread.sleep(500);
		}
		// if (true)
		// return;
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
		// System.out.print("Lightning" + 0 +" \n");
		// System.out.print("RED: " + Analog1.ReadRed() + "\n");
		// System.out.print("Yellow: " + Analog1.ReadYellow() + "\n");
		// System.out.print("Green: " + Analog1.ReadGreen() + "\n");
		// System.out.print("UV: " + Analog1.ReadUV() + "\n");
		// System.out.print("Humidity: " + Analog1.ReadHumidity() + "\n");
		// System.out.print("Pressure: " + Analog1.ReadPressure() + "\n");
		// System.out.print("Gas: " + Analog1.ReadGas() + "\n");

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

	private static void ListPins() {
		final GpioController gpio;
		gpio = GpioFactory.getInstance();
		// // TODO Auto-generated method stub
		final GpioPinDigitalInput mybutton0 = gpio
				.provisionDigitalInputPin(RaspiPin.GPIO_00);
		final GpioPinDigitalInput mybutton1 = gpio
				.provisionDigitalInputPin(RaspiPin.GPIO_01);
		final GpioPinDigitalInput mybutton2 = gpio
				.provisionDigitalInputPin(RaspiPin.GPIO_02);
		final GpioPinDigitalInput mybutton3 = gpio
				.provisionDigitalInputPin(RaspiPin.GPIO_03);
		final GpioPinDigitalInput mybutton4 = gpio
				.provisionDigitalInputPin(RaspiPin.GPIO_04);
		final GpioPinDigitalInput mybutton5 = gpio
				.provisionDigitalInputPin(RaspiPin.GPIO_05);
		// final GpioPinDigitalInput mybutton6 =
		// gpio.provisionDigitalInputPin(RaspiPin.GPIO_06);
		final GpioPinDigitalInput mybutton7 = gpio
				.provisionDigitalInputPin(RaspiPin.GPIO_07);
		final GpioPinDigitalInput mybutton8 = gpio
				.provisionDigitalInputPin(RaspiPin.GPIO_08);
		final GpioPinDigitalInput mybutton9 = gpio
				.provisionDigitalInputPin(RaspiPin.GPIO_09);
		// final GpioPinDigitalInput mybutton10 =
		// gpio.provisionDigitalInputPin(RaspiPin.GPIO_10);
		// final GpioPinDigitalInput mybutton11 =
		// gpio.provisionDigitalInputPin(RaspiPin.GPIO_11);
		final GpioPinDigitalInput mybutton12 = gpio
				.provisionDigitalInputPin(RaspiPin.GPIO_12);
		final GpioPinDigitalInput mybutton13 = gpio
				.provisionDigitalInputPin(RaspiPin.GPIO_13);
		final GpioPinDigitalInput mybutton14 = gpio
				.provisionDigitalInputPin(RaspiPin.GPIO_14);
		final GpioPinDigitalInput mybutton15 = gpio
				.provisionDigitalInputPin(RaspiPin.GPIO_15);
		final GpioPinDigitalInput mybutton16 = gpio
				.provisionDigitalInputPin(RaspiPin.GPIO_16);
		final GpioPinDigitalInput mybutton17 = gpio
				.provisionDigitalInputPin(RaspiPin.GPIO_17);
		final GpioPinDigitalInput mybutton18 = gpio
				.provisionDigitalInputPin(RaspiPin.GPIO_18);
		final GpioPinDigitalInput mybutton19 = gpio
				.provisionDigitalInputPin(RaspiPin.GPIO_19);
		final GpioPinDigitalInput mybutton20 = gpio
				.provisionDigitalInputPin(RaspiPin.GPIO_20);
		// ParallelPort.PutData(0, 1);
		mybutton0.addListener(new PinStatus());
		mybutton1.addListener(new PinStatus());
		mybutton2.addListener(new PinStatus());
		mybutton3.addListener(new PinStatus());
		mybutton4.addListener(new PinStatus());
		mybutton5.addListener(new PinStatus());
		// mybutton6.addListener(new PinStatus());
		mybutton7.addListener(new PinStatus());
		mybutton8.addListener(new PinStatus());
		mybutton9.addListener(new PinStatus());
		// mybutton10.addListener(new PinStatus());
		// mybutton11.addListener(new PinStatus());
		mybutton12.addListener(new PinStatus());
		mybutton13.addListener(new PinStatus());
		mybutton14.addListener(new PinStatus());
		mybutton15.addListener(new PinStatus());
		mybutton16.addListener(new PinStatus());
		mybutton17.addListener(new PinStatus());
		mybutton18.addListener(new PinStatus());
		mybutton19.addListener(new PinStatus());
		mybutton20.addListener(new PinStatus());

	}

}