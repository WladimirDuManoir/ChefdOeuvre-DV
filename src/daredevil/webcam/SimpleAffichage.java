package daredevil.webcam;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
 
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
 
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
 
public class SimpleAffichage {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
    static Mat frame = null;
 
    public static void main(String arg[]) {
 
        JFrame jframe = new JFrame("HUMAN MOTION DETECTOR FPS");
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel vidpanel = new JLabel();
        jframe.setContentPane(vidpanel);
        jframe.setSize(640, 480);
        jframe.setVisible(true);
 
        VideoCapture capture = new VideoCapture(0);
 
        Mat frame = new Mat();
        Size sz = new Size(640, 480);
 
        capture.read(frame);
        if (capture.isOpened()) {
            while (true) {
                capture.read(frame);
                if (!frame.empty()) {
 
                    Imgproc.resize(frame, frame, sz);
 
                    ImageIcon image = new ImageIcon(Mat2bufferedImage(frame));
                    vidpanel.setIcon(image);
                    vidpanel.repaint();
 
                }
            }
        }
 
    }
 
    public static BufferedImage Mat2bufferedImage(Mat image) {
        MatOfByte bytemat = new MatOfByte();
        Imgcodecs.imencode(".jpg", image, bytemat);
        byte[] bytes = bytemat.toArray();
        InputStream in = new ByteArrayInputStream(bytes);
        BufferedImage img = null;
        try {
            img = ImageIO.read(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return img;
    }
 
}