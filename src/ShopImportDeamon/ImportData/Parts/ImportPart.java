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
package ShopImportDeamon.ImportData.Parts;

import ShopImportDeamon.Helpers.LogFile;
import ShopImportDeamon.Helpers.MySQL.MySQLPreparedStatement;
import ShopImportDeamon.ImportData.ImportDataHelper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Node;

/**
 *
 * @author Maxim Zaytsev
 */
public class ImportPart {

    protected final Node rootNode;
    protected final Node thisNode;
    protected final LogFile logFile;
    protected final String logId;
    protected final String exportType;
    protected final ImportDataHelper importDataHelper;
    private final Map<String, Integer> logInfo = new HashMap<>();
    private final Map<String, Integer> globalLogInfo = new HashMap<>();

    public ImportPart(Node rootNode, LogFile logFile, String logId, String exportType) {
        importDataHelper = ImportDataHelper.getInstance();
        this.rootNode = rootNode;
        this.logFile = logFile;
        this.logId = logId;
        this.exportType = exportType;
        this.thisNode = this.getThisNode();
        this.getGlobalLogInfo();
        this.preprocessing();
        this.updateGlobalLogInfo();
    }

    private void updateGlobalLogInfo() {
        MySQLPreparedStatement.update_ShopImportLogs_Statistics(
                this.logId,
                (this.getLogInfo(this.logFile.ERROR_TYPE) + this.globalLogInfo.get(this.logFile.ERROR_TYPE)),
                (this.getLogInfo(this.logFile.WARNING_TYPE) + this.globalLogInfo.get(this.logFile.WARNING_TYPE)),
                (this.getLogInfo(this.logFile.NOTICE_TYPE) + this.globalLogInfo.get(this.logFile.NOTICE_TYPE)));
    }

    private void getGlobalLogInfo() {
        ResultSet rs = MySQLPreparedStatement.select_ShopImportLogs_LogInfo(this.logId);
        try {
            while (rs.next()) {
                Integer val_errors = rs.getInt("errors");
                Integer val_warnings = rs.getInt("warnings");
                Integer val_notices = rs.getInt("notices");
                globalLogInfo.put(this.logFile.ERROR_TYPE, val_errors);
                globalLogInfo.put(this.logFile.WARNING_TYPE, val_warnings);
                globalLogInfo.put(this.logFile.NOTICE_TYPE, val_notices);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ImportPart.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void inLogInfo(String type) {
        if (this.logInfo.get(type) == null) {
            this.logInfo.put(type, 1);
        } else {
            this.logInfo.put(type, this.logInfo.get(type) + 1);
        }
    }

    protected void writeInLog(String type, String message) {
        this.inLogInfo(type);
        this.logFile.writeInLog(type, message);
    }

    protected void writeInLog(String message) {
        this.logFile.writeInLog(message);
    }

    protected Integer getLogInfo(String type) {
        if (this.logInfo.get(type) == null) {
            return 0;
        } else {
            return this.logInfo.get(type);
        }
    }

    public void startExecution() {
        if ((this.getLogInfo(this.logFile.ERROR_TYPE)
                + this.globalLogInfo.get(this.logFile.ERROR_TYPE)) < 1) {
            this.execution();
        }
    }

    protected Node getThisNode() {
        return this.rootNode;
    }

    protected void preprocessing() {
    }

    protected void execution() {
    }

}
