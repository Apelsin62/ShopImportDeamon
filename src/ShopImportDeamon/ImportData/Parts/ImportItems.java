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
import ShopImportDeamon.ImportData.Elements.ItemElement;
import ShopImportDeamon.ImportData.Elements.PricesTypeElement;
import ShopImportDeamon.ImportData.Elements.StorageElement;
import ShopImportDeamon.ShopImportDeamon;
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
public class ImportItems extends ImportPart {

    private ArrayList<String> pricesTypesId;
    private ArrayList<String> pricesTypesXMLId;
    private Map<String, PricesTypeElement> pricesTypesXML;
    private ArrayList<String> storagesId;
    private ArrayList<String> storagesXMLId;
    private Map<String, StorageElement> storagesXML;
    private String defaultPricesTypes;
    private String defaultPricePer;

    private Map<String, ItemElement> ItemsListXML;
    private Map<String, String> ItemsIdListDB;
    private Map<String, String> ItemsStatusListDB;
    private ArrayList<ItemElement> insertElements;
    private ArrayList<ItemElement> updateElements;

    private Map<String, Boolean> statusForNoZiro;

    public ImportItems(Node rootNode, LogFile logFile, String logId, String exportType) {
        super(rootNode, logFile, logId, exportType);
    }

    @Override
    protected Node getThisNode() {
        return XMLHelper.getNode(this.rootNode, "Items");
    }

    @Override
    protected void preprocessing() {
        this.defaultPricePer = "шт";
        this.ItemsListXML = new HashMap<>();
        this.ItemsIdListDB = new HashMap<>();
        this.ItemsStatusListDB = new HashMap<>();
        this.insertElements = new ArrayList<>();
        this.updateElements = new ArrayList<>();

        this.pricesTypesId = new ArrayList<>();
        this.pricesTypesId.addAll(this.importDataHelper.getAllPricesTypesId());
        this.pricesTypesXMLId = new ArrayList<>();
        this.pricesTypesXMLId.addAll(this.importDataHelper.getAllPricesTypesXMLId());
        this.pricesTypesXML = new HashMap<>();
        this.pricesTypesXML.putAll(this.importDataHelper.getAllPricesTypesXML());
        this.defaultPricesTypes = this.importDataHelper.getDefaultPricesTypes();

        this.storagesId = new ArrayList<>();
        this.storagesId.addAll(this.importDataHelper.getAllStoragesId());
        this.storagesXMLId = new ArrayList<>();
        this.storagesXMLId.addAll(this.importDataHelper.getAllStoragesXMLId());
        this.storagesXML = new HashMap<>();
        this.storagesXML.putAll(this.importDataHelper.getAllStoragesXML());

        this.statusForNoZiro = new HashMap<>();
        this.statusForNoZiro.put("Возится и в наличии", true);
        this.statusForNoZiro.put("Возится под заказ", false);
        this.statusForNoZiro.put("Снят с производства, распродается", false);
        this.statusForNoZiro.put("Распродается, возится под заказ", false);
        this.statusForNoZiro.put("Комиссионный товар", false);

        if (this.thisNode != null) {
            this.getElementDataFromXml();
            this.getElementDataFromDB();
            this.checkMerge();
        } else {
            this.writeInLog(this.logFile.ERROR_TYPE, "Отсутствует блок Items.");
        }
    }

