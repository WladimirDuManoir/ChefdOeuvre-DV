import javax.swing.*;
import java.awt.*;

public class Frame extends JPanel{

    public Frame() {
        super();
        JFrame window = new JFrame();
        window.setSize(840, 560);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.add(this);
        window.setVisible(true);

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

}
