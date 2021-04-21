import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;

abstract public class PaneListener implements Subject{
    ArrayList observers;

    PaneListener(){
        observers = new ArrayList();
    }

    @Override
    public void registerObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        int i = observers.indexOf(o);
        if (i >= 0) observers.remove(0);
    }

    @Override
    abstract public void notifyObservers();
}

class PaneMouseListener extends PaneListener implements MouseListener, MouseMotionListener{
    private Object source;
    private int getX;
    private int getY;
    private boolean isClick;

    @Override
    public void mouseClicked(MouseEvent e) {
        getX = e.getX();
        getY = e.getY();
        source = e.getSource();
        isClick = true;
        notifyObservers();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        getX = e.getX();
        getY = e.getY();
        source = e.getSource();
        isClick = false;
        notifyObservers();
    }

    @Override
    public void notifyObservers() {
        for (int i = 0; i < observers.size(); i++){
            Observer observer = (Observer)observers.get(i);
            observer.update(source, getX, getY, isClick);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseDragged(MouseEvent e) {}
}

class PaneActionListener extends PaneListener implements ActionListener{
    private String actionCommand;

    @Override
    public void actionPerformed(ActionEvent e) {
        actionCommand = e.getActionCommand();
        notifyObservers();
    }

    @Override
    public void notifyObservers() {
        for (int i = 0; i < observers.size(); i++){
            Observer observer = (Observer)observers.get(i);
            observer.update(actionCommand);
        }
    }
}