
package daredevil.webcam;


import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

public class DetectionObjetAmelioree{

    public DetectionObjetAmelioree() {
    }

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
        Mat hsv_image = new Mat();
        Mat thresholded = new Mat();
        //Mat thresholded2 = new Mat();
        Mat circles = new Mat();

        Mat array255 = new Mat(480, 640, CvType.CV_8UC1);
        array255.setTo(new Scalar(255));

        Mat distance = new Mat(480, 640, CvType.CV_8UC1);
        List<Mat> lhsv = new ArrayList<Mat>(3);
        
        //Valeurs pour le rouge
//        Scalar hsv_min = new Scalar(0, 50, 50, 0);
//        Scalar hsv_max = new Scalar(6, 255, 255, 0);
//        Scalar hsv_min2 = new Scalar(175, 50, 50, 0);
//        Scalar hsv_max2 = new Scalar(179, 255, 255, 0);

       
//valeurs pour le vert
//        Scalar hsv_min = new Scalar(40, 0, 0, 0);
//        Scalar hsv_max = new Scalar(50, 255, 255, 0);
//        Scalar hsv_min2 = new Scalar(50, 0, 0, 0);
//        Scalar hsv_max2 = new Scalar(60, 255, 255, 0);
        
//Valeurs pour le bleu ciel
//         Scalar hsv_min = new Scalar(80, 0, 0, 0);
//        Scalar hsv_max = new Scalar(90, 255, 255, 0);
//        Scalar hsv_min2 = new Scalar(91, 0, 0, 0);
//        Scalar hsv_max2 = new Scalar(96, 255, 255, 0);
        
         Scalar hsv_min = new Scalar(10, 50, 50, 0);
         Scalar hsv_max = new Scalar(75, 255, 255, 0);

//    Orange  0-22
//    Jaune 22- 38
//    Vert 38-75
//    Bleu 75-130
//    Violet 130-160
//    Rouge 160-179 et 0-7

        
        Size sz = new Size(640, 480);

        capture.read(frame);
        if (capture.isOpened()) {
            while (true) {
                capture.read(frame);
                if (!frame.empty()) {

                    Imgproc.resize(frame, frame, sz);

                    Imgproc.cvtColor(frame, hsv_image, Imgproc.COLOR_BGR2HSV);
                    Core.inRange(hsv_image, hsv_min, hsv_max, thresholded);
                    //Core.inRange(hsv_image, hsv_min2, hsv_max2, thresholded2);
                    //Core.bitwise_or(thresholded, thresholded2, thresholded);
                    
                    Core.split(hsv_image, lhsv);

                    Mat S = lhsv.get(1);
                    Mat V = lhsv.get(2);

                    Core.subtract(array255, S, S);
                    Core.subtract(array255, V, V);

                    S.convertTo(S, CvType.CV_32F);
                    V.convertTo(V, CvType.CV_32F);

                    Core.magnitude(S, V, distance);

                    //Core.inRange(distance, new Scalar(0.0), new Scalar(200.0), thresholded2);

                    //Core.bitwise_and(thresholded, thresholded2, thresholded);
                    
                    Imgproc.GaussianBlur(thresholded, thresholded, new Size(9, 9), 0, 0);
                    Imgproc.HoughCircles(thresholded, circles,
                            Imgproc.CV_HOUGH_GRADIENT, 2,
                            thresholded.height() / 4, 500, 50, 0, 0);
                    int rows = circles.rows();
                    int elemSize = (int) circles.elemSize();
                    float[] data2 = new float[rows * elemSize / 4];
                    if (data2.length > 0) {
                        circles.get(0, 0, data2);
//                        for (int i = 0; i < data2.length; i = i + 3) {
//                            Point center = new Point(data2[i], data2[i + 1]);
//                            Imgproc.ellipse(frame, center, new Size(
//                                    (double) data2[i + 2],
//                                    (double) data2[i + 2]), 0, 0, 360,
//                                    new Scalar(255, 0, 255), 4, 8, 0);
//                        }
                        
                        // Creation du rectangle de detection
                        Rect r = detect_red_ball(thresholded);
                        if (r != null) {
                            Imgproc.rectangle(frame, r.tl(), r.br(), new Scalar(0,
                                    255, 0), 2);
                        }
                        Imgproc.putText(frame, "( x = " + 0.5 * (r.tl().x + r.br().x) + ", y = " + 0.5 * (r.tl().y + r.br().y) + " )", new Point(0.5 * (r.tl().x + r.br().x), 0.5 * (r.tl().y + r.br().y)), 1, 1, new Scalar(255, 255, 255));
                        System.out.println("( x = " + r.tl().x + ", y = " +r.tl().y + " )");
                        //Imgproc.drawMarker(frame, new Point(0.5 * (r.tl().x + r.br().x),0.5 * (r.tl().y + r.br().y)), hsv_max);
                        Imgproc.drawMarker(frame, new Point(r.tl().x,r.tl().y), hsv_max);

                    }

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
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return img;
    }

    public static Rect detect_red_ball(Mat outmat) {

        Mat v = new Mat();
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Imgproc.findContours(outmat, contours, v, Imgproc.RETR_LIST,
                Imgproc.CHAIN_APPROX_SIMPLE);

        double maxArea = -1;
        int maxAreaIdx = -1;
        Rect r = null;

        for (int idx = 0; idx < contours.size(); idx++) {
            Mat contour = contours.get(idx);
            double contourarea = Imgproc.contourArea(contour);
            if (contourarea > maxArea) {
                maxArea = contourarea;
                maxAreaIdx = idx;
                r = Imgproc.boundingRect(contours.get(maxAreaIdx));
            }
        }

        v.release();

        return r;

    }

}