    protected void execution_General() {
        for (ItemElement insertElement : insertElements) {
            Map<String, Float> pricesValueList = new HashMap<>();
            pricesValueList.putAll(insertElement.getPricesValueList());
            Map<String, Float> detailAmountValueList = new HashMap<>();
            detailAmountValueList.putAll(insertElement.getDetailAmountValueList());
            MySQLPreparedStatement.import_ShopItems(
                    insertElement.getId(),
                    insertElement.getItemName(),
                    insertElement.getArticle(),
                    insertElement.getDirectory(),
                    insertElement.getDirectoryPath(),
                    insertElement.getStatus(),
                    insertElement.getType(),
                    insertElement.getPricePer(),
                    insertElement.getSeviceCenter(),
                    insertElement.getAction(),
                    insertElement.getShown(),
                    insertElement.getToRemove(),
                    insertElement.getTotalAmount(),
                    insertElement.getMinAmount());
            for (Map.Entry<String, Float> priceValue : pricesValueList.entrySet()) {
                String key = priceValue.getKey();
                Float value = priceValue.getValue();
                MySQLPreparedStatement.import_ShopItemsPrices(insertElement.getId(), key, value);
            }
            for (Map.Entry<String, Float> detailAmount : detailAmountValueList.entrySet()) {
                String key = detailAmount.getKey();
                Float value = detailAmount.getValue();
                MySQLPreparedStatement.import_ShopItemsAmount(insertElement.getId(), key, value);
            }
        }
        for (ItemElement updateElement : updateElements) {
            Map<String, Float> pricesValueList = new HashMap<>();
            pricesValueList.putAll(updateElement.getPricesValueList());
            Map<String, Float> detailAmountValueList = new HashMap<>();
            detailAmountValueList.putAll(updateElement.getDetailAmountValueList());
            MySQLPreparedStatement.update_ShopItems(
                    updateElement.getId(),
                    updateElement.getItemName(),
                    updateElement.getArticle(),
                    updateElement.getDirectory(),
                    updateElement.getDirectoryPath(),
                    updateElement.getStatus(),
                    updateElement.getType(),
                    updateElement.getPricePer(),
                    updateElement.getSeviceCenter(),
                    updateElement.getAction(),
                    updateElement.getShown(),
                    updateElement.getToRemove(),
                    updateElement.getTotalAmount(),
                    updateElement.getMinAmount());
            for (Map.Entry<String, Float> priceValue : pricesValueList.entrySet()) {
                String key = priceValue.getKey();
                Float value = priceValue.getValue();
                MySQLPreparedStatement.update_ShopItemsPrices(updateElement.getId(), key, value);
            }
            for (Map.Entry<String, Float> detailAmount : detailAmountValueList.entrySet()) {
                String key = detailAmount.getKey();
                Float value = detailAmount.getValue();
                MySQLPreparedStatement.update_ShopItemsAmount(updateElement.getId(), key, value);
            }
        }
    }

    protected void execution_Changes() {
        this.execution_General();
    }

    protected void execution_PricesAndAmounts() {
        for (ItemElement updateElement : updateElements) {
            Map<String, Float> pricesValueList = new HashMap<>();
            pricesValueList.putAll(updateElement.getPricesValueList());
            Map<String, Float> detailAmountValueList = new HashMap<>();
            detailAmountValueList.putAll(updateElement.getDetailAmountValueList());
            MySQLPreparedStatement.update_ShopItems_PricesAndAmounts(
                    updateElement.getId(),
                    updateElement.getTotalAmount(),
                    updateElement.getMinAmount());
            for (Map.Entry<String, Float> priceValue : pricesValueList.entrySet()) {
                String key = priceValue.getKey();
                Float value = priceValue.getValue();
                MySQLPreparedStatement.update_ShopItemsPrices(updateElement.getId(), key, value);
            }
            for (Map.Entry<String, Float> detailAmount : detailAmountValueList.entrySet()) {
                String key = detailAmount.getKey();
                Float value = detailAmount.getValue();
                MySQLPreparedStatement.update_ShopItemsAmount(updateElement.getId(), key, value);
            }
        }
    }

    @Override
    protected void execution() {
        switch (this.exportType) {
            case ShopImportDeamon.typeGeneral:
                this.execution_General();
                break;
            case ShopImportDeamon.typeChanges:
                this.execution_Changes();
                break;
            case ShopImportDeamon.typePricesAndAmounts:
                this.execution_PricesAndAmounts();
                break;
        }
    }

