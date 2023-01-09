import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


/**
 * @author Pavel
 */
public class Main {
    static  JLabel label = new JLabel();
    static JFrame window = new JFrame("Неронка");
    static int fps = 0;
    static LabelS labelS = new LabelS(2);
    static ThreadScreen threadScreen = new ThreadScreen(labelS);
    static KeyListener keyListener = new KeyListener() {
        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_DOWN){
                System.out.println("!!!!!!KEY!!!!!!!!!!!!!!!!!");
                threadScreen.key = true;
                return ;
            }
            threadScreen.key = false;
        }

        @Override
        public void keyReleased(KeyEvent e) {
//            if (e.getKeyCode() == KeyEvent.VK_DOWN){
//                threadScreen.key = false;
//                return ;
//            }

        }
    };


    public static void main(String[] args)  {
        window.addKeyListener(keyListener);
        Thread thread0 = new Thread(threadScreen);
        Thread thread1 = new Thread(threadScreen);
        thread0.start();
        thread1.start();

        window.setSize(840, 480);

        window.add(label);

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);

        thread0.interrupt();
        thread1.interrupt();
    }
}
