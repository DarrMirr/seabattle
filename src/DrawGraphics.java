import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Владимир on 27.02.2015.
 */

enum TextGoods{
    TextField1("А"), TextField2("Б"), TextField3("В"), TextField4("Г"), TextField5("Д"),
    TextField6("Е"), TextField7("Ж"), TextField8("З"), TextField9("И"), TextField10("К");

    private String textGood;

    TextGoods(String s){
        textGood = s;
    }

    String getText(){
        return textGood;
    }
}

//абстракный класс для всех классов отрисовки
public abstract class DrawGraphics extends JComponent {
    protected BufferedImage buffer = null;
    protected Graphics2D g2d;

    protected void rebuildBuffer(){
        buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        g2d = buffer.createGraphics();
        startDraw();
    };

    protected abstract void startDraw();

    @Override
    protected void paintComponent(Graphics g){
        if (buffer == null){
            rebuildBuffer();
        }
        g.drawImage(buffer, 0, 0, this);
    }
}

//отрисовка сетки и объектов на ней
class DrawBattlePane extends DrawGraphics {
    private final int LENGHTNETS = 30;

    DrawBattlePane() {
        setIgnoreRepaint(true);
        setSize(LENGHTNETS * 10 + 1, LENGHTNETS * 10 + 1);
        setLocation(30, 30);
    }

    protected void startDraw(){
        g2d.setColor(Color.black);
        g2d.setFont(new Font("Dialog", Font.PLAIN, 18));

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                g2d.drawRect(LENGHTNETS * i, LENGHTNETS * j, LENGHTNETS, LENGHTNETS);
            }
        }
        g2d.setStroke(new BasicStroke(5));
        g2d.drawRect(0, 0, LENGHTNETS * 10, LENGHTNETS * 10);
        g2d.setStroke(new BasicStroke(3));
    }

    protected void drawShipOnPane(int[] shipCoords) {
        g2d.drawRect(shipCoords[0], shipCoords[1], shipCoords[2] - shipCoords[0], shipCoords[3] - shipCoords[1]);
        repaint();
    }

    protected void drawDot(int x, int y) {
        g2d.fillOval(x, y, 10, 10);
        repaint();
    }

    protected void drawCrest(int x, int y) {
        g2d.drawLine(x, y, x + 30, y + 30);
        g2d.drawLine(x + 30, y, x, y + 30);
        repaint();
    }

    protected void drawBoldRect(int[] coords) {
        if ((coords[3] - coords[1]) == 30) {
            g2d.drawRect(coords[0], coords[1], coords[2] - coords[0], 30);
        } else g2d.drawRect(coords[0], coords[1], 30, coords[3] - coords[1]);
        repaint();
    }

    protected void drawClearPane(){
        rebuildBuffer();
        repaint();
    }
}

//отрисовка букв и цифр
class DrawBattleChars extends DrawGraphics {
    private String s = null;
    private final int LENGHTNETS = 30;

    DrawBattleChars(){
        setIgnoreRepaint(true);
        setSize(LENGHTNETS * 11 + 10, LENGHTNETS * 11 + 10);
    }

    protected void startDraw(){
        g2d.setColor(Color.black);
        g2d.setFont(new Font("Dialog", Font.PLAIN, 18));
        for (int i = 1; i < 11; i++){
            g2d.drawString(TextGoods.values()[i-1].getText(), LENGHTNETS * i + 10, LENGHTNETS - 5);
            s = "" + i;
            g2d.drawString(s, 5, LENGHTNETS * i + 20);
        }
    }
}

//отрисовка кораблей
class DrawShip extends DrawGraphics {
    private int palub = 0;
    private int size = 0;
    private Dimension sizeShip = null;

    DrawShip(int n, int size){
        this.sizeShip = new Dimension(size * n, size);
        this.palub = n;
        this.size = size;
        setSize(sizeShip);
    }

    protected void startDraw(){
        g2d.setColor(Color.black);
        if (size == 20){
            for (int i = 0; i < palub; i++){
                g2d.drawRect(size * i, 0, size, size);
            }
        }
        g2d.setStroke(new BasicStroke(3));
        if (size == 20)g2d.drawRect(0, 0, size * palub, size);
        else if (sizeShip.getWidth() > sizeShip.getHeight()){
            g2d.drawRect(size, size, size * palub, size);
        } else  g2d.drawRect(size, size, size, size * palub);
    }

    protected Dimension getSizeShip(){
        return sizeShip;
    }

    protected void rotateShip(){
        buffer = null;
        if (sizeShip.getWidth() > sizeShip.getHeight()){
            sizeShip.setSize(size, size * palub);
        } else sizeShip.setSize(size * palub, size);
        repaint();
    }

    protected void nextShip(int palub){
        buffer = null;
        this.palub = palub;
        if (sizeShip.getWidth() > sizeShip.getHeight()){
            sizeShip.setSize(size * palub, size);
        } else sizeShip.setSize(size, size * palub);
        repaint();
    }

    protected boolean is_Hor(){
        if (sizeShip.getWidth() > sizeShip.getHeight()){
            return true;
        } else return false;
    }
/*
    protected void clearDrawShip(){
        this.removeNotify();
    } */
}

class DrawCursor extends DrawGraphics {

    DrawCursor(){
        setSize(30, 30);
        setLocation(30, 30);
    }

    protected void startDraw(){
        Color color = new Color(0,0,0,25);
        g2d.setColor(color);
        g2d.fillRect(30, 30, 30, 30);
    }
}

//отрисовка примера неправильного размещения кораблей
class DrawWrongShip extends DrawGraphics {

    DrawWrongShip(){
        setSize(190, 50);
    }

    @Override
    protected void startDraw() {
        g2d.setColor(Color.black);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRect(10, 10, 20, 20);
        g2d.drawRect(70, 10, 40, 20);
        g2d.drawRect(30, 30, 20, 20);
        g2d.drawRect(90, 30, 40, 20);
        g2d.drawRect(150, 30, 20, 20);
        g2d.drawRect(170, 30, 20, 20);
    }
}