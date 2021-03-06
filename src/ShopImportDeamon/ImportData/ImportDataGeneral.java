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

import ShopImportDeamon.Helpers.FileFinder;
import ShopImportDeamon.Helpers.MySQL.MySQLPreparedStatement;
import ShopImportDeamon.ImportData.Parts.ImportItems;
import ShopImportDeamon.ImportData.Parts.ImportPricesTypes;
import ShopImportDeamon.ImportData.Parts.ImportStorages;

/**
 *
 * @author Maxim Zaytsev
 */
public class ImportDataGeneral extends ImportDataMain {

    public ImportDataGeneral(FileFinder.ImportFileInfo importFile) {
        super(importFile);
    }

    @Override
    public void execution() {
        super.execution();
        super.log.writeInLog("Анализиурем данные в файле выгрузки.");
        ImportPricesTypes importPricesTypes = new ImportPricesTypes(rootNode, this.getLogFile(), this.getLogId(), this.importFile.getExportType());
        ImportStorages importStorages = new ImportStorages(rootNode, this.getLogFile(), this.getLogId(), this.importFile.getExportType());
        ImportItems importItems = new ImportItems(rootNode, this.getLogFile(), this.getLogId(), this.importFile.getExportType());
        if (this.noErrors()) {
            super.log.writeInLog("При анализе данных выгрузки не было выявлено критических ошибок. Присутпаем к импорту данных из файла " + this.importFile.getFileName() + ".");
            importPricesTypes.startExecution();
            importStorages.startExecution();
            importItems.startExecution();
            MySQLPreparedStatement.update_ShopImportLogs_SetSuccess(this.getLogId());
            super.log.writeInLog("Импорт данных из файла " + this.importFile.getFileName() + " завершен УСПЕШНО.");
        } else {
            super.log.writeInLog(super.log.WARNING_TYPE, "При анализе данных выгрузки были выявлены ошибки. Выгрузка из файла " + this.importFile.getFileName() + " ОТМЕНЕНА.");
        }
    }
}
