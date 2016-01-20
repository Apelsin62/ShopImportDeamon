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
package ShopImportDeamon.ImportData.Elements;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Maxim Zaytsev
 */
public class ItemElement {

    private final String id;
    private final String itemName;
    private final String article;
    private final String directory;
    private final String directotyPath;
    private String status;
    private final String type;
    private String pricePer;
    private final String seviceCenter;
    private Integer action;
    private Integer shown;
    private Integer toRemove;
    private Float totalAmount;
    private Float minAmount;
    private final Map<String, Float> detailAmountValueList = new HashMap<>();
    private final Map<String, Float> pricesValueList = new HashMap<>();

    public ItemElement(String id, String itemName, String article,
            String directory, String directotyPath, String status,
            String type, String pricePer, String seviceCenter,
            Integer action, Integer shown, Integer toRemove,
            Float totalAmount, Float minAmount,
            Map<String, Float> detailAmountValueList,
            Map<String, Float> pricesValueList) {
        this.id = id;
        this.itemName = itemName;
        this.article = article;
        this.directory = directory;
        this.directotyPath = directotyPath;
        this.status = status;
        this.type = type;
        this.pricePer = pricePer;
        this.seviceCenter = seviceCenter;
        this.action = action;
        this.shown = shown;
        this.toRemove = toRemove;
        this.totalAmount = totalAmount;
        this.minAmount = minAmount;
        this.detailAmountValueList.putAll(detailAmountValueList);
        this.pricesValueList.putAll(pricesValueList);
    }

    public ItemElement(String id, Float totalAmount, Float minAmount,
            Map<String, Float> detailAmountValueList,
            Map<String, Float> pricesValueList) {
        this.id = id;
        this.itemName = null;
        this.article = null;
        this.directory = null;
        this.directotyPath = null;
        this.status = null;
        this.type = null;
        this.pricePer = null;
        this.seviceCenter = null;
        this.action = null;
        this.shown = null;
        this.toRemove = null;
        this.totalAmount = totalAmount;
        this.minAmount = minAmount;
        this.detailAmountValueList.putAll(detailAmountValueList);
        this.pricesValueList.putAll(pricesValueList);
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setAction(Integer action) {
        this.action = action;
    }

    public void setShown(Integer shown) {
        this.shown = shown;
    }

    public void setToRemove(Integer toRemove) {
        this.toRemove = toRemove;
    }

    public void setPricePer(String pricePer) {
        this.pricePer = pricePer;
    }

    public void setTotalAmount(Float totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setMinAmount(Float minAmount) {
        this.minAmount = minAmount;
    }

    public void setDetailAmountValueList(Map<String, Float> valueList) {
        this.detailAmountValueList.clear();
        this.detailAmountValueList.putAll(valueList);
    }

    public void setPricesValueList(Map<String, Float> valueList) {
        this.pricesValueList.clear();
        this.pricesValueList.putAll(valueList);
    }

    public String getId() {
        return id;
    }

    public String getItemName() {
        return itemName;
    }

    public String getArticle() {
        return article;
    }

    public String getDirectory() {
        return directory;
    }

    public String getDirectotyPath() {
        return directotyPath;
    }

    public String getStatus() {
        return status;
    }

    public String getType() {
        return type;
    }

    public String getPricePer() {
        return pricePer;
    }

    public String getSeviceCenter() {
        return seviceCenter;
    }

    public Integer getAction() {
        return action;
    }

    public Integer getShown() {
        return shown;
    }

    public Integer getToRemove() {
        return toRemove;
    }

    public Float getTotalAmount() {
        return totalAmount;
    }

    public Float getMinAmount() {
        return minAmount;
    }

    public Map<String, Float> getDetailAmountValueList() {
        return detailAmountValueList;
    }

    public Map<String, Float> getPricesValueList() {
        return pricesValueList;
    }
}
