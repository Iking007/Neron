import javax.swing.*;

/**
 * @author Pavel
 */
public class ThreadSetLab implements Runnable{

    ImageIcon icon;
    ThreadSetLab(ImageIcon icon){
        this.icon = icon;
    }
    @Override
    public void run() {
        try {
            ThreadScreen.labelS.setLabel(icon);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
