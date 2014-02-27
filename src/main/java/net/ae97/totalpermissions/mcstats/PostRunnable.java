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
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author Lord_Ralex
 */
public final class PostRunnable extends BukkitRunnable {

    private boolean firstPost = false;
    private Metrics metrics;

    protected void setMetrics(Metrics met) {
        if (metrics == null) {
            metrics = met;
        }
    }

    @Override
    public void run() {
        try {
            if (metrics == null) {
                this.cancel();
            }
            if (metrics.isOptOut()) {
            }
            metrics.postPlugin(!firstPost);
            firstPost = false;
        } catch (IOException e) {
        }
    }

    public boolean isScheduled() {
        try {
            return this.getTaskId() != -1;
        } catch (IllegalStateException e) {
            return false;
        }
    }
}