    private void getElementDataFromXml_General() {
        NodeList elements = XMLHelper.getNodeList(this.thisNode, "Item");
        if (elements != null && elements.getLength() > 0) {
            for (int s = 0; s < elements.getLength(); s++) {
                Map<String, Float> detailAmountValueList = new HashMap<>();
                Map<String, Float> pricesValueList = new HashMap<>();
                Element element = XMLHelper.getElement(elements, s);
                String val_id = XMLHelper.getElementValue(element, "id");
                String val_itemName = XMLHelper.getElementValue(element, "itemName");
                String val_article = XMLHelper.getElementValue(element, "article");
                String val_directory = XMLHelper.getElementValue(element, "directory");
                String val_directoryPath = XMLHelper.getElementValue(element, "directoryPath");
                String val_status = XMLHelper.getElementValue(element, "status");
                String val_type = XMLHelper.getElementValue(element, "type");
                String val_pricePer = XMLHelper.getElementValue(element, "pricePer");
                String val_seviceCenter = XMLHelper.getElementValue(element, "seviceCenter");
                Integer val_action = Integer.parseInt(XMLHelper.getElementValue(element, "action"));
                Integer val_shown = Integer.parseInt(XMLHelper.getElementValue(element, "shown"));
                Integer val_toRemove = Integer.parseInt(XMLHelper.getElementValue(element, "toRemove"));
                Float val_totalAmount = Float.parseFloat(XMLHelper.getElementValue(element, "totalAmount"));
                Float val_minAmount = Float.parseFloat(XMLHelper.getElementValue(element, "minAmount"));
                Element detailAmountElement = XMLHelper.getElement(element, "detailAmount");
                NodeList detailAmountList = XMLHelper.getNodeList(detailAmountElement, "amount");
                if (detailAmountList != null && detailAmountList.getLength() > 0) {
                    for (int p = 0; p < detailAmountList.getLength(); p++) {
                        Element detailAmount = XMLHelper.getElement(detailAmountList, p);
                        detailAmountValueList.put(XMLHelper.getElementValue(detailAmount, "storage"), Float.parseFloat(XMLHelper.getElementValue(detailAmount, "value")));
                    }
                } else {
                    this.writeInLog(this.logFile.ERROR_TYPE, "Для товара " + val_id + " (" + val_itemName + ") не найдено ни однйо записи в блоке detailAmount.");
                }
                Element pricesElement = XMLHelper.getElement(element, "prices");
                NodeList pricesList = XMLHelper.getNodeList(pricesElement, "price");
                if (pricesList != null && pricesList.getLength() > 0) {
                    for (int e = 0; e < pricesList.getLength(); e++) {
                        Element price = XMLHelper.getElement(pricesList, e);
                        pricesValueList.put(XMLHelper.getElementValue(price, "priceType"), Float.parseFloat(XMLHelper.getElementValue(price, "value")));
                    }
                } else {
                    this.writeInLog(this.logFile.ERROR_TYPE, "Для товара " + val_id + " (" + val_itemName + ") не найдено ни однйо записи в блоке prices.");
                }
                ItemElement item = new ItemElement(val_id, val_itemName,
                        val_article, val_directory, val_directoryPath,
                        val_status, val_type, val_pricePer, val_seviceCenter,
                        val_action, val_shown, val_toRemove, val_totalAmount,
                        val_minAmount, detailAmountValueList, pricesValueList);
                ItemsListXML.put(val_id, item);
            }
        } else {
            this.writeInLog(this.logFile.WARNING_TYPE, "Должен быть хотябы один блок Item.");
        }
    }

    private void getElementDataFromXml_Changes() {
        this.getElementDataFromXml_General();
    }

