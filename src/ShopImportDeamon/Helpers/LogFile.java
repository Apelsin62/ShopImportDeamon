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
package ShopImportDeamon.Helpers;

import ShopImportDeamon.General.Configuration;
import ShopImportDeamon.General.SystemConstants;
import ShopImportDeamon.ImportData.ImportDataMain;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Maxim Zaytsev
 */
public class LogFile {

    private final Configuration configuration = Configuration.getInstance();
    private final String logFile = IdGenerator.generageId() + ".log.html";
    private final Locale local = new Locale("ru", "RU");
    private final String logFolder = configuration.getFilePathsData(SystemConstants.getFilePathsConfigureKey_LogFolder(), "directory");
    public final String ERROR_TYPE = "ERROR";
    public final String WARNING_TYPE = "WARNING";
    public final String NOTICE_TYPE = "NOTICE";
    public final String MESSAGE_TYPE = "MESSAGE";

    public LogFile(FileFinder.ImportFileInfo importFile) {
        this.writeHeadInLog(importFile);
    }

    public LogFile(String fileName) {
        this.writeHeadInLogBadFile(fileName);
    }

    protected void writeHeadInLogBadFile(String fileName) {
        try (FileWriter writer = new FileWriter(this.logFolder + this.logFile, false)) {
            DateFormat df = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.DEFAULT, local);
            Date importDateTime = new Date();
            writer.write("<!DOCTYPE html>\n"
                    + "<!-- Данный файл сгенерирован утилитой ShopImportDeamon -->\n"
                    + "<html>\n"
                    + "<head>\n"
                    + "    <title>ShopImportDeamonLog</title>\n"
                    + "    <meta charset=\"UTF-8\">\n"
                    + "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
                    + "    <style>\n"
                    + "        body {\n"
                    + "            font: 14px/14px arial, sans-serif;\n"
                    + "        }\n"
                    + "        .ShopImportDeamonLogHeadBlock {\n"
                    + "            margin: 10px;\n"
                    + "            padding: 10px;\n"
                    + "        }\n"
                    + "        .ShopImportDeamonLogStartBlock {\n"
                    + "            margin: 10px;\n"
                    + "            padding: 10px;\n"
                    + "        }\n"
                    + "        .ShopImportDeamonLogElement {\n"
                    + "            margin: 10px;\n"
                    + "            border: 1px solid #626262;\n"
                    + "            background: #EFEFEF;\n"
                    + "            color: #626262;\n"
                    + "            border-radius: 5px;\n"
                    + "        }\n"
                    + "        .ShopImportDeamonLogElement.ERROR {\n"
                    + "            background: #FFE0E0;\n"
                    + "            color: #5F0E0E;\n"
                    + "            border-color: #5F0E0E;\n"
                    + "        }\n"
                    + "        .ShopImportDeamonLogElement.WARNING {\n"
                    + "            background: #FFFFE8;\n"
                    + "            color: #A08F58;\n"
                    + "            border-color: #A08F58;\n"
                    + "        }\n"
                    + "        .ShopImportDeamonLogElement.NOTICE {\n"
                    + "            background: #E2FFE2;\n"
                    + "            color: #013F02;\n"
                    + "            border-color: #013F02;\n"
                    + "        }\n"
                    + "        .ShopImportDeamonLogElement .ShopImportDeamonLogElementTitle {\n"
                    + "            border-radius: 5px 5px 0px 0px;\n"
                    + "            padding: 10px 10px 5px 10px;\n"
                    + "            font: bolder 16px/16px arial, sans-serif;\n"
                    + "        }\n"
                    + "        .ShopImportDeamonLogElement .ShopImportDeamonLogElementMessage {\n"
                    + "            border-radius: 0px 0px 5px 5px;\n"
                    + "            padding: 10px;\n"
                    + "        }\n"
                    + "        \n"
                    + "    </style>\n"
                    + "</head>\n"
                    + "<body>\n");
            writer.write("<div class='ShopImportDeamonLogHeadBlock'>\n");
            writer.write("<div>FILE: " + fileName + "</div>\n");
            writer.write("<div>TYPE: BAD FILE TYPE</div>\n");
            writer.write("<div>IMPORT DATE: " + df.format(importDateTime) + "</div>\n");
            writer.write("</div>\n");
            writer.write("<div class='ShopImportDeamonLogStartBlock'> - ERRORS LOG - </div>\n");
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    protected void writeHeadInLog(FileFinder.ImportFileInfo importFile) {
        try (FileWriter writer = new FileWriter(this.logFolder + this.logFile, false)) {
            DateFormat df = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.DEFAULT, local);
            Date importDateTime = new Date();
            writer.write("<!DOCTYPE html>\n"
                    + "<!-- Данный файл сгенерирован утилитой ShopImportDeamon -->\n"
                    + "<html>\n"
                    + "<head>\n"
                    + "    <title>ShopImportDeamonLog</title>\n"
                    + "    <meta charset=\"UTF-8\">\n"
                    + "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
                    + "    <style>\n"
                    + "        body {\n"
                    + "            font: 14px/14px arial, sans-serif;\n"
                    + "        }\n"
                    + "        .ShopImportDeamonLogHeadBlock {\n"
                    + "            margin: 10px;\n"
                    + "            padding: 10px;\n"
                    + "        }\n"
                    + "        .ShopImportDeamonLogStartBlock {\n"
                    + "            margin: 10px;\n"
                    + "            padding: 10px;\n"
                    + "        }\n"
                    + "        .ShopImportDeamonLogElement {\n"
                    + "            margin: 10px;\n"
                    + "            border: 1px solid #626262;\n"
                    + "            background: #EFEFEF;\n"
                    + "            color: #626262;\n"
                    + "            border-radius: 5px;\n"
                    + "        }\n"
                    + "        .ShopImportDeamonLogElement.ERROR {\n"
                    + "            background: #FFE0E0;\n"
                    + "            color: #5F0E0E;\n"
                    + "            border-color: #5F0E0E;\n"
                    + "        }\n"
                    + "        .ShopImportDeamonLogElement.WARNING {\n"
                    + "            background: #FFFFE8;\n"
                    + "            color: #A08F58;\n"
                    + "            border-color: #A08F58;\n"
                    + "        }\n"
                    + "        .ShopImportDeamonLogElement.NOTICE {\n"
                    + "            background: #E2FFE2;\n"
                    + "            color: #013F02;\n"
                    + "            border-color: #013F02;\n"
                    + "        }\n"
                    + "        .ShopImportDeamonLogElement .ShopImportDeamonLogElementTitle {\n"
                    + "            border-radius: 5px 5px 0px 0px;\n"
                    + "            padding: 10px 10px 5px 10px;\n"
                    + "            font: bolder 16px/16px arial, sans-serif;\n"
                    + "        }\n"
                    + "        .ShopImportDeamonLogElement .ShopImportDeamonLogElementMessage {\n"
                    + "            border-radius: 0px 0px 5px 5px;\n"
                    + "            padding: 10px;\n"
                    + "        }\n"
                    + "        \n"
                    + "    </style>\n"
                    + "</head>\n"
                    + "<body>\n");
            writer.write("<div class='ShopImportDeamonLogHeadBlock'>\n");
            writer.write("<div>FILE: " + importFile.getFileName() + "</div>\n");
            writer.write("<div>TYPE: " + importFile.getExportType() + "</div>\n");
            if (!importFile.getDirectory().equals("")) {
                writer.write("<div>DIRECTORY: " + importFile.getDirectory() + "</div>\n");
            }
            writer.write("<div>IMPORT DATE: " + df.format(importDateTime) + "</div>\n");
            try {
                String xmlDateString = importFile.getYear() + "-" + importFile.getMonth() + "-" + importFile.getDay() + " " + importFile.getHours() + ":" + importFile.getMinutes() + ":" + importFile.getSeconds();
                SimpleDateFormat dateBaseDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date exportDateTime = dateBaseDateFormat.parse(xmlDateString);
                writer.write("<div>EXPORT DATE: " + df.format(exportDateTime) + "</div>\n");
            } catch (ParseException ex) {
                Logger.getLogger(ImportDataMain.class.getName()).log(Level.SEVERE, null, ex);
            }
            writer.write("</div>\n");
            writer.write("<div class='ShopImportDeamonLogStartBlock'> - START IMPORT - </div>\n");
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void writeInLog(String type, String message) {
        try (FileWriter writer = new FileWriter(this.logFolder + this.logFile, true)) {
            DateFormat df = DateFormat.getTimeInstance(DateFormat.DEFAULT, local);
            Date currentTime = new Date();
            writer.write("<div class='ShopImportDeamonLogElement " + type + "'>\n");
            writer.write("<div class='ShopImportDeamonLogElementTitle'>\n" + type + " - " + df.format(currentTime) + "\n</div>\n");
            writer.write("<div class='ShopImportDeamonLogElementMessage'>\n" + message + "\n</div>\n");
            writer.write("</div>\n");
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void writeInLog(String message) {
        try (FileWriter writer = new FileWriter(this.logFolder + this.logFile, true)) {
            writer.write("<div class='ShopImportDeamonLogElement'>\n");
            writer.write("<div class='ShopImportDeamonLogElementMessage'>\n" + message + "\n</div>\n");
            writer.write("</div>\n");
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void writeEndFile() {
        try (FileWriter writer = new FileWriter(this.logFolder + this.logFile, true)) {
            DateFormat df = DateFormat.getTimeInstance(DateFormat.DEFAULT, local);
            Date currentTime = new Date();
            writer.write("<div class='ShopImportDeamonLogStartBlock'> - END " + df.format(currentTime) + " - </div>\n");
            writer.write("</body>\n");
            writer.write("</html>\n");
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public String getLogFile() {
        return this.logFile;
    }

    public String getLogFolder() {
        return this.logFolder;
    }

}
