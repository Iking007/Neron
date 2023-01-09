import javax.swing.*;

/**
 * @author Pavel
 */
public class LabelS {
    int threads;
    LabelS(int threads){
        this.threads = threads;
    }
    int n = 0;
    public synchronized void setLabel(ImageIcon icon) throws InterruptedException {
        if (n == 0 & threads > 0) {
            label1(icon);
        }
        else if (n == 1 & threads > 1){
            label2(icon);
        }
        else if (n == 2 & threads > 2){
            label3(icon);
        }
    }
    private void label1(ImageIcon icon) throws InterruptedException {
        if (n != 0){
            wait();
            System.out.println("t1 дождался");
        }
        if (threads == 1) {
            n = 0;
        }
        else {
            n++;
        }
        set(icon);
        notify();
    }
    private void label2(ImageIcon icon) throws InterruptedException {
        if (n != 1){
            wait();
            System.out.println("t2 дождался");
        }
        if (threads == 2) {
            n = 0;
        }
        else {
            n++;
        }
        set(icon);
        notify();
    }
    private void label3(ImageIcon icon) throws InterruptedException {
        if (n != 2){
            wait();
            System.out.println("t3 дождался");
        }
        if (threads == 3) {
            n = 0;
        }
        else {
            n++;
        }
        set(icon);
        notify();
    }
    void set(ImageIcon icon){
        Main.label.setIcon(icon);
        Main.fps++;
        Main.window.setTitle("Нейронка fps: " + Main.fps);
        //System.out.println(Main.fps);
    }
}
