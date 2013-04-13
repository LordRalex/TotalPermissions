/*
 * Copyright (C) 2013 LordRalex
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
package net.ae97.totalpermissions.lang;

import org.bukkit.configuration.file.FileConfiguration;

/**
 * @version 0.2
 * @author 1Rogue
 * @since 0.2
 */
public class Cipher {
    
    FileConfiguration langFile;
    
    public Cipher(FileConfiguration file) {
        langFile = file;
    }
    
    public void setLangFile(FileConfiguration file) {
        langFile = file;
    }
    
    public FileConfiguration getLangFile() {
        return langFile;
    }
    
    public String getString (String path) {
        return getString(path, "", "");
    }
    
    public String getString (String path, String varOne) {
        return getString(path, varOne, "");
    }
    
    public String getString (String path, String varOne, String varTwo) {
        return langFile.getString(path).replace("{0}", varOne).replace("{1}", varTwo);
    }
}