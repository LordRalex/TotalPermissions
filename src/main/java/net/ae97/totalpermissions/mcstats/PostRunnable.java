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

/**
 * @author Lord_Ralex
 */
final class PostRunnable extends Thread {

    private boolean firstPost = false;
    private Metrics metrics;
    private final int PING_INTERVAL = 1200 * 15;
    boolean running = false;

    protected PostRunnable() {
    }

    protected void setMetrics(Metrics met) {
        if (metrics == null) {
            metrics = met;
        }
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            try {
                if (metrics == null) {
                    return;
                }
                if (metrics.isOptOut()) {
                    return;
                }
                metrics.postPlugin(!firstPost);
                firstPost = false;
            } catch (IOException e) {
            }
            synchronized (this) {
                try {
                    this.wait(PING_INTERVAL);
                } catch (InterruptedException ex) {
                }
            }
        }
    }
}
