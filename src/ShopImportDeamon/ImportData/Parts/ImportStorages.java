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
import ShopImportDeamon.Helpers.XMLHelper;
import ShopImportDeamon.ImportData.Elements.StorageElement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Maxim Zaytsev
 */
public class ImportStorages extends ImportPart {

    private Map<String, StorageElement> elementsFromDB;
    private Map<String, StorageElement> elementsFromXML;
    private ArrayList<String> elementsIdFromDB;
    private ArrayList<String> elementsIdFromXML;
    private ArrayList<StorageElement> insertElements;
    private ArrayList<StorageElement> updateElements;

    public ImportStorages(Node rootNode, LogFile logFile, String logId, String exportType) {
        super(rootNode, logFile, logId, exportType);
    }

    @Override
    protected Node getThisNode() {
        return XMLHelper.getNode(this.rootNode, "Storages");
    }

    @Override
    protected void preprocessing() {
        this.elementsFromDB = new HashMap<>();
        this.elementsFromXML = new HashMap<>();
        this.elementsIdFromDB = new ArrayList<>();
        this.elementsIdFromXML = new ArrayList<>();
        this.insertElements = new ArrayList<>();
        this.updateElements = new ArrayList<>();
        if (this.thisNode != null) {
            this.getElementDataFromXml();
            this.getElementDataFromDB();
            this.checkMerge();
        } else {
            this.writeInLog(this.logFile.ERROR_TYPE, "Отсутствует блок Storages.");
        }
    }

    @Override
    protected void execution() {
        for (StorageElement insertElement : insertElements) {
            MySQLPreparedStatement.import_ShopStorages(insertElement.getId(), insertElement.getName1c());
        }
        for (StorageElement updateElement : updateElements) {
            MySQLPreparedStatement.update_ShopStorages(updateElement.getId(), updateElement.getName1c());
        }
    }

    private void getElementDataFromXml() {
        NodeList elements = XMLHelper.getNodeList(this.thisNode, "Storage");
        if (elements != null && elements.getLength() > 0) {
            for (int s = 0; s < elements.getLength(); s++) {
                Element element = XMLHelper.getElement(elements, s);
                String val_id = XMLHelper.getElementValue(element, "id");
                String val_name = XMLHelper.getElementValue(element, "name");
                StorageElement stElement = new StorageElement(val_id, val_name, val_name);
                this.elementsFromXML.put(val_id, stElement);
                this.elementsIdFromXML.add(val_id);
            }
        } else {
            this.writeInLog(this.logFile.WARNING_TYPE, "Должен быть хотябы один блок Storage.");
        }
    }

    private void getElementDataFromDB() {
        ResultSet rs = MySQLPreparedStatement.select_ShopStorages();
        try {
            while (rs.next()) {
                String val_id = rs.getString("id");
                String val_name = rs.getString("storageName");
                String val_name_1c = rs.getString("storageName_1C");
                StorageElement stElement = new StorageElement(val_id, val_name, val_name_1c);
                this.elementsFromDB.put(val_id, stElement);
                elementsIdFromDB.add(val_id);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ImportStorages.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void checkMerge() {
        for (String idFromDB : elementsIdFromDB) {
            if (elementsFromXML.get(idFromDB) == null) {
                StorageElement element = elementsFromDB.get(idFromDB);
                this.writeInLog(this.logFile.WARNING_TYPE, "В файле выгрузки отсутствует стедующее хранилище: " + idFromDB + " (" + element.getName() + ")");
                importDataHelper.addStorage(element);
            }
        }
        for (String idFromXML : elementsIdFromXML) {
            StorageElement element = elementsFromXML.get(idFromXML);
            if (elementsFromDB.get(idFromXML) == null) {
                this.writeInLog(this.logFile.NOTICE_TYPE, "В файле выгрузки найдено новое хранилище: " + idFromXML + " (" + element.getName() + ")");
                this.insertElements.add(element);
            } else {
                this.updateElements.add(element);
            }
            importDataHelper.addStorage(element);
            importDataHelper.addStorageXML(element);
        }
    }

}
