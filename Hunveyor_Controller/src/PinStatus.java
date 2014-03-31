import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;


public class PinStatus implements GpioPinListenerDigital{

	@Override
	public void handleGpioPinDigitalStateChangeEvent(
			GpioPinDigitalStateChangeEvent event) {
		// TODO Auto-generated method stub
		 System.out.println(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());
		
	}

}
