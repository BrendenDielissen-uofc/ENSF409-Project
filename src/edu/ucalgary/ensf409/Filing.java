package edu.ucalgary.ensf409;

public class Filing {
    private boolean rails;
    private boolean drawers;
    private boolean cabinet;

    public Filing(boolean rails, boolean drawers, boolean cabinet){
        this.rails = rails;
        this.drawers = drawers;
        this.cabinet = cabinet;
    }

    public boolean isRails() {
        return rails;
    }

    public void setRails(boolean rails) {
        this.rails = rails;
    }

    public boolean isDrawers() {
        return drawers;
    }

    public void setDrawers(boolean drawers) {
        this.drawers = drawers;
    }

    public boolean isCabinet() {
        return cabinet;
    }

    public void setCabinet(boolean cabinet) {
        this.cabinet = cabinet;
    }
}
