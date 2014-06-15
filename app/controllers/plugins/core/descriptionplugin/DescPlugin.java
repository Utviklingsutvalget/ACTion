package controllers.plugins.core.descriptionplugin;

import controllers.plugins.Plugin;
import controllers.plugins.PowerUp;

import javax.xml.transform.Result;

public class DescPlugin extends Plugin {

    protected DescPlugin(PowerUp powerUp) {
        super(powerUp);
    }

    @Override
    public Result accessPowerUp() {
        return null;
    }
}
