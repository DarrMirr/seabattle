public interface Observer {
    public void update(Object source, int getX, int getY, boolean isClick);
    public void update(String actionCommand);
}
