package com.lordralex.permissionsar;

import java.io.File;
import java.io.IOException;
import org.bukkit.configuration.file.YamlConfiguration;

public class Configuration {

	public static YamlConfiguration config = new YamlConfiguration();

	public void loadDefaults() {
		config = YamlConfiguration.loadConfiguration(new File("plugins/ListManager/config.yml"));
		if (!config.contains("random.value"))
			config.set("random.value", "derp");
		save();
	}

	public void save() {
		try {
			config.save(new File("plugins/PermissionsAR/config.yml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getRandomValue() {
		return config.getString("random.value");
	}
	public void setRandomValue (String value) {
		config.set("random.value", value);
	}
}