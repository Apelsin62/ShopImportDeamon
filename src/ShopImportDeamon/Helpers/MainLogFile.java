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
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author Maxim Zaytsev
 */
public class MainLogFile {

    private static MainLogFile _instance = null;
    
    private final Configuration configuration = Configuration.getInstance();
    private final String logFile;
    private final String logFolder = configuration.getFilePathsData(SystemConstants.getFilePathsConfigureKey_MainLogFolder(), "directory");
    private final Locale local = new Locale("ru", "RU");
    private final String startDate;
    private final String startTime;

    public final String ERROR_TYPE = "ERROR";
    public final String WARNING_TYPE = "WARNING";
    public final String NOTICE_TYPE = "NOTICE";
    public final String MESSAGE_TYPE = "MESSAGE";

    private MainLogFile() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd '-' HH:mm:ss");
        SimpleDateFormat startDateFormat = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat startTimeFormat = new SimpleDateFormat("dd:MM:yyyy");
        Date date = new Date();
        this.startDate = startDateFormat.format(date);
        this.startTime = startTimeFormat.format(date);
        this.logFile = dateFormat.format(date) + ".log";
        this.writeHeadInLog();
    }

    private void writeHeadInLog() {
        try (FileWriter writer = new FileWriter(this.logFolder + this.logFile, true)) {
            writer.write("Program: " + SystemConstants.getProgramName() + "\n");
            writer.write("Version: " + SystemConstants.getProgramVersion() + "\n");
            writer.write("Start date: " + this.startDate + "\n");
            writer.write("Start time: " + this.startTime + "\n");
            writer.write("Program: Shop Import Deamon\n");
            writer.write("---------------------------\n\n");
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void writeInLog(String type, String message) {
        try (FileWriter writer = new FileWriter(this.logFolder + this.logFile, true)) {
            DateFormat df = DateFormat.getTimeInstance(DateFormat.DEFAULT, local);
            Date currentTime = new Date();
            String logString = df.format(currentTime) + " - " + type + ": " + message;
            writer.write(logString + "\n");
            System.out.println(logString);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void writeInLog(String message) {
        try (FileWriter writer = new FileWriter(this.logFolder + this.logFile, true)) {
            DateFormat df = DateFormat.getTimeInstance(DateFormat.DEFAULT, local);
            Date currentTime = new Date();
            String logString = df.format(currentTime) + ": " + message;
            writer.write(logString + "\n");
            System.out.println(logString);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public synchronized static MainLogFile getInstance() {
        if (_instance == null) {
            _instance = new MainLogFile();
        }
        return _instance;
    }
}
