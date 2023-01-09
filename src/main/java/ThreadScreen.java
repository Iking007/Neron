import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Pavel
 */
public class ThreadScreen implements Runnable{
    static LabelS labelS;
    boolean key = false;
    ThreadScreen(LabelS labelS){
        this.labelS = labelS;
    }
    @Override
    public void run() {
        nu.pattern.OpenCV.loadLocally();
        Rectangle sizeScreen = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        sizeScreen.setSize(840,480);
        CascadeClassifier cascadeClassifier = new CascadeClassifier("src\\main\\resources\\cascade(512 1900 11 0.3).xml");
        Robot robot = null;
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
        int hsvPoint = 130;
        Scalar oneScalar = new Scalar(hsvPoint - 3, 50, 50);
        Scalar twoScalar = new Scalar(hsvPoint + 3, 255, 255);
        MatOfRect s = new MatOfRect();
        List<MatOfPoint> contours = new ArrayList<>();
        Mat frameGray = new Mat();
        Mat img;
        Rect ch =new Rect(sizeScreen.height - 30, sizeScreen.width - 30, 60,60 );
        Point onePoint;
        Point twoPoint;
        Scalar colorRect = new Scalar(255, 0, 255);
        Scalar colorMe = new Scalar(255, 0, 0);
        FireThread fireThread = new FireThread();
        //ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(); // Для массива бит из картинки
        while (true) {
            try {
                assert robot != null;
                // Преобразование в массива бит
                //Image ins = robot.createMultiResolutionScreenCapture(sizeScreen).getResolutionVariant(sizeScreen.width,sizeScreen.height);
                 BufferedImage in = robot.createScreenCapture(sizeScreen);
                //BufferedImage in = (BufferedImage) ins;
//                ImageIO.write(in, "bmp", byteArrayOutputStream);
//                byteArrayOutputStream.flush();
//                img = Imgcodecs.imdecode(new MatOfByte(byteArrayOutputStream.toByteArray()), Imgcodecs.CV_LOAD_IMAGE_UNCHANGED);
                img = BufferedImage2Mat(in);
                contours.clear();
                //Imgproc.applyColorMap(img, img, BufferedImage2Mat(color));
                Imgproc.cvtColor(img, frameGray, Imgproc.COLOR_BGR2HSV);
                Core.inRange(frameGray,oneScalar,twoScalar, frameGray);

                Imgproc.findContours(frameGray, contours, frameGray, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
//                if (!contours.isEmpty()) {
//                    for (int i = 0; i < contours.size(); ++i) {
//                        ch=Imgproc.boundingRect(contours.get(i));
//                        Imgproc.rectangle(img, ch.br(), ch.tl(), colorMe, 0);
//                    }
//
//                }

//                if (!contours.isEmpty()) {
//                    Imgproc.drawContours(img, contours.stream().toList(), 0, new Scalar(255, 0, 255),3);
//                }
//                if(!contours.isEmpty()){
//                    Imgproc.rectangle(img, contours.get(0).toArray()[0], contours.get(contours.size()-1).toArray()[0], new Scalar(255, 0, 255), 0);
//                }
                //Imgproc.applyColorMap(frameGray, frameGray, Imgproc.COLORMAP_BONE);
                //Imgproc.cvtColor(frameGray, frameGray, Imgproc.COLOR_BGRA2GRAY);
//                Imgproc.cvtColor(img, frameGray, Imgproc.COLOR_BGR2GRAY);
                //Imgproc.equalizeHist(frameGray, frameGray);

                //cascadeClassifier.detectMultiScale(frameGray, s);

                //cascadeClassifier.detectMultiScale(img, s);

                cascadeClassifier.detectMultiScale(img, s,1.1, 1,1, new Size(25,25),new Size(50, 50) );
                for (Rect t : MinRectangles(s.toList())) {
                    onePoint = new Point(t.x - t.height/2, t.y - t.width/ 2);
                    twoPoint = new Point(t.x + t.height/ 2 , t.y + t.width/ 2);
                    Imgproc.rectangle(img, onePoint, twoPoint, colorRect, 0);
                    //System.out.println(ch.x +"  "+ t.x);
                    if(key & ((ch.x + 30 < t.x | ch.x - 30 > t.x) & (ch.y + 30 < t.y | ch.y - 30 > t.y))){
                        robot.mouseMove(t.x , t.y);
                        new Thread(fireThread).start();
                    }
                }
                ThreadSetLab threadSetLab = new ThreadSetLab(new ImageIcon(Mat2BufferedImage(img)));
                new Thread(threadSetLab).start();
                //labelS.setLabel(new ImageIcon(Mat2BufferedImage(img)));
                //labelS.setLabel(new ImageIcon(ins));
                new Thread(new ThreadTimer()).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private static Mat BufferedImage2Mat(BufferedImage image) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "bmp", byteArrayOutputStream);
        byteArrayOutputStream.flush();
        return Imgcodecs.imdecode(new MatOfByte(byteArrayOutputStream.toByteArray()), Imgcodecs.IMREAD_UNCHANGED);
    }

    private static BufferedImage Mat2BufferedImage(Mat matrix)throws IOException {
        MatOfByte mob=new MatOfByte();
        Imgcodecs.imencode(".bmp", matrix, mob);
        return ImageIO.read(new ByteArrayInputStream(mob.toArray()));
    }

    private static List<Rect> MinRectangles(List<Rect> rects){
        //Объединяет квадратики в прямоугольники
        List <Rect> recs = new ArrayList<>(rects);
        for (int n = 0; n < recs.size(); n++){
            for(int j = n + 1; j < recs.size(); j++){
                if (recs.get(n).x + recs.get(n).height/2 > recs.get(j).x &
                        recs.get(n).x - recs.get(n).height/2 < recs.get(j).x &
                        recs.get(n).y + recs.get(n).width/2 > recs.get(j).y &
                        recs.get(n).y - recs.get(n).width/2 < recs.get(j).y){
                    // Объединение и создание нового прямоугольника из двух других
                    recs.set(n, new Rect( new Point(Math.min(recs.get(n).x - recs.get(n).height/2, recs.get(j).x- recs.get(j).height/2),
                            Math.min(recs.get(n).y - recs.get(n).width/2, recs.get(j).y- recs.get(j).width/2)),
                            new Point(Math.min(recs.get(n).x + recs.get(n).height/2, recs.get(j).x+ recs.get(j).height/2),
                            Math.min(recs.get(n).y + recs.get(n).width/2, recs.get(j).y+ recs.get(j).width/2))));
                    recs.remove(recs.get(j));
                }
            }
        }
        if (rects.size() > recs.size())
            System.out.println("ДО: " + rects.size() + " после: "+ recs.size());
        return recs;
    }
}
