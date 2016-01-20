/*
 * НЕ ИЗМЕНЯТЬ И НЕ УДАЛЯТЬ АВТОРСКИЕ ПРАВА И ЗАГОЛОВОК ФАЙЛА
 * 
 * Копирайт © 2010-2016, CompuProject и/или дочерние компании.
 * Все права защищены.
 * 
 * ShopImportDeamon это программное обеспечение предоставленное и разработанное 
 * CompuProject в рамках проекта ApelsinShop без каких либо сторонних изменений.
 * 
 * Распространение, использование исходного кода в любой форме и/или его 
 * модификация разрешается при условии, что выполняются следующие условия:
 * 
 * 1. При распространении исходного кода должно оставатсья указанное выше 
 *    уведомление об авторских правах, этот список условий и последующий 
 *    отказ от гарантий.
 * 
 * 2. При изменении исходного кода должно оставатсья указанное выше 
 *    уведомление об авторских правах, этот список условий, последующий 
 *    отказ от гарантий и пометка о сделанных изменениях.
 * 
 * 3. Распространение и/или изменение исходного кода должно происходить
 *    на условиях Стандартной общественной лицензии GNU в том виде, в каком 
 *    она была опубликована Фондом свободного программного обеспечения;
 *    либо лицензии версии 3, либо (по вашему выбору) любой более поздней
 *    версии. Вы должны были получить копию Стандартной общественной 
 *    лицензии GNU вместе с этой программой. Если это не так, см. 
 *    <http://www.gnu.org/licenses/>.
 * 
 * ShopImportDeamon распространяется в надежде, что она будет полезной,
 * но БЕЗО ВСЯКИХ ГАРАНТИЙ; даже без неявной гарантии ТОВАРНОГО ВИДА
 * или ПРИГОДНОСТИ ДЛЯ ОПРЕДЕЛЕННЫХ ЦЕЛЕЙ. Подробнее см. в Стандартной
 * общественной лицензии GNU.
 * 
 * НИ ПРИ КАКИХ УСЛОВИЯХ ПРОЕКТ, ЕГО УЧАСТНИКИ ИЛИ CompuProject НЕ 
 * НЕСУТ ОТВЕТСТВЕННОСТИ ЗА КАКИЕ ЛИБО ПРЯМЫЕ, КОСВЕННЫЕ, СЛУЧАЙНЫЕ, 
 * ОСОБЫЕ, ШТРАФНЫЕ ИЛИ КАКИЕ ЛИБО ДРУГИЕ УБЫТКИ (ВКЛЮЧАЯ, НО НЕ 
 * ОГРАНИЧИВАЯСЬ ПРИОБРЕТЕНИЕМ ИЛИ ЗАМЕНОЙ ТОВАРОВ И УСЛУГ; ПОТЕРЕЙ 
 * ДАННЫХ ИЛИ ПРИБЫЛИ; ПРИОСТАНОВЛЕНИЕ БИЗНЕСА). 
 * 
 * ИСПОЛЬЗОВАНИЕ ДАННОГО ИСХОДНОГО КОДА ОЗНАЧАЕТ, ЧТО ВЫ БЫЛИ ОЗНАКОЛМЛЕНЫ
 * СО ВСЕМИ ПРАВАМИ, СТАНДАРТАМИ И УСЛОВИЯМИ, УКАЗАННЫМИ ВЫШЕ, СОГЛАСНЫ С НИМИ
 * И ОБЯЗУЕТЕСЬ ИХ СОБЛЮДАТЬ.
 * 
 * ЕСЛИ ВЫ НЕ СОГЛАСНЫ С ВЫШЕУКАЗАННЫМИ ПРАВАМИ, СТАНДАРТАМИ И УСЛОВИЯМИ, 
 * ТО ВЫ МОЖЕТЕ ОТКАЗАТЬСЯ ОТ ИСПОЛЬЗОВАНИЯ ДАННОГО ИСХОДНОГО КОДА.
 * 
 */
package ShopImportDeamon.Helpers.MySQL;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Maxim Zaytsev
 */
public class MySQLStrings {

    private static final Map<String, String> sql = new HashMap<>();
    private static final String groupIdForNewItems = "SYSTEM_GROUP_FOR_NEW_ITEMS";
    private static Boolean neadSet = true;

