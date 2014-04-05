/*
 * Copyright (C) 2014 AE97
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.ae97.totalpermissions.mcstats;

import java.io.IOException;
import org.apache.commons.lang.Validate;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

/**
 * @author Lord_Ralex
 */
final class PostRunnable extends BukkitRunnable {

    private boolean firstPost = false;
    private final int PING_INTERVAL = 1200 * 15;
    private Metrics metrics;
    private BukkitTask task;

    protected PostRunnable() {
    }

    public boolean start(Plugin pl, Metrics m) {
        Validate.notNull(m, "Metrics instance cannot be null");
        if (task != null) {
            return false;
        }
        metrics = m;
        task = runTaskTimerAsynchronously(pl, PING_INTERVAL, PING_INTERVAL);
        return true;
    }

    @Override
    public void run() {
        try {
            if (metrics == null) {
                cancel();
            } else if (metrics.isOptOut()) {
            } else {
                metrics.postPlugin(!firstPost);
                firstPost = false;
            }
        } catch (IOException e) {
        }
    }
}
