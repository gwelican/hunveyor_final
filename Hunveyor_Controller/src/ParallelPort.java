import java.math.BigInteger;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class ParallelPort {
	final static GpioController gpio;

	final static GpioPinDigitalOutput[] gpio_data_pins;

	final static GpioPinDigitalOutput toggle_bit;
	final static GpioPinDigitalOutput[] gpio_port_pins;

	final static GpioPinDigitalInput[] status_bits;
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
		//
		status_bits = new GpioPinDigitalInput[] {
				gpio.provisionDigitalInputPin(RaspiPin.GPIO_01),
				gpio.provisionDigitalInputPin(RaspiPin.GPIO_02)

		};
	}

	public static void ResetPins(GpioPinDigitalOutput[] pins) {

		for (GpioPinDigitalOutput pin : pins) {
			pin.low();
		}
	}

	public static void ResetParallelPort() throws InterruptedException {
		PutData(0, 0);
		Thread.sleep(50);
		PutData(0, 1);
		Thread.sleep(50);
		PutData(0, 2);
		Thread.sleep(50);
		PutData(0, 3);
		Thread.sleep(50);
	}

	public static boolean StartPosition(int motor) {
		return true;
	}

	public static void resetCameraPosition(int cameraId) throws InterruptedException {
		while (positionFeedback()) {
			ParallelPort.MoveMotor(cameraId, 1, 360);
		}
	}
	public static void verticalMove(int cameraId) throws InterruptedException {
		while(positionFeedback()) {
			System.out.println("Moving");
//			ParallelPort.ResetParallelPort();
			ParallelPort.PutData(16, 1);
			Thread.sleep(1000);
			//ParallelPort.ResetParallelPort();
			ParallelPort.PutData(0, 1);
			Thread.sleep(3500);
		}
		ParallelPort.ResetParallelPort();
	}

	public static boolean positionFeedback() {
		for(GpioPinDigitalInput pin : status_bits) {
			System.out.println("Pin state: " + pin.getState() + " Name: " + pin.getName());
			if (pin.getState() == PinState.LOW) {
				return false;
			}
		}
		return true;
	}

	public static void MoveMotor(int port, int direction, int degree)
			throws InterruptedException {
		int moves_req = (int) (degree * 2.3138);
		int moves = 0;
		int[] pattern = { 1, 3, 2, 6, 4, 12, 8, 9 };
		int i = 0;
		while (moves < moves_req) {
			if (positionFeedback() == false) {
				return;
			}
			if (direction == 1) {
				i++;
				if (i > 7)
					i = 0;
			} else {

				i--;
				if (i < 0)
					i = 7;
			}
			moves++;
			PutData(pattern[i], port);
			Thread.sleep(50);
		}

	}

	public static void PutData(int data, int port) {
		// gpio.provisionDigitalOutputPin(RaspiPin.GPIO_10, PinState.HIGH);
		toggle_bit.high();
		// toggle_bit.low();
		switch (port) {
		case 1:
			gpio_port_pins[0].low();
			gpio_port_pins[1].low();
			break;
		case 0:
			gpio_port_pins[0].low();
			gpio_port_pins[1].high();
			break;
		case 2:
			gpio_port_pins[0].high();
			gpio_port_pins[1].low();
			break;
		case 3:
			gpio_port_pins[0].high();
			gpio_port_pins[1].high();
			break;
		}
		// ResetPins(gpio_data_pins);
		for (int i = 0; i < 8; i++) {

			if (BigInteger.valueOf(data).testBit(i)) {
				gpio_data_pins[i].high();
			} else {
				gpio_data_pins[i].low();
			}
			// gpio_data_pins[i].high();
		}
		int[] gpiopins = { 4, 17, 22, 10, 9, 11, 23, 24, 25, 8 };
		// toggle_bit.high();

		toggle_bit.low();
	}

	public static void Led(boolean status) {
		if (status) {
			ParallelPort.PutData(64, 1);
		} else {
			ParallelPort.PutData(0, 1);
		}
	}
}
