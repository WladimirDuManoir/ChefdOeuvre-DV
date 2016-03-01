package QRcode;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import javax.swing.JFrame;
import javax.swing.JTextArea;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.DetectorResult;
import com.google.zxing.common.HybridBinarizer;

public class WebcamQRCodeExample extends JFrame implements Runnable, ThreadFactory {

    private static final long serialVersionUID = 6441489157408381878L;

    private Executor executor = Executors.newSingleThreadExecutor(this);

    private Webcam webcam = null;
    private WebcamPanel panel = null;
    private JTextArea textarea = null;

    public WebcamQRCodeExample() {
        super();

        setLayout(new FlowLayout());
        setTitle("Read QR / Bar Code With Webcam");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Dimension size = WebcamResolution.QVGA.getSize();
        Dimension size = new Dimension(640, 480);

        webcam = Webcam.getWebcams().get(0);
        webcam.setViewSize(size);

        panel = new WebcamPanel(webcam);
        panel.setPreferredSize(size);

        textarea = new JTextArea();
        textarea.setEditable(false);
        textarea.setPreferredSize(size);

        add(panel);
        add(textarea);

        pack();
        setVisible(true);

        executor.execute(this);
    }

    @Override
    public void run() {

        do {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Result result = null;
            DetectorResult detres = null;
            BufferedImage image = null;
            ResultPoint[] points = null;

            if (webcam.isOpen()) {

                if ((image = webcam.getImage()) == null) {
                    continue;
                }

                LuminanceSource source = new BufferedImageLuminanceSource(image);
                BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

                try {
                    result = new MultiFormatReader().decode(bitmap);
                    points = result.getResultPoints();
                    System.out.println(points[0].toString());

                } catch (NotFoundException e) {
                    // fall thru, it means there is no QR code in image
                }
            }

            if (result != null) {
                textarea.setText("Message du QR Code :" + result.getText() + "\nPosition X :" + points[0].getX() + "\nPosition Y :" + points[0].getY());
            }

        } while (true);
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r, "example-runner");
        t.setDaemon(true);
        return t;
    }

    public static void main(String[] args) {
        new WebcamQRCodeExample();
    }
}
