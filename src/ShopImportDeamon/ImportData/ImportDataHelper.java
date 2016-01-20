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

import ShopImportDeamon.ImportData.Elements.PricesTypeElement;
import ShopImportDeamon.ImportData.Elements.StorageElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Maxim Zaytsev
 */
public class ImportDataHelper {

    private static ImportDataHelper _instance = null;
    private static final Map<String, PricesTypeElement> pricesTypes = new HashMap<>();
    private static final Map<String, PricesTypeElement> pricesTypesXML = new HashMap<>();
    private static final Map<String, StorageElement> Storages = new HashMap<>();
    private static final Map<String, StorageElement> StoragesXML = new HashMap<>();
    private static final ArrayList<String> pricesTypesId = new ArrayList<>();
    private static final ArrayList<String> pricesTypesXMLId = new ArrayList<>();
    private static final ArrayList<String> StoragesId = new ArrayList<>();
    private static final ArrayList<String> StoragesXMLId = new ArrayList<>();
    private static String defaultPricesTypes = null;

    /**
     * Получение конфигурации
     */
    private ImportDataHelper() {
    }

    public void setDefaultPricesTypes(String defaultPricesTypes) {
        ImportDataHelper.defaultPricesTypes = defaultPricesTypes;
    }

    public void addPricesType(PricesTypeElement pricesType) {
        String id = pricesType.getVal_id();
        if (ImportDataHelper.pricesTypes.get(id) == null) {
            ImportDataHelper.pricesTypesId.add(id);
        }
        ImportDataHelper.pricesTypes.put(id, pricesType);
    }

    public void addPricesTypeXML(PricesTypeElement pricesType) {
        String id = pricesType.getVal_id();
        if (ImportDataHelper.pricesTypesXML.get(id) == null) {
            ImportDataHelper.pricesTypesXMLId.add(id);
        }
        ImportDataHelper.pricesTypesXML.put(id, pricesType);
    }

    public void addStorage(StorageElement storage) {
        String id = storage.getId();
        if (ImportDataHelper.Storages.get(id) == null) {
            ImportDataHelper.StoragesId.add(id);
        }
        ImportDataHelper.Storages.put(id, storage);
    }

    public void addStorageXML(StorageElement storage) {
        String id = storage.getId();
        if (ImportDataHelper.StoragesXML.get(id) == null) {
            ImportDataHelper.StoragesXMLId.add(id);
        }
        ImportDataHelper.StoragesXML.put(id, storage);
    }

    public Map<String, PricesTypeElement> getAllPricesTypes() {
        return ImportDataHelper.pricesTypes;
    }

    public Map<String, PricesTypeElement> getAllPricesTypesXML() {
        return ImportDataHelper.pricesTypesXML;
    }

    public Map<String, StorageElement> getAllStorages() {
        return ImportDataHelper.Storages;
    }

    public Map<String, StorageElement> getAllStoragesXML() {
        return ImportDataHelper.StoragesXML;
    }

    public ArrayList<String> getAllPricesTypesId() {
        return ImportDataHelper.pricesTypesId;
    }

    public ArrayList<String> getAllPricesTypesXMLId() {
        return ImportDataHelper.pricesTypesXMLId;
    }

    public ArrayList<String> getAllStoragesId() {
        return ImportDataHelper.StoragesId;
    }

    public ArrayList<String> getAllStoragesXMLId() {
        return ImportDataHelper.StoragesXMLId;
    }

    public String getDefaultPricesTypes() {
        return ImportDataHelper.defaultPricesTypes;
    }

    /**
     * Получить singleton объект класса
     *
     * @return Configuration - singleton объект класса
     */
    public synchronized static ImportDataHelper getInstance() {
        if (_instance == null) {
            _instance = new ImportDataHelper();
        }
        return _instance;
    }

}