    private static void setAll() {
        neadSet = false;
        // ShopImportLogs
        MySQLStrings.setShopImportLogs_Import();
        MySQLStrings.setShopImportLogsLogInfo_Select();
        MySQLStrings.setShopImportLogsBadFile_Import();
        MySQLStrings.setShopImportLogsStatistics_Update();
        MySQLStrings.setShopImportLogsSetSuccess_Update();
        // ShopPricesTypes
        MySQLStrings.setShopPricesTypes_Select();
        MySQLStrings.setShopPricesTypes_Update();
        MySQLStrings.setShopPricesTypes_Import();
        MySQLStrings.setShopPricesTypesUnsetDefault_Update();
        // ShopStorages
        MySQLStrings.setShopStorages_Select();
        MySQLStrings.setShopStorages_Update();
        MySQLStrings.setShopStorages_Import();
        // ShopItems
        MySQLStrings.setShopItemsIdAndStatus_Select();
        MySQLStrings.setShopItems_Update();
        MySQLStrings.setShopItemsPricesAndAmounts_Update();
        MySQLStrings.setShopItems_Import();
        // ShopItemsAmount
        MySQLStrings.setShopItemsAmount_Select();
        MySQLStrings.setShopItemsAmount_Update();
        MySQLStrings.setShopItemsAmount_Import();
        // ShopItemsPrices
        MySQLStrings.setShopItemsPrices_Select();
        MySQLStrings.setShopItemsPrices_Update();
        MySQLStrings.setShopItemsPrices_Import();
    }

    private static void setShopImportLogs_Import() {
        sql.put(MySQLStringsKeys.ShopImportLogs_Import,
                "INSERT INTO `ShopImportLogs`("
                + "`logId`, "
                + "`importDateTime`, "
                + "`exportDateTime`, "
                + "`exportType`, "
                + "`directory`, "
                + "`success`, "
                + "`errors`, "
                + "`warnings`, "
                + "`notices`, "
                + "`xmlFile`, "
                + "`logFile`) "
                + "VALUES "
                + "(?,now(),?,?,?,'0','0','0','0',?,?);");
    }

    private static void setShopImportLogsLogInfo_Select() {
        sql.put(MySQLStringsKeys.ShopImportLogsLogInfo_Select,
                "SELECT `errors`, `warnings`, `notices` "
                + "FROM `ShopImportLogs` WHERE `logId`=?;");
    }

    private static void setShopImportLogsBadFile_Import() {
        sql.put(MySQLStringsKeys.ShopImportLogsBadFile_Import,
                "INSERT INTO `ShopImportLogs`("
                + "`logId`, "
                + "`importDateTime`, "
                + "`exportDateTime`, "
                + "`exportType`, "
                + "`success`, "
                + "`errors`, "
                + "`warnings`, "
                + "`notices`, "
                + "`xmlFile`, "
                + "`logFile`) "
                + "VALUES "
                + "(?,now(),now(),'BadFile','0',?,'0','0',?,?);");
    }

    private static void setShopImportLogsStatistics_Update() {
        sql.put(MySQLStringsKeys.ShopImportLogsStatistics_Update,
                "UPDATE `ShopImportLogs` SET "
                + "`errors`=?,"
                + "`warnings`=?,"
                + "`notices`=? "
                + "WHERE `logId`=?;");
    }

    private static void setShopImportLogsSetSuccess_Update() {
        sql.put(MySQLStringsKeys.ShopImportLogsSetSuccess_Update,
                "UPDATE `ShopImportLogs` SET "
                + "`success`='1'"
                + "WHERE `logId`=?;");
    }

    private static void setShopPricesTypes_Select() {
        sql.put(MySQLStringsKeys.ShopPricesTypes_Select,
                "SELECT `id`, `typeName`, `typeName_1c`, `default` "
                + "FROM `ShopPricesTypes`;");
    }

    private static void setShopPricesTypes_Update() {
        sql.put(MySQLStringsKeys.ShopPricesTypes_Update,
                "UPDATE `ShopPricesTypes` SET "
                + "`typeName_1c`=?,"
                + "`default`=? "
                + "WHERE `id`=?;");
    }

    private static void setShopPricesTypes_Import() {
        sql.put(MySQLStringsKeys.ShopPricesTypes_Import,
                "INSERT INTO `ShopPricesTypes`("
                + "`id`, "
                + "`typeName`, "
                + "`typeName_1c`, "
                + "`default`) "
                + "VALUES "
                + "(?,?,?,?);");
    }

