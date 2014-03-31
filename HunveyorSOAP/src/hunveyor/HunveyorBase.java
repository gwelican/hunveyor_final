package hunveyor;

import javax.xml.ws.Endpoint;

public class HunveyorBase {

	public static void main(String[] args) {
		String serviceAddress = "http://0.0.0.0:443/hunveyor";
		HunveyorService service = new HunveyorService();
		Endpoint ep = Endpoint.publish(serviceAddress,service);
		// TODO Auto-generated method stub

	}

}