    private void getElementDataFromXml_PricesAndAmounts() {
        NodeList elements = XMLHelper.getNodeList(this.thisNode, "Item");
        if (elements != null && elements.getLength() > 0) {
            for (int s = 0; s < elements.getLength(); s++) {
                Map<String, Float> detailAmountValueList = new HashMap<>();
                Map<String, Float> pricesValueList = new HashMap<>();
                Element element = XMLHelper.getElement(elements, s);
                String val_id = XMLHelper.getElementValue(element, "id");
                Float val_totalAmount = Float.parseFloat(XMLHelper.getElementValue(element, "totalAmount"));
                Float val_minAmount = Float.parseFloat(XMLHelper.getElementValue(element, "minAmount"));
                Element detailAmountElement = XMLHelper.getElement(element, "detailAmount");
                NodeList detailAmountList = XMLHelper.getNodeList(detailAmountElement, "amount");
                if (detailAmountList != null && detailAmountList.getLength() > 0) {
                    for (int p = 0; p < detailAmountList.getLength(); p++) {
                        Element detailAmount = XMLHelper.getElement(detailAmountList, p);
                        detailAmountValueList.put(XMLHelper.getElementValue(detailAmount, "storage"), Float.parseFloat(XMLHelper.getElementValue(detailAmount, "value")));
                    }
                } else {
                    this.writeInLog(this.logFile.ERROR_TYPE, "Для товара " + val_id + " не найдено ни однйо записи в блоке detailAmount.");
                }
                Element pricesElement = XMLHelper.getElement(element, "prices");
                NodeList pricesList = XMLHelper.getNodeList(pricesElement, "price");
                if (pricesList != null && pricesList.getLength() > 0) {
                    for (int e = 0; e < pricesList.getLength(); e++) {
                        Element price = XMLHelper.getElement(pricesList, e);
                        pricesValueList.put(XMLHelper.getElementValue(price, "priceType"), Float.parseFloat(XMLHelper.getElementValue(price, "value")));
                    }
                } else {
                    this.writeInLog(this.logFile.ERROR_TYPE, "Для товара " + val_id + " не найдено ни однйо записи в блоке prices.");
                }
                ItemElement item = new ItemElement(val_id, val_totalAmount,
                        val_minAmount, detailAmountValueList, pricesValueList);
                ItemsListXML.put(val_id, item);
            }
        } else {
            this.writeInLog(this.logFile.WARNING_TYPE, "Должен быть хотябы один блок Item.");
        }
    }

    private void getElementDataFromXml() {
        switch (this.exportType) {
            case ShopImportDeamon.typeGeneral:
                this.getElementDataFromXml_General();
                break;
            case ShopImportDeamon.typeChanges:
                this.getElementDataFromXml_Changes();
                break;
            case ShopImportDeamon.typePricesAndAmounts:
                this.getElementDataFromXml_PricesAndAmounts();
                break;
        }
    }

