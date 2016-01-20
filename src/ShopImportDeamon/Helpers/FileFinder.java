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
import ShopImportDeamon.Helpers.MySQL.MySQLPreparedStatement;
import ShopImportDeamon.ShopImportDeamon;
import java.io.File;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Maxim Zaytsev
 */
public class FileFinder {

//    private final ArrayList<String> errors = new ArrayList<>();
    private final Map<String, ArrayList<String>> errors = new HashMap<>();
    private final ArrayList<ImportFileInfo> importFileInfo = new ArrayList<>();
    private final ArrayList<ImportFileInfo> filesForImport = new ArrayList<>();
    private final Configuration configuration = Configuration.getInstance();
    private final String watchedFolder = configuration.getFilePathsData(SystemConstants.getFilePathsConfigureKey_WatchedFolder(), "directory");
    private final String badFolder = configuration.getFilePathsData(SystemConstants.getFilePathsConfigureKey_BadFolder(), "directory");
    private final ArrayList<File> badFiles = new ArrayList<>();
    private final MainLogFile log = MainLogFile.getInstance();

    public FileFinder() {
        log.writeInLog("Поиск файлов для импорта.");
        File[] fileListTemp;
        ArrayList<File> fileList = new ArrayList<>();
        File folder = new File(this.watchedFolder);
        fileListTemp = folder.listFiles();
        for (File file : fileListTemp) {
            if (file.isFile() && "xml".equals(getFileExtention(file.getName()).toLowerCase())) {
                fileList.add(file);
            } else {
                this.putError(file.getName(), "SCANNING ERROR: Файл не был опознан");
                this.thisFileIsBad(file);
            }
        }
        this.getFileInfo(fileList);

    }

    private void putError(String fileName, String error) {
        ArrayList<String> fileErrors = this.errors.get(fileName);
        if (fileErrors == null) {
            fileErrors = new ArrayList<>();
        }
        fileErrors.add(error);
        this.errors.put(fileName, fileErrors);
    }

    private void putErrors(String fileName, ArrayList<String> newErrors) {
        ArrayList<String> fileErrors = this.errors.get(fileName);
        if (fileErrors == null) {
            fileErrors = new ArrayList<>();
        }
        fileErrors.addAll(newErrors);
        this.errors.put(fileName, fileErrors);
    }

    private void thisFileIsBad(File file) {
        if (!badFiles.contains(file)) {
            badFiles.add(file);
        }
    }

    private static String getFileExtention(String filename) {
        int dotPos = filename.lastIndexOf(".") + 1;
        return filename.substring(dotPos);
    }

    private void getFileInfo(ArrayList<File> fileList) {
        for (File file : fileList) {
            this.getSystemInformation(file);
        }
        this.findAllFilesForImport();
        this.moveBadFiles();
        if(this.filesForImport.size() > 0) {
            log.writeInLog(log.WARNING_TYPE, "Найдены файлы для импорта в количестве " + this.filesForImport.size() + "шт.");
        } else {
            log.writeInLog(log.NOTICE_TYPE, "Не найдено ни одного файла для импорта данных.");
        }
    }

    private void moveBadFiles() {
        if(this.badFiles.size() > 0) {
            log.writeInLog(log.WARNING_TYPE, "Найдены битые файлы в количестве " + this.badFiles.size() + "шт.");
        }
        for (File file : this.badFiles) {
            log.writeInLog("Перемещаю битый фал \"" + file.getName() + "\" в \"" + this.badFolder + "\"");
            LogFile logFile = new LogFile(file.getName());
            ArrayList<String> fileErrors = this.errors.get(file.getName());
            Integer countErrors = 0;
            if (fileErrors != null) {
                for (String fileError : fileErrors) {
                    logFile.writeInLog(fileError);
                }
                countErrors = fileErrors.size();
            }
            MySQLPreparedStatement.import_ShopImportLogs_BadFile(file.getName(), logFile.getLogFile(), countErrors);
            FileFinder.moveFile(file, this.badFolder);
        }
    }

    private void findAllFilesForImport() {
        Collections.sort(this.importFileInfo);
        this.filesForImport.addAll(this.importFileInfo);
    }

