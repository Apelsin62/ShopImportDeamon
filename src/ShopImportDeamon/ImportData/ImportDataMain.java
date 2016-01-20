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
package ShopImportDeamon.ImportData;

import ShopImportDeamon.General.Configuration;
import ShopImportDeamon.General.SystemConstants;
import ShopImportDeamon.Helpers.FileFinder;
import ShopImportDeamon.Helpers.ImportDocument;
import ShopImportDeamon.Helpers.LogFile;
import ShopImportDeamon.Helpers.MainLogFile;
import ShopImportDeamon.Helpers.MySQL.MySQLPreparedStatement;
import ShopImportDeamon.Helpers.XMLHelper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 *
 * @author Maxim Zaytsev
 */
public class ImportDataMain {

    private final Configuration configuration = Configuration.getInstance();
    private final String usedFolder = configuration.getFilePathsData(SystemConstants.getFilePathsConfigureKey_UsedFolder(), "directory");
    private final String badFolder = configuration.getFilePathsData(SystemConstants.getFilePathsConfigureKey_BadFolder(), "directory");
    private final LogFile logFile;
    private final String logId;
    protected final FileFinder.ImportFileInfo importFile;
    protected final Node rootNode;
    protected final ArrayList<String> errors = new ArrayList<>();
    protected final MainLogFile log = MainLogFile.getInstance();

    public ImportDataMain(FileFinder.ImportFileInfo importFile) {
        this.importFile = importFile;
        Document doc = XMLHelper.getXMLDOC(this.importFile.getFilePath());
        this.rootNode = doc.getElementsByTagName(ImportDocument.getTagName()).item(0);
        this.logFile = new LogFile(importFile);
        this.logId = MySQLPreparedStatement.import_ShopImportLogs(
                importFile.getTimestamp(),
                importFile.getExportType(),
                importFile.getDirectory(),
                importFile.getFileName(),
                this.logFile.getLogFile());
    }

    protected void moveUsedFile() {
        FileFinder.moveFile(this.importFile.getFile(), this.usedFolder);
    }

    protected void moveBadFile() {
        FileFinder.moveFile(this.importFile.getFile(), this.badFolder);
    }

    protected void writeInLog(String type, String message) {
        this.logFile.writeInLog(type, message);
    }

    protected void writeInLog(String message) {
        this.logFile.writeInLog(message);
    }

    protected LogFile getLogFile() {
        return this.logFile;
    }

    protected String getLogId() {
        return this.logId;
    }

    protected Boolean noErrors() {
        Integer val_errors = 0;
        ResultSet rs = MySQLPreparedStatement.select_ShopImportLogs_LogInfo(this.getLogId());
        try {
            while (rs.next()) {
                val_errors = rs.getInt("errors");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ImportDataMain.class.getName()).log(Level.SEVERE, null, ex);
        }
        return val_errors < 1;
    }

    public ArrayList<String> getErrors() {
        return errors;
    }

    public void endExecution() {
        this.logFile.writeEndFile();
        this.moveUsedFile();
    }

    public void execution() {
    }
}
