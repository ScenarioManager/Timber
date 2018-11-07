package me.calebbassham.scenariomanager.timber;

import me.calebbassham.scenariomanager.api.ScenarioManagerInstance;
import org.bukkit.plugin.java.JavaPlugin;

public class TimberPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        ScenarioManagerInstance.getScenarioManager().register(new Scenario(), this);
    }
}