    private void getSystemInformation(File file) {
        String filePath = file.getPath();
        String errorPrefix = "GET DATA ERROR: ";
        Document doc = XMLHelper.getXMLDOC(filePath);
        if (doc != null) {
            Node rootNode = doc.getElementsByTagName(ImportDocument.getTagName()).item(0);
            Node systemInformation = XMLHelper.getNode(rootNode, "SystemInformation");
            if (systemInformation != null) {
                Element systemInformationElement = XMLHelper.NodeToElement(systemInformation);
                NodeList dateTimeElements = XMLHelper.getNodeList(systemInformation, "ExportDateTime");
                if (dateTimeElements != null && dateTimeElements.getLength() > 0) {
                    Element dateTimeElement = XMLHelper.getElement(dateTimeElements, 0);
                    String exportType = XMLHelper.getElementValue(systemInformationElement, "ExportType");
                    String directory = XMLHelper.getElementValue(systemInformationElement, "Directory");
                    ImportFileInfo info = new ImportFileInfo(file, dateTimeElement, exportType, directory, errorPrefix);
                    if (!info.getBrokenFile()) {
                        this.importFileInfo.add(info);
                    } else {
                        this.putErrors(file.getName(), info.getErrors());
                    }
                } else {
                    this.thisFileIsBad(file);
                    this.putError(file.getName(), errorPrefix + "Отсутствует блок ExportDateTime.");
                }

            } else {
                this.thisFileIsBad(file);
                this.putError(file.getName(), errorPrefix + "Отсутствует блок SystemInformation.");
            }
        } else {
            this.thisFileIsBad(file);
            this.putError(file.getName(), errorPrefix + "Невозможно обработать файл как xml документ.");
        }
    }

    public static void moveFile(File file, String dir) {
        file.renameTo(new File(dir, file.getName()));
    }

    public ArrayList<ImportFileInfo> getFilesForImport() {
        return filesForImport;
    }

    public class ImportFileInfo implements Comparable<ImportFileInfo> {

        private final File file;
        private final String filePath;
        private final String fileName;
        private Integer year = 0;
        private Integer month = 0;
        private Integer day = 0;
        private Integer hours = 0;
        private Integer minutes = 0;
        private Integer seconds = 0;
        private String exportType = "";
        private String directory = "";
        private Boolean brokenfile = false;
        private Integer importanceTypeValue = 0;
        private Timestamp timestamp;
        private ArrayList<String> errors = new ArrayList<>();

