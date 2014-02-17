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
package net.ae97.totalpermissions.lang;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * @author Lord_Ralex
 * @deprecated The idea of a language system built like this is awful
 *             This will be deprecated until it is either determined
 *             to be the best way to handle this, or a new system is
 *             built.
 */
public enum Lang {

    COMMAND_ACTION_SUBACTION_SAVEERROR1(),
    COMMAND_ACTION_SUBACTION_SAVEERROR2(),
    COMMAND_ACTION_SUBACTION_SAVEERROR3(),
    COMMAND_ACTION_HANDLER_IFNULL(),
    COMMAND_ACTION_HANDLER_INVALID(),
    COMMAND_ACTION_HANDLER_EXECFIELDS(),
    COMMAND_ACTION_HANDLER_EDITFIELDS(),
    COMMAND_ACTION_HANDLER_NOTSUPPORTED(),
    COMMAND_ACTION_HANDLER_DENIED(),
    COMMAND_ACTION_HANDLER_USAGE(),
    COMMAND_ACTION_ADD_PERMISSIONS(),
    COMMAND_ACTION_ADD_INHERITANCE(),
    COMMAND_ACTION_ADD_COMMANDS(),
    COMMAND_ACTION_ADD_GROUPS(),
    COMMAND_ACTION_ADD_HELP(),
    COMMAND_ACTION_CHECK_PERMISSIONS_HAS(),
    COMMAND_ACTION_CHECK_PERMISSIONS_NOT(),
    COMMAND_ACTION_CHECK_INHERITANCE_HAS(),
    COMMAND_ACTION_CHECK_INHERITANCE_NOT(),
    COMMAND_ACTION_CHECK_COMMANDS_HAS(),
    COMMAND_ACTION_CHECK_COMMANDS_NOT(),
    COMMAND_ACTION_CHECK_GROUPS_HAS(),
    COMMAND_ACTION_CHECK_GROUPS_NOT(),
    COMMAND_ACTION_CHECK_HELP(),
    COMMAND_ACTION_REMOVE_PERMISSIONS(),
    COMMAND_ACTION_REMOVE_INHERITANCE(),
    COMMAND_ACTION_REMOVE_COMMANDS(),
    COMMAND_ACTION_REMOVE_GROUPS(),
    COMMAND_ACTION_REMOVE_PREFIX(),
    COMMAND_ACTION_REMOVE_SUFFIX(),
    COMMAND_ACTION_REMOVE_HELP(),
    COMMAND_ACTION_LIST_PERMISSIONS(),
    COMMAND_ACTION_LIST_INHERITANCE(),
    COMMAND_ACTION_LIST_COMMANDS(),
    COMMAND_ACTION_LIST_GROUPS(),
    COMMAND_ACTION_LIST_PREFIX(),
    COMMAND_ACTION_LIST_SUFFIX(),
    COMMAND_ACTION_LIST_HELP(),
    COMMAND_ACTION_SET_DEFAULT(),
    COMMAND_ACTION_SET_PREFIX(),
    COMMAND_ACTION_SET_SUFFIX(),
    COMMAND_ACTION_SET_HELP(),
    COMMAND_BACKUP_COMMAND(),
    COMMAND_BACKUP_HELP(),
    COMMAND_DEBUG_COMMAND(),
    COMMAND_DEBUG_ARGS(),
    COMMAND_DEBUG_ARGSPLAIN("command.debug.args-plain"),
    COMMAND_DEBUG_NULLTARGET("command.debug.null-target"),
    COMMAND_DEBUG_DEBUGON("command.debug.debug-on"),
    COMMAND_DEBUG_DEBUGOFF("command.debug.debug-off"),
    COMMAND_DEBUG_HELP(),
    COMMAND_DUMP_HELP(),
    COMMAND_DUMP_PAGE(),
    COMMAND_DUMP_TITLE(),
    COMMAND_HELP_COMMAND(),
    COMMAND_HELP_ACTIONHELP("command.help.action-help"),
    COMMAND_HELP_PAGE(),
    COMMAND_HELP_CONT(),
    COMMAND_HELP_CONTPLAIN("command.help.cont-plain"),
    COMMAND_HELP_HELP(),
    COMMAND_RELOAD_COMMAND(),
    COMMAND_RELOAD_RELOADING(),
    COMMAND_RELOAD_BADCONFIG(),
    COMMAND_RELOAD_GENERAL(),
    COMMAND_RELOAD_SUCCESS(),
    COMMAND_RELOAD_HELP(),
    COMMAND_HANDLER_IFNULL(),
    COMMAND_HANDLER_IFNULLPLAIN("command.handler.ifnull-plain"),
    COMMAND_HANDLER_USAGE(),
    COMMAND_HANDLER_HELPCHECK("command.handler.help-check"),
    COMMAND_HANDLER_DENIED(),
    COMMAND_USER_NONPLAYER("command.user.non-player"),
    COMMAND_USER_GROUPS(),
    COMMAND_USER_PLAYER(),
    COMMAND_USER_DEBUG(),
    COMMAND_USER_HELP(),
    COMMAND_GROUP_GROUP(),
    COMMAND_GROUP_INHERITS(),
    COMMAND_GROUP_PREFIX(),
    COMMAND_GROUP_SUFFIX(),
    COMMAND_GROUP_LIST(),
    COMMAND_GROUP_HELP(),
    COMMAND_SPECIAL_HELP(),
    COMMAND_WORLD_HELP(),
    CONFIGURATION_CONFIGURATION_STRICTMODE("configuration.configuration.strict-mode"),
    CONFIGURATION_CONFIGURATION_REFLECTION_STAR(),
    CONFIGURATION_CONFIGURATION_REFLECTION_DEBUG(),
    CONFIGURATION_CONFIGURATION_UPDATECHECK("configuration.configuration.update-check"),
    CONFIGURATION_CONFIGURATION_ANGRYDEBUG("configuration.configuration.angry-debug"),
    LANGUAGE_LANG_NULLRESC(),
    LANGUAGE_LANG_DEFAULT(),
    LISTENER_TPLISTENER_LOGIN_HOOKED(),
    LISTENER_TPLISTENER_LOGIN_ERROR(),
    LISTENER_TPLISTENER_PREPROCESS_ACTIVATE(),
    LISTENER_TPLISTENER_PREPROCESS_ALLOW(),
    LISTENER_TPLISTENER_PREPROCESS_DENY(),
    LISTENER_TPLISTENER_PREPROCESS_INVALID(),
    LISTENER_TPLISTENER_PREPROCESS_INDEX(),
    PERMISSION_BASE_ADDING(),
    PERMISSION_BASE_ADD(),
    PERMISSION_GROUP_DEFAULT(),
    REFLECTION_CHECKING(),
    MANAGER_NULLGROUP("manager.null-group"),
    MANAGER_NULLDEFAULT("manager.null-default"),
    UPDATE_DEV(),
    UPDATE_UPDATEERROR("update.update-error"),
    UPDATE_VERSIONINGERROR("update.versioning-error"),
    UPDATE_DOWNLOADENABLED("update.download-enabled"),
    UPDATE_DOWNLOADING(),
    UPDATE_DOWNLOADCOMPLETE("update.download-complete"),
    UPDATE_BADNAME("update.bad-name"),
    UPDATE_ISUPDATE(),
    MAIN_INIT(),
    MAIN_INITDONE("main.init-done"),
    MAIN_BADVERSION1("main.bad-version1"),
    MAIN_BADVERSION2("main.bad-version2"),
    MAIN_YAMLERROR("main.yaml-error"),
    MAIN_LOADBACKUP("main.load-backup"),
    MAIN_LOADED1(),
    MAIN_LOADED2(),
    MAIN_LOADFAILED1("main.load-failed1"),
    MAIN_LOADFAILED2("main.load-failed2"),
    MAIN_CREATE_PERMS(),
    MAIN_CREATE_LISTENER(),
    MAIN_CREATE_COMMAND(),
    MAIN_METRICS(),
    MAIN_METRICSOFF("main.metrics-off"),
    MAIN_ERROR(),
    MAIN_LOADCRASH(),
    MAIN_STORAGEERROR("main.storage-error"),
    VARIABLES_COMMAND(),
    VARIABLES_USERNAME(),
    VARIABLES_GROUP(),
    VARIABLES_FIELD(),
    VARIABLES_PERMISSION(),
    VARIABLES_NODE(),
    VARIABLES_VALUE(),
    VARIABLES_WORLD(),
    VARIABLES_COMMANDOPTIONAL("variables.command-optional"),
    VARIABLES_USERNAMEOPTIONAL("variables.username-optional"),
    VARIABLES_GROUPOPTIONAL("variables.group-optional"),
    VARIABLES_FIELDOPTIONAL("variables.field-optional"),
    VARIABLES_PERMISSIONOPTIONAL("variables.permission-optional"),
    VARIABLES_NODEOPTIONAL("variables.node-optional"),
    VARIABLES_VALUEOPTIONAL("variables.value-optional"),
    VARIABLES_WORLDOPTIONAL("variables.world-optional"),
    ERROR_GENERIC(),
    ERROR_CREATION(),
    ERROR_CONFIG(),
    MERGE_START(),
    MERGE_MYSQLTO("merge.mysql-to"),
    MERGE_MYSQLFROM("merge.mysql-from"),
    MERGE_COMPLETE(),
    DATAHOLDER_MYSQL_IMPORT(),
    DATAHOLDER_MYSQL_IMPORTINVALID("dataholder.mysql.import-invalid"),
    DATAHOLDER_MYSQL_IMPORTCOMPLETE("dataholder.mysql.import-complete");
    private static FileConfiguration file;

    public static void setLanguageConfig(FileConfiguration f) {
        file = f;
    }

    public static Lang getLang(String p) {
        for (Lang lang : Lang.values()) {
            if (lang.path.equalsIgnoreCase(p)) {
                return lang;
            }
        }
        return null;
    }

    private final String path;

    private Lang() {
        path = name().toLowerCase().replace("_", ".");
    }

    private Lang(String p) {
        path = p;
    }

    @Override
    public String toString() {
        return path + ": " + getMessage();
    }

    public String getMessage(Object... args) {
        String message = ChatColor.translateAlternateColorCodes('&', file.getString(path, path));
        for (int i = 0; i < args.length; i++) {
            message = message.replace("{" + i + "}", args[i].toString());
        }
        return message;
    }
}