    private void getElementDataFromDB() {
        ResultSet rs = MySQLPreparedStatement.select_ShopItems_IdAndStatus();
        try {
            while (rs.next()) {
                String val_id = rs.getString("id");
                String val_status = rs.getString("status");
                this.ItemsIdListDB.put(val_id, val_id);
                this.ItemsStatusListDB.put(val_id, val_status);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ImportItems.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void checkMerge_General() {
        for (Map.Entry<String, ItemElement> entrySet : ItemsListXML.entrySet()) {
            ItemElement item = entrySet.getValue();
            String itemId = item.getId();
            String itemName = item.getItemName();
            if (item.getId() == null || item.getId().equals("")) {
                this.writeInLog(this.logFile.ERROR_TYPE, "У товара " + itemId + " (" + itemName + ") отсутствует значение у обязательного поля id");
            }
            if (item.getItemName() == null || item.getItemName().equals("")) {
                this.writeInLog(this.logFile.ERROR_TYPE, "У товара " + itemId + " (" + itemName + ") отсутствует значение у обязательного поля itemName");
            }
            if (item.getDirectory() == null || item.getDirectory().equals("")) {
                this.writeInLog(this.logFile.ERROR_TYPE, "У товара " + itemId + " (" + itemName + ") отсутствует значение у обязательного поля directory");
            }
            if (item.getDirectoryPath() == null || item.getDirectoryPath().equals("")) {
                this.writeInLog(this.logFile.ERROR_TYPE, "У товара " + itemId + " (" + itemName + ") отсутствует значение у обязательного поля directoryPath");
            }
            if (item.getAction() == null || item.getAction() < 0) {
                item.setAction(0);
            }
            if (item.getShown() == null || item.getShown() < 0) {
                item.setShown(0);
            }
            if (item.getToRemove() == null || item.getToRemove() < 0) {
                item.setToRemove(0);
            }
            if (item.getMinAmount() == null || item.getMinAmount() < 0F) {
                item.setMinAmount(0F);
            }
            if (item.getTotalAmount() == null || item.getTotalAmount() < 0F) {
                item.setTotalAmount(0F);
            }
            if (item.getPricePer() == null || item.getPricePer().equals("")) {
                item.setPricePer(this.defaultPricePer);
            }
            Map<String, Float> pricesValueList = new HashMap<>();
            pricesValueList.putAll(item.getPricesValueList());
            Map<String, Float> detailAmountValueList = new HashMap<>();
            detailAmountValueList.putAll(item.getDetailAmountValueList());
            // Для каждого типа цен в XML есть значение цены

            if(this.defaultPricesTypes != null && this.pricesTypesXMLId != null && this.pricesTypesXML != null && this.pricesTypesXMLId.size() > 0 && this.pricesTypesXML.size() > 0) {
                Boolean defaultPriceNotZero = true;
                if (pricesValueList.get(this.defaultPricesTypes) != null && pricesValueList.get(this.defaultPricesTypes) <= 0F) {
                    if (this.CheckStatusNoZiro(item.getStatus())) {
                        this.writeInLog(this.logFile.WARNING_TYPE, "Для товара " + itemId + " (" + itemName + ") значение цены типа " + this.defaultPricesTypes + " (" + this.pricesTypesXML.get(this.defaultPricesTypes).getVal_typeName() + "), являющейся ценой по умолчанию, указано равным или меньше нуля");
                    }
                    defaultPriceNotZero = false;
                }
                for (String priceTypeXMLId : this.pricesTypesXMLId) {
                    if (pricesValueList.get(priceTypeXMLId) == null) {
                        this.writeInLog(this.logFile.WARNING_TYPE, "Для товара " + itemId + " (" + itemName + ") отсутствует значение цены типа " + priceTypeXMLId + " (" + this.pricesTypesXML.get(priceTypeXMLId).getVal_typeName() + ").");
                        pricesValueList.put(priceTypeXMLId, this.getDefaultPriseValue(item));
                    } else {
                        if (pricesValueList.get(priceTypeXMLId) <= 0F) {
                            if (!priceTypeXMLId.equals(this.defaultPricesTypes)) {
                                if (this.CheckStatusNoZiro(item.getStatus())) {
                                    this.writeInLog(this.logFile.NOTICE_TYPE, "Для товара " + itemId + " (" + itemName + ") значение цены типа " + priceTypeXMLId + " (" + this.pricesTypesXML.get(priceTypeXMLId).getVal_typeName() + ") указано равным или меньше нуля. Будет использовано значение цены по умолчанию.");
                                }
                            }
                            if (defaultPriceNotZero) {
                                pricesValueList.put(priceTypeXMLId, this.getDefaultPriseValue(item));
                            }
                        }
                    }

                }
                // В XML нет типа цен отсутствующего в самом XML
                for (Map.Entry<String, Float> price : pricesValueList.entrySet()) {
                    String priceId = price.getKey();
                    if (this.pricesTypesXML.get(priceId) == null) {
                        this.writeInLog(this.logFile.WARNING_TYPE, "Для товара " + itemId + " (" + itemName + ") указано значение цены неизвестного типа.");
                        pricesValueList.remove(priceId);
                    }
                }
            }
            
            if(this.storagesXMLId != null && this.storagesXML != null && this.storagesXMLId.size() > 0 && this.storagesXML.size() > 0) {
                // Для каждого Склада в XML есть значение количества
                for (String storagXMLId : this.storagesXMLId) {
                    if (detailAmountValueList.get(storagXMLId) == null) {
                        this.writeInLog(this.logFile.WARNING_TYPE, "Для товара " + itemId + " (" + itemName + ") отсутствует значение количества товара на складе " + storagXMLId + " (" + this.storagesXML.get(storagXMLId).getName() + ").");
                    } else {
                        if (detailAmountValueList.get(storagXMLId) < 0F) {
                            detailAmountValueList.put(storagXMLId, 0F);
                        }
                    }
                }
                // В XML нет склада отсутствующего в самом XML
                for (Map.Entry<String, Float> amount : detailAmountValueList.entrySet()) {
                    String amountId = amount.getKey();
                    if (this.storagesXML.get(amountId) == null) {
                        this.writeInLog(this.logFile.WARNING_TYPE, "Для товара " + itemId + " (" + itemName + ") указано количество товара на неизвестном складе.");
                        detailAmountValueList.remove(amountId);
                    }
                }
            }
            
            item.setPricesValueList(pricesValueList);
            item.setDetailAmountValueList(detailAmountValueList);

            if (this.ItemsIdListDB.get(itemId) != null) {
                this.updateElements.add(item);
            } else {
                this.insertElements.add(item);
            }
        }
    }

    private void checkMerge_Changes() {
        this.checkMerge_General();
    }

    private void checkMerge_PricesAndAmounts() {
        for (Map.Entry<String, ItemElement> entrySet : ItemsListXML.entrySet()) {
            ItemElement item = entrySet.getValue();
            String itemId = item.getId();
            if (this.ItemsIdListDB.get(itemId) != null) {
                if (this.ItemsStatusListDB.get(itemId) != null) {
                    item.setStatus(ItemsStatusListDB.get(itemId));
                }
                if (item.getId() == null || item.getId().equals("")) {
                    this.writeInLog(this.logFile.ERROR_TYPE, "У товара " + itemId + " отсутствует значение у обязательного поля id");
                }

                if (item.getMinAmount() == null || item.getMinAmount() < 0F) {
                    item.setMinAmount(0F);
                }
                if (item.getTotalAmount() == null || item.getTotalAmount() < 0F) {
                    item.setTotalAmount(0F);
                }
                Map<String, Float> pricesValueList = new HashMap<>();
                pricesValueList.putAll(item.getPricesValueList());
                Map<String, Float> detailAmountValueList = new HashMap<>();
                detailAmountValueList.putAll(item.getDetailAmountValueList());
                // Для каждого типа цен в XML есть значение цены

                Boolean defaultPriceNotZero = true;
                if (pricesValueList.get(this.defaultPricesTypes) != null && pricesValueList.get(this.defaultPricesTypes) <= 0F) {
                    if (this.CheckStatusNoZiro(item.getStatus())) {
                        this.writeInLog(this.logFile.WARNING_TYPE, "Для товара " + itemId + " значение цены типа " + this.defaultPricesTypes + " (" + this.pricesTypesXML.get(this.defaultPricesTypes).getVal_typeName() + "), являющейся ценой по умолчанию, указано равным или меньше нуля");
                    }
                    defaultPriceNotZero = false;
                }

                for (String priceTypeXMLId : pricesTypesXMLId) {
                    if (pricesValueList.get(priceTypeXMLId) == null) {
                        this.writeInLog(this.logFile.WARNING_TYPE, "Для товара " + itemId + " отсутствует значение цены типа " + priceTypeXMLId + " (" + this.pricesTypesXML.get(priceTypeXMLId).getVal_typeName() + ").");
                        pricesValueList.put(priceTypeXMLId, this.getDefaultPriseValue(item));
                    } else {
                        if (pricesValueList.get(priceTypeXMLId) <= 0F) {
                            if (!priceTypeXMLId.equals(this.defaultPricesTypes)) {
                                if (this.CheckStatusNoZiro(item.getStatus())) {
                                    this.writeInLog(this.logFile.NOTICE_TYPE, "Для товара " + itemId + " значение цены типа " + priceTypeXMLId + " (" + this.pricesTypesXML.get(priceTypeXMLId).getVal_typeName() + ") указано равным или меньше нуля. Будет использовано значение цены по умолчанию.");
                                }
                            }
                            if (defaultPriceNotZero) {
                                pricesValueList.put(priceTypeXMLId, this.getDefaultPriseValue(item));
                            }
                        }
                    }

                }
                // В XML нет типа цен отсутствующего в самом XML
                for (Map.Entry<String, Float> price : pricesValueList.entrySet()) {
                    String priceId = price.getKey();
                    if (this.pricesTypesXML.get(priceId) == null) {
                        this.writeInLog(this.logFile.WARNING_TYPE, "Для товара " + itemId + " указано значение цены неизвестного типа.");
                        pricesValueList.remove(priceId);
                    }
                }
                // Для каждого Склада в XML есть значение количества
                for (String storagXMLId : this.storagesXMLId) {
                    if (detailAmountValueList.get(storagXMLId) == null) {
                        this.writeInLog(this.logFile.WARNING_TYPE, "Для товара " + itemId + " отсутствует значение количества товара на складе " + storagXMLId + " (" + this.storagesXML.get(storagXMLId).getName() + ").");
                    } else {
                        if (detailAmountValueList.get(storagXMLId) < 0F) {
                            detailAmountValueList.put(storagXMLId, 0F);
                        }
                    }
                }
                // В XML нет склада отсутствующего в самом XML
                for (Map.Entry<String, Float> amount : detailAmountValueList.entrySet()) {
                    String amountId = amount.getKey();
                    if (this.storagesXML.get(amountId) == null) {
                        this.writeInLog(this.logFile.WARNING_TYPE, "Для товара " + itemId + " указано количество товара на неизвестном складе.");
                        detailAmountValueList.remove(amountId);
                    }
                }

                item.setPricesValueList(pricesValueList);
                item.setDetailAmountValueList(detailAmountValueList);

                this.updateElements.add(item);
            } else {
                this.writeInLog(this.logFile.WARNING_TYPE, "Товар " + itemId + " не был найден в базе. Изменения невозможны.");
            }
        }
    }

    private void checkMerge() {
        switch (this.exportType) {
            case ShopImportDeamon.typeGeneral:
                this.checkMerge_General();
                break;
            case ShopImportDeamon.typeChanges:
                this.checkMerge_Changes();
                break;
            case ShopImportDeamon.typePricesAndAmounts:
                this.checkMerge_PricesAndAmounts();
                break;
        }
    }

    private Boolean CheckStatusNoZiro(String status) {
        if (statusForNoZiro.get(status) != null) {
            return statusForNoZiro.get(status);
        }
        return false;
    }

    private Float getDefaultPriseValue(ItemElement item) {
        Float priceVal = 0F;
        Map<String, Float> pricesValueList = new HashMap<>();
        pricesValueList.putAll(item.getPricesValueList());
        if (pricesValueList.get(this.defaultPricesTypes) != null) {
            priceVal = pricesValueList.get(this.defaultPricesTypes);
        } else {
            ResultSet rs = MySQLPreparedStatement.select_ShopItemsPrices(item.getId(), this.defaultPricesTypes);
            try {
                while (rs.next()) {
                    priceVal = rs.getFloat("value");
                }
            } catch (SQLException ex) {
                Logger.getLogger(ImportItems.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (priceVal <= 0F) {
            priceVal = 0F;
            if (this.CheckStatusNoZiro(item.getStatus())) {
                this.writeInLog(this.logFile.WARNING_TYPE, "Для товара " + item.getId() + " (" + item.getItemName() + ") используется значение типа цены по умолчанию, но оно оказалось равным или меньше нуля.");
            }
        }
        return priceVal;
    }

}