        public ImportFileInfo(File file, Element dateTimeElement, String exportType, String directory, String errorPrefix) {
            this.file = file;
            this.filePath = this.file.getPath();
            this.fileName = this.file.getName();
            String yearStr = XMLHelper.getElementValue(dateTimeElement, "year");
            String monthStr = XMLHelper.getElementValue(dateTimeElement, "month");
            String dayStr = XMLHelper.getElementValue(dateTimeElement, "day");
            String hoursStr = XMLHelper.getElementValue(dateTimeElement, "hours");
            String minutesStr = XMLHelper.getElementValue(dateTimeElement, "minutes");
            String secondsStr = XMLHelper.getElementValue(dateTimeElement, "seconds");
            if (!"".equals(yearStr)) {
                this.year = Integer.parseInt(yearStr);
            } else {
                this.brokenfile = true;
                this.errors.add(errorPrefix + "Отсутствует или содержит некорректное значение блок year.");
            }
            if (!"".equals(monthStr)) {
                this.month = Integer.parseInt(monthStr);
            } else {
                this.brokenfile = true;
                this.errors.add(errorPrefix + "Отсутствует или содержит некорректное значение блок month.");
            }
            if (!"".equals(dayStr)) {
                this.day = Integer.parseInt(dayStr);
            } else {
                this.brokenfile = true;
                this.errors.add(errorPrefix + "Отсутствует или содержит некорректное значение блок day.");
            }
            if (!"".equals(hoursStr)) {
                this.hours = Integer.parseInt(hoursStr);
            } else {
                this.brokenfile = true;
                this.errors.add(errorPrefix + "Отсутствует или содержит некорректное значение блок hours.");
            }
            if (!"".equals(minutesStr)) {
                this.minutes = Integer.parseInt(minutesStr);
            } else {
                this.brokenfile = true;
                this.errors.add(errorPrefix + "Отсутствует или содержит некорректное значение блок minutes.");
            }
            if (!"".equals(secondsStr)) {
                this.seconds = Integer.parseInt(secondsStr);
            } else {
                this.brokenfile = true;
                this.errors.add(errorPrefix + "Отсутствует или содержит некорректное значение блок seconds.");
            }
            if (!Arrays.asList(ShopImportDeamon.types).contains(exportType)) {
                this.brokenfile = true;
                this.errors.add(errorPrefix + "Отсутствует или содержит некорректное значение блок ExportType.");
            }
            this.exportType = exportType;
            this.directory = directory;
            if (this.getBrokenFile()) {
                thisFileIsBad(file);
            } else {
                String xmlDateString = year + "-" + month + "-" + day + " " + hours + ":" + minutes + ":" + seconds;
                SimpleDateFormat dateBaseDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    Date xmlDate = dateBaseDateFormat.parse(xmlDateString);
                    this.timestamp = new Timestamp(xmlDate.getTime());
                } catch (ParseException ex) {
                    Logger.getLogger(FileFinder.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            this.thisImportanceTypeValue();
        }

        public Boolean newerThan(ImportFileInfo fileDateTime) {
            Boolean rezult = false;
            if (fileDateTime == null) {
                rezult = true;
            } else if (fileDateTime.getBrokenFile()) {
                rezult = true;
            } else if (this.year > fileDateTime.getYear() || (Objects.equals(this.year, fileDateTime.getYear()) && this.moreImportantType(fileDateTime))) {
                rezult = true;
            } else if (this.month > fileDateTime.getMonth() || (Objects.equals(this.month, fileDateTime.getMonth()) && this.moreImportantType(fileDateTime))) {
                rezult = true;
            } else if (this.day > fileDateTime.getDay() || (Objects.equals(this.day, fileDateTime.getDay()) && this.moreImportantType(fileDateTime))) {
                rezult = true;
            } else if (this.hours > fileDateTime.getHours() || (Objects.equals(this.hours, fileDateTime.getHours()) && this.moreImportantType(fileDateTime))) {
                rezult = true;
            } else if (this.minutes > fileDateTime.getMinutes() || (Objects.equals(this.minutes, fileDateTime.getMinutes()) && this.moreImportantType(fileDateTime))) {
                rezult = true;
            } else if (this.seconds > fileDateTime.getSeconds() || (Objects.equals(this.seconds, fileDateTime.getSeconds()) && this.moreImportantType(fileDateTime))) {
                rezult = true;
            }
            return rezult;
        }

        public void thisImportanceTypeValue() {
            Boolean dirExport = !this.directory.equals("");
            if (this.exportType.equals(ShopImportDeamon.typePricesAndAmounts) && dirExport) {
                this.importanceTypeValue = 1;
            } else if (this.exportType.equals(ShopImportDeamon.typePricesAndAmounts) && !dirExport) {
                this.importanceTypeValue = 2;
            } else if (this.exportType.equals(ShopImportDeamon.typeChanges) && dirExport) {
                this.importanceTypeValue = 3;
            } else if (this.exportType.equals(ShopImportDeamon.typeChanges) && !dirExport) {
                this.importanceTypeValue = 4;
            } else if (this.exportType.equals(ShopImportDeamon.typeGeneral) && dirExport) {
                this.importanceTypeValue = 5;
            } else if (this.exportType.equals(ShopImportDeamon.typeGeneral) && !dirExport) {
                this.importanceTypeValue = 6;
            } else {
                this.importanceTypeValue = 0;
            }
        }

        public Boolean moreImportantType(ImportFileInfo fileDateTime) {
            return this.getThisImportanceTypeValue() > fileDateTime.getThisImportanceTypeValue();
        }

        public Integer getThisImportanceTypeValue() {
            return importanceTypeValue;
        }

        public Integer getYear() {
            return year;
        }

        public Integer getMonth() {
            return month;
        }

        public Integer getDay() {
            return day;
        }

        public Integer getHours() {
            return hours;
        }

        public Integer getMinutes() {
            return minutes;
        }

        public Integer getSeconds() {
            return seconds;
        }

        public String getFileName() {
            return fileName;
        }

        public String getFilePath() {
            return filePath;
        }

        public File getFile() {
            return file;
        }

        public String getExportType() {
            return exportType;
        }

        public String getDirectory() {
            return directory;
        }

        public Timestamp getTimestamp() {
            return timestamp;
        }

        public Boolean getBrokenFile() {
            return this.brokenfile;
        }

        public ArrayList<String> getErrors() {
            return this.errors;
        }

        @Override
        public int compareTo(ImportFileInfo fileDateTime) {
            if (this.newerThan(fileDateTime)) {
                return 1;
            } else {
                return -1;
            }
        }
    }
}
