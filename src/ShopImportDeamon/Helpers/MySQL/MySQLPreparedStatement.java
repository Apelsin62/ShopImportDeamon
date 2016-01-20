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

import ShopImportDeamon.General.SystemConstants;
import ShopImportDeamon.Helpers.IdGenerator;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Maxim Zaytsev
 */
public class MySQLPreparedStatement {

    private static final MySQLHelper datebase = new MySQLHelper(SystemConstants.getDefaultShopConnectionsName());
    private static final Map<String, PreparedStatement> preparedStatement = new HashMap<>();

    private static void setPreparedStatement(String PreparedStatementKey, String preparedStatementString) {
        preparedStatement.put(PreparedStatementKey, datebase.preparedStatement(preparedStatementString));
    }

    private static PreparedStatement getPreparedStatement(String PreparedStatementKey) {
        return preparedStatement.get(PreparedStatementKey);
    }

    private static PreparedStatement setAndGetPreparedStatement(String PreparedStatementKey) {
        if (MySQLPreparedStatement.getPreparedStatement(PreparedStatementKey) == null) {
            MySQLPreparedStatement.setPreparedStatement(PreparedStatementKey, MySQLStrings.getSQL(PreparedStatementKey));
        }
        return MySQLPreparedStatement.getPreparedStatement(PreparedStatementKey);
    }

    public static String import_ShopImportLogs(Timestamp exportDateTime, String exportType, String directory, String xmlFile, String logFile) {
        PreparedStatement sql = MySQLPreparedStatement.setAndGetPreparedStatement(MySQLStringsKeys.ShopImportLogs_Import);
        String logId = IdGenerator.generageId();
        try {
            sql.setString(1, logId);
            sql.setTimestamp(2, exportDateTime);
            sql.setString(3, exportType);
            sql.setString(4, directory);
            sql.setString(5, xmlFile);
            sql.setString(6, logFile);
            sql.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(MySQLPreparedStatement.class.getName()).log(Level.SEVERE, null, ex);
        }
        return logId;
    }

