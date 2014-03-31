package hunveyor;

import javax.jws.WebService;

@WebService(serviceName="Hunveyor")
public class HunveyorService {
	public String Hello() {
		return "works";
	}
	public float GetLight() {
		return Analog1.ReadLight();
	}
	public float GetSound() {
		return Analog1.ReadNoise();
	}
	public float GetGas() {
		return Analog1.ReadGas();
	}
	public float GetHumidity() {
		return Analog1.ReadHumidity();
	}
	public float GetPressure() {
		return Analog1.ReadPressure();
	}
	public float GetUV() {
		return Analog1.ReadUV();
	}
	public float GetGreen() {
		return Analog1.ReadGreen();
	}
	public float GetYellow() {
		return Analog1.ReadYellow();
	}
	public float GetRed() {
		return Analog1.ReadRed();
	}
	public void SetPressure(boolean status) {
		Analog1.SwitchPressure(status);
	}
	public void SetDust(boolean status) {
		Analog1.SwitchDust(status);
	}
	public void SetGas(boolean status) {
		Analog1.SwitchGas(status);
	}
	public float GetTemperature(int address) {
		float temp = -60;
		for(int tries = 0; tries < 5 && temp == -60; tries++) temp = Temperature.ReadTemperature(address);
		return temp;
	}
}
