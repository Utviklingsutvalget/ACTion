package controllers.plugins;

import play.mvc.Controller;

import javax.xml.transform.Result;

public abstract class Plugin extends Controller {

    private final PowerUp powerUp;

    protected Plugin(PowerUp powerUp) {
        this.powerUp = powerUp;
    }

    public PowerUp getPowerUp() {
        return powerUp;
    }

    public abstract Result accessPowerUp();
}
