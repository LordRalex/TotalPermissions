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
package net.ae97.totalpermissions.logger;

import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

/**
 * @version 1.0
 * @author Lord_Ralex
 */
public class DebugLogFormatter extends SimpleFormatter {

    @Override
    public synchronized String format(LogRecord record) {
        StringBuilder builder = new StringBuilder();
        builder.append("[").append(record.getLevel()).append("]");
        builder.append(" ");
        builder.append(record.getMessage());
        if (record.getThrown() != null) {
            builder.append(System.lineSeparator());
            Throwable thrown = record.getThrown();
            builder.append(thrown.toString());
            StackTraceElement[] elements = thrown.getStackTrace();
            for (StackTraceElement stack : elements) {
                builder.append(System.lineSeparator());
                builder.append("  at ").append(stack.toString());
            }
        }
        builder.append(System.lineSeparator());
        return builder.toString();
    }
}
