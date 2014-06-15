package controllers.plugins;

public abstract class PowerUp {

    private final int width;
    private final int height;
    private final String viewName;
    private final String friendlyName;

    protected PowerUp(int width, int height, String viewName, String friendlyName) {
        this.width = width;
        this.height = height;
        this.viewName = viewName;
        this.friendlyName = friendlyName;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getViewName() {
        return viewName;
    }
}
