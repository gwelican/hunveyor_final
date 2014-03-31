import java.awt.Dimension;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.imageio.ImageIO;

import com.github.sarxos.webcam.Webcam;

public class Camera {
	public static String Screenshot(int camNum) {
		List<Webcam> cams = Webcam.getWebcams();
		Webcam cam = cams.get(camNum);
		try {
			cam.setViewSize(new Dimension(640, 480));
			cam.open();
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			OutputStream b64 = new Base64.OutputStream(os);
			ImageIO.write(cam.getImage(), "png", b64);
			String result = os.toString("UTF-8");
			cam.close();
			return result;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
