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
package ShopImportDeamon;

import ShopImportDeamon.Helpers.MainLogFile;
import ShopImportDeamon.ImportData.ImportData;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Выгрузка данных из 1C. Для работы проекта нужны каталоги: conf, resources
 *
 * @author mzaytsev
 */
public class ShopImportDeamon {

    public static final String typeGeneral = "General";
    public static final String typeChanges = "Changes";
    public static final String typePricesAndAmounts = "PricesAndAmounts";
    public static final String[] types = {typeGeneral, typeChanges, typePricesAndAmounts};

    private static final String lockFile = "./conf/ProgramSession.Lock";
    private static MainLogFile log = MainLogFile.getInstance();

    /**
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        if ((new File(lockFile)).exists()) {
            log.writeInLog(log.ERROR_TYPE, "Уже запущен один экземпляр програмы");
        } else {
            execution();
        }
        log.writeInLog("Программа завершила свое выполнение.");
    }

    /**
     * Основной исполняющий блок
     */
    private static void execution() {
        createLockFile();
        log.writeInLog("Начинаем анализировать и обрабатывать данные.");
        ImportData importData = new ImportData();
        importData.execution();
        log.writeInLog("Все операции завершены. Прекращение выполнения программы.");
        deleteLockFile();
    }

    /**
     * Создаем файл блокировки
     */
    private static void createLockFile() {
        File newFile = new File(lockFile);
        try {
            newFile.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(ShopImportDeamon.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Удаляем файл блокировки
     */
    private static void deleteLockFile() {
        File file = new File(lockFile);
        file.delete();
    }

}
