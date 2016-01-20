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
import ShopImportDeamon.ImportData.Elements.PricesTypeElement;
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
public class ImportPricesTypes extends ImportPart {

    private Map<String, PricesTypeElement> elementsFromDB;
    private Map<String, PricesTypeElement> elementsFromXML;
    private ArrayList<String> elementsIdFromDB;
    private ArrayList<String> elementsIdFromXML;
    private ArrayList<PricesTypeElement> insertElements;
    private ArrayList<PricesTypeElement> updateElements;
    private String defaultElementsIdFromDB;
    private String defaultElementsIdFromXML;
    private String unsetDefault;

    public ImportPricesTypes(Node rootNode, LogFile logFile, String logId, String exportType) {
        super(rootNode, logFile, logId, exportType);
    }

    @Override
    protected Node getThisNode() {
        return XMLHelper.getNode(this.rootNode, "PriceTypes");
    }

    @Override
    protected void preprocessing() {
        this.elementsFromDB = new HashMap<>();
        this.elementsFromXML = new HashMap<>();
        this.elementsIdFromDB = new ArrayList<>();
        this.elementsIdFromXML = new ArrayList<>();
        this.insertElements = new ArrayList<>();
        this.updateElements = new ArrayList<>();
        this.defaultElementsIdFromDB = null;
        this.defaultElementsIdFromXML = null;
        this.unsetDefault = null;
        if (this.thisNode != null) {
            this.getElementDataFromXml();
            this.getElementDataFromDB();
            this.checkMerge();
        } else {
            this.writeInLog(this.logFile.ERROR_TYPE, "Отсутствует блок PriceTypes.");
        }
    }

    @Override
    protected void execution() {
        if (this.unsetDefault != null) {
            MySQLPreparedStatement.update_ShopPricesTypes_UnsetDefault(this.unsetDefault);
        }
        for (PricesTypeElement insertElement : insertElements) {
            MySQLPreparedStatement.import_ShopPricesTypes(insertElement.getVal_id(), insertElement.getVal_typeName_1c(), insertElement.getVal_default());
        }
        for (PricesTypeElement updateElement : updateElements) {
            MySQLPreparedStatement.update_ShopPricesTypes(updateElement.getVal_id(), updateElement.getVal_typeName_1c(), updateElement.getVal_default());
        }
    }

    private void getElementDataFromXml() {
        Boolean noDefault = true;
        NodeList elements = XMLHelper.getNodeList(this.thisNode, "PriceType");
        if (elements != null && elements.getLength() > 0) {
            for (int s = 0; s < elements.getLength(); s++) {
                Element element = XMLHelper.getElement(elements, s);
                String val_id = XMLHelper.getElementValue(element, "id");
                String val_typeName = XMLHelper.getElementValue(element, "name");
                Integer val_default = Integer.parseInt(XMLHelper.getElementValue(element, "default"));
                PricesTypeElement ptElement = new PricesTypeElement(val_id, val_typeName, val_typeName, val_default);
                if (val_default > 0) {
                    this.defaultElementsIdFromXML = val_id;
                    importDataHelper.setDefaultPricesTypes(val_id);
                    noDefault = false;
                }
                this.elementsFromXML.put(val_id, ptElement);
                this.elementsIdFromXML.add(val_id);
            }
            if (noDefault) {
                this.writeInLog(this.logFile.ERROR_TYPE, "В файле выгрузки отсутствует цена по умолчанию.");
            }
        } else {
            this.writeInLog(this.logFile.ERROR_TYPE, "Должен быть хотябы один блок PriceType.");
        }
    }

    private void getElementDataFromDB() {
        ResultSet rs = MySQLPreparedStatement.select_ShopPricesTypes();
        try {
            while (rs.next()) {
                String val_id = rs.getString("id");
                String val_typeName = rs.getString("typeName");
                String val_typeName_1c = rs.getString("typeName_1c");
                Integer val_default = rs.getInt("default");
                PricesTypeElement ptElement = new PricesTypeElement(val_id, val_typeName, val_typeName_1c, val_default);
                this.elementsFromDB.put(val_id, ptElement);
                elementsIdFromDB.add(val_id);
                if (val_default > 0) {
                    this.defaultElementsIdFromDB = val_id;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ImportPricesTypes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void checkMerge() {
        for (String idFromDB : elementsIdFromDB) {
            if (elementsFromXML.get(idFromDB) == null) {
                PricesTypeElement element = elementsFromDB.get(idFromDB);
                this.writeInLog(this.logFile.WARNING_TYPE, "В файле выгрузки отсутствует следующий тип для цены: " + idFromDB + " (" + element.getVal_typeName() + ")");
                importDataHelper.addPricesType(element);
            }
        }
        for (String idFromXML : elementsIdFromXML) {
            PricesTypeElement element = elementsFromXML.get(idFromXML);
            if (elementsFromDB.get(idFromXML) == null) {
                this.writeInLog(this.logFile.NOTICE_TYPE, "В файле выгрузки найден новый тип цены: " + idFromXML + " (" + element.getVal_typeName() + ")");
                this.insertElements.add(element);
            } else {
                this.updateElements.add(element);
            }
            importDataHelper.addPricesType(element);
            importDataHelper.addPricesTypeXML(element);
        }
        if (this.defaultElementsIdFromXML != null && this.defaultElementsIdFromDB != null) {
            if (!this.defaultElementsIdFromXML.equals(this.defaultElementsIdFromDB)) {
                PricesTypeElement elementXML = elementsFromXML.get(this.defaultElementsIdFromXML);
                PricesTypeElement elementDB = elementsFromDB.get(this.defaultElementsIdFromDB);
                this.writeInLog(this.logFile.NOTICE_TYPE, "Тип цен по умолчанию был с менен с " + this.defaultElementsIdFromDB + " (" + elementDB.getVal_typeName() + ") на " + this.defaultElementsIdFromXML + " (" + elementXML.getVal_typeName() + ")");
                this.unsetDefault = this.defaultElementsIdFromXML;
            }
        }
    }
}