    public static ResultSet select_ShopImportLogs_LogInfo(String logId) {
        PreparedStatement sql = MySQLPreparedStatement.setAndGetPreparedStatement(MySQLStringsKeys.ShopImportLogsLogInfo_Select);
        try {
            sql.setString(1, logId);
            return sql.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(MySQLPreparedStatement.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public static String import_ShopImportLogs_BadFile(String xmlFile, String logFile, Integer countErrors) {
        PreparedStatement sql = MySQLPreparedStatement.setAndGetPreparedStatement(MySQLStringsKeys.ShopImportLogsBadFile_Import);
        String logId = IdGenerator.generageId();
        try {
            sql.setString(1, logId);
            sql.setInt(2, countErrors);
            sql.setString(3, xmlFile);
            sql.setString(4, logFile);
            sql.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(MySQLPreparedStatement.class.getName()).log(Level.SEVERE, null, ex);
        }
        return logId;
    }

    public static void update_ShopImportLogs_Statistics(String logId, Integer errors, Integer warnings, Integer notices) {
        PreparedStatement sql = MySQLPreparedStatement.setAndGetPreparedStatement(MySQLStringsKeys.ShopImportLogsStatistics_Update);
        try {
            sql.setInt(1, errors);
            sql.setInt(2, warnings);
            sql.setInt(3, notices);
            sql.setString(4, logId);
            sql.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(MySQLPreparedStatement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void update_ShopImportLogs_SetSuccess(String logId) {
        PreparedStatement sql = MySQLPreparedStatement.setAndGetPreparedStatement(MySQLStringsKeys.ShopImportLogsSetSuccess_Update);
        try {
            sql.setString(1, logId);
            sql.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(MySQLPreparedStatement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static ResultSet select_ShopPricesTypes() {
        PreparedStatement sql = MySQLPreparedStatement.setAndGetPreparedStatement(MySQLStringsKeys.ShopPricesTypes_Select);
        try {
            return sql.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(MySQLPreparedStatement.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public static void update_ShopPricesTypes(String id, String typeName_1c, Integer isDefault) {
        PreparedStatement sql = MySQLPreparedStatement.setAndGetPreparedStatement(MySQLStringsKeys.ShopPricesTypes_Update);
        try {
            sql.setString(1, typeName_1c);
            sql.setInt(2, isDefault);
            sql.setString(3, id);
            sql.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(MySQLPreparedStatement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void import_ShopPricesTypes(String id, String typeName_1c, Integer isDefault) {
        PreparedStatement sql = MySQLPreparedStatement.setAndGetPreparedStatement(MySQLStringsKeys.ShopPricesTypes_Import);
        try {
            sql.setString(1, id);
            sql.setString(2, typeName_1c);
            sql.setString(3, typeName_1c);
            sql.setInt(4, isDefault);
            sql.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(MySQLPreparedStatement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void update_ShopPricesTypes_UnsetDefault(String id) {
        PreparedStatement sql = MySQLPreparedStatement.setAndGetPreparedStatement(MySQLStringsKeys.ShopPricesTypesUnsetDefault_Update);
        try {
            sql.setString(1, id);
            sql.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(MySQLPreparedStatement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static ResultSet select_ShopStorages() {
        PreparedStatement sql = MySQLPreparedStatement.setAndGetPreparedStatement(MySQLStringsKeys.ShopStorages_Select);
        try {
            return sql.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(MySQLPreparedStatement.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public static void update_ShopStorages(String id, String storageName_1C) {
        PreparedStatement sql = MySQLPreparedStatement.setAndGetPreparedStatement(MySQLStringsKeys.ShopStorages_Update);
        try {
            sql.setString(1, storageName_1C);
            sql.setString(2, id);
            sql.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(MySQLPreparedStatement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void import_ShopStorages(String id, String storageName_1C) {
        PreparedStatement sql = MySQLPreparedStatement.setAndGetPreparedStatement(MySQLStringsKeys.ShopStorages_Import);
        try {
            sql.setString(1, id);
            sql.setString(2, storageName_1C);
            sql.setString(3, storageName_1C);
            sql.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(MySQLPreparedStatement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static ResultSet select_ShopItems_IdAndStatus() {
        PreparedStatement sql = MySQLPreparedStatement.setAndGetPreparedStatement(MySQLStringsKeys.ShopItemsIdAndStatus_Select);
        try {
            return sql.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(MySQLPreparedStatement.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public static void update_ShopItems(String id, String itemName,
            String article, String directory, String directoryPath,
            String status, String type, String pricePer, String serviceCenter,
            Integer action, Integer shown, Integer toRemove,
            Float totalAmount, Float minAmount) {
        PreparedStatement sql = MySQLPreparedStatement.setAndGetPreparedStatement(MySQLStringsKeys.ShopItems_Update);
        try {
            sql.setString(1, itemName);
            sql.setString(2, article);
            sql.setString(3, directory);
            sql.setString(4, directoryPath);
            sql.setString(5, status);
            sql.setString(6, type);
            sql.setString(7, pricePer);
            sql.setString(8, serviceCenter);
            sql.setInt(9, action);
            sql.setInt(10, shown);
            sql.setInt(11, toRemove);
            sql.setFloat(12, totalAmount);
            sql.setFloat(13, minAmount);
            sql.setString(14, id);
            sql.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(MySQLPreparedStatement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void update_ShopItems_PricesAndAmounts(String id, Float totalAmount, Float minAmount) {
        PreparedStatement sql = MySQLPreparedStatement.setAndGetPreparedStatement(MySQLStringsKeys.ShopItemsPricesAndAmounts_Update);
        try {
            sql.setFloat(1, totalAmount);
            sql.setFloat(2, minAmount);
            sql.setString(3, id);
            sql.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(MySQLPreparedStatement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void import_ShopItems(String id, String itemName,
            String article, String directory, String directoryPath,
            String status, String type, String pricePer, String serviceCenter,
            Integer action, Integer shown, Integer toRemove,
            Float totalAmount, Float minAmount) {
        PreparedStatement sql = MySQLPreparedStatement.setAndGetPreparedStatement(MySQLStringsKeys.ShopItems_Import);
        try {
            sql.setString(1, id);
            sql.setString(2, itemName);
            sql.setString(3, article);
            sql.setString(4, directory);
            sql.setString(5, directoryPath);
            sql.setString(6, status);
            sql.setString(7, type);
            sql.setString(8, pricePer);
            sql.setString(9, serviceCenter);
            sql.setInt(10, action);
            sql.setInt(11, shown);
            sql.setInt(12, toRemove);
            sql.setFloat(13, totalAmount);
            sql.setFloat(14, minAmount);
            sql.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(sql.toString());
            Logger.getLogger(MySQLPreparedStatement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static ResultSet select_ShopItemsAmount(String item, String storage) {
        PreparedStatement sql = MySQLPreparedStatement.setAndGetPreparedStatement(MySQLStringsKeys.ShopItemsAmount_Select);
        try {
            sql.setString(1, item);
            sql.setString(2, storage);
            return sql.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(MySQLPreparedStatement.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public static void update_ShopItemsAmount(String item, String storage, Float value) {
        PreparedStatement sql = MySQLPreparedStatement.setAndGetPreparedStatement(MySQLStringsKeys.ShopItemsAmount_Update);
        try {
            sql.setFloat(1, value);
            sql.setString(2, item);
            sql.setString(3, storage);
            sql.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(MySQLPreparedStatement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void import_ShopItemsAmount(String item, String storage, Float value) {
        PreparedStatement sql = MySQLPreparedStatement.setAndGetPreparedStatement(MySQLStringsKeys.ShopItemsAmount_Import);
        try {
            sql.setString(1, item);
            sql.setString(2, storage);
            sql.setFloat(3, value);
            sql.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(MySQLPreparedStatement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static ResultSet select_ShopItemsPrices(String item, String price) {
        PreparedStatement sql = MySQLPreparedStatement.setAndGetPreparedStatement(MySQLStringsKeys.ShopItemsPrices_Select);
        try {
            sql.setString(1, item);
            sql.setString(2, price);
            return sql.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(MySQLPreparedStatement.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public static void update_ShopItemsPrices(String item, String price, Float value) {
        PreparedStatement sql = MySQLPreparedStatement.setAndGetPreparedStatement(MySQLStringsKeys.ShopItemsPrices_Update);
        try {
            sql.setFloat(1, value);
            sql.setString(2, item);
            sql.setString(3, price);
            sql.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(MySQLPreparedStatement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void import_ShopItemsPrices(String item, String price, Float value) {
        PreparedStatement sql = MySQLPreparedStatement.setAndGetPreparedStatement(MySQLStringsKeys.ShopItemsPrices_Import);
        try {
            sql.setString(1, item);
            sql.setString(2, price);
            sql.setFloat(3, value);
            sql.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(MySQLPreparedStatement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
