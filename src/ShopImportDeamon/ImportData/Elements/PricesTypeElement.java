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

import java.util.Objects;

/**
 * Хранит набор данных о типе цен
 *
 * @author Maxim Zaytsev
 */
public class PricesTypeElement {

    private final String val_id;
    private final String val_typeName;
    private final String val_typeName_1c;
    private final Integer val_default;

    public PricesTypeElement(String val_id, String val_typeName, String val_typeName_1c, Integer val_default) {
        this.val_id = val_id;
        this.val_typeName = val_typeName;
        this.val_typeName_1c = val_typeName_1c;
        this.val_default = val_default;
    }

    /**
     * Вернет истину если типы цен имеют полностью одинаковый набор данных
     *
     * @param pricesType тип цен
     * @return истина или лож
     */
    public Boolean fullLike(PricesTypeElement pricesType) {
        return this.like(pricesType) && (this.val_typeName == null ? pricesType.val_typeName == null : this.val_typeName.equals(pricesType.val_typeName));
    }

    /**
     * Вернет истину если типы цен имеют одинаковый набор данных не считая
     * названия для сайта - typeName
     *
     * @param pricesType тип цен
     * @return истина или лож
     */
    public Boolean like(PricesTypeElement pricesType) {
        return (this.val_id == null ? pricesType.val_id == null : this.val_id.equals(pricesType.val_id))
                && (this.val_typeName_1c == null ? pricesType.val_typeName_1c == null : this.val_typeName_1c.equals(pricesType.val_typeName_1c))
                && Objects.equals(this.val_default, pricesType.val_default);
    }

    public Boolean likeId(PricesTypeElement pricesType) {
        return (this.val_id == null ? pricesType.val_id == null : this.val_id.equals(pricesType.val_id));
    }

    public String getVal_id() {
        return val_id;
    }

    public String getVal_typeName() {
        return val_typeName;
    }

    public String getVal_typeName_1c() {
        return val_typeName_1c;
    }

    public Integer getVal_default() {
        return val_default;
    }
}