    private static void setShopPricesTypesUnsetDefault_Update() {
        sql.put(MySQLStringsKeys.ShopPricesTypesUnsetDefault_Update,
                "UPDATE `ShopPricesTypes` SET "
                + "`default`='0' "
                + "WHERE `id`=?;");
    }

    private static void setShopStorages_Select() {
        sql.put(MySQLStringsKeys.ShopStorages_Select,
                "SELECT `id`, `storageName`, `storageName_1C` "
                + "FROM `ShopStorages`;");
    }

    private static void setShopStorages_Update() {
        sql.put(MySQLStringsKeys.ShopStorages_Update,
                "UPDATE `ShopStorages` SET "
                + "`storageName_1C`=?"
                + "WHERE `id`=?;");
    }

    private static void setShopStorages_Import() {
        sql.put(MySQLStringsKeys.ShopStorages_Import,
                "INSERT INTO `ShopStorages`("
                + "`id`, "
                + "`storageName`, "
                + "`storageName_1C`) "
                + "VALUES "
                + "(?,?,?);");
    }

    private static void setShopItemsIdAndStatus_Select() {
        sql.put(MySQLStringsKeys.ShopItemsIdAndStatus_Select,
                "SELECT `id`,`status` FROM `ShopItems`;");
    }

    private static void setShopItems_Update() {
        sql.put(MySQLStringsKeys.ShopItems_Update,
                "UPDATE `ShopItems` SET "
                + "`itemName`=?,"
                + "`article`=?,"
                + "`directory`=?,"
                + "`directoryPath`=?,"
                + "`status`=?,"
                + "`type`=?,"
                + "`pricePer`=?,"
                + "`serviceCenter`=?,"
                + "`action`=?,"
                + "`shown`=?,"
                + "`toRemove`=?,"
                + "`totalAmount`=?,"
                + "`minAmount`=?"
                + "WHERE `id`=?;");
    }

    private static void setShopItemsPricesAndAmounts_Update() {
        sql.put(MySQLStringsKeys.ShopItemsPricesAndAmounts_Update,
                "UPDATE `ShopItems` SET "
                + "`totalAmount`=?,"
                + "`minAmount`=?"
                + "WHERE `id`=?;");
    }

    private static void setShopItems_Import() {
        sql.put(MySQLStringsKeys.ShopItems_Import,
                "INSERT INTO `ShopItems`("
                + "`id`, "
                + "`itemName`, "
                + "`article`, "
                + "`directory`, "
                + "`directoryPath`, "
                + "`status`, "
                + "`type`, "
                + "`pricePer`, "
                + "`serviceCenter`, "
                + "`action`, "
                + "`shown`, "
                + "`toRemove`, "
                + "`totalAmount`, "
                + "`minAmount`, "
                + "`group`) "
                + "VALUES "
                + "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,'" + MySQLStrings.groupIdForNewItems + "');");
    }

    private static void setShopItemsAmount_Select() {
        sql.put(MySQLStringsKeys.ShopItemsAmount_Select,
                "SELECT `value` FROM `ShopItemsAmount` "
                + "WHERE `item`=? AND `storage`=?;");
    }

    private static void setShopItemsAmount_Update() {
        sql.put(MySQLStringsKeys.ShopItemsAmount_Update,
                "UPDATE `ShopItemsAmount` SET `value`=? "
                + "WHERE `item`=? AND `storage`=?;");
    }

    private static void setShopItemsAmount_Import() {
        sql.put(MySQLStringsKeys.ShopItemsAmount_Import,
                "INSERT INTO `ShopItemsAmount`(`item`, `storage`, `value`) "
                + "VALUES (?,?,?)");
    }

    private static void setShopItemsPrices_Select() {
        sql.put(MySQLStringsKeys.ShopItemsPrices_Select,
                "SELECT `value` FROM `ShopItemsPrices` "
                + "WHERE `item`=? AND `price`=?;");
    }

    private static void setShopItemsPrices_Update() {
        sql.put(MySQLStringsKeys.ShopItemsPrices_Update,
                "UPDATE `ShopItemsPrices` SET `value`=? "
                + "WHERE `item`=? AND `price`=?;");
    }

    private static void setShopItemsPrices_Import() {
        sql.put(MySQLStringsKeys.ShopItemsPrices_Import,
                "INSERT INTO `ShopItemsPrices`(`item`, `price`, `value`) "
                + "VALUES (?,?,?)");
    }

    public static String getSQL(String key) {
        if (neadSet) {
            MySQLStrings.setAll();
        }
        return sql.get(key);
    }

}
