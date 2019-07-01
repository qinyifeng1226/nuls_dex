package io.nuls.dex.util.bancor;

import io.nuls.dex.util.StringUtils;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

/**
 * 类描述：bancor公式测试
 *
 * @author qinyifeng
 * @version v1.0
 * @date 2019/7/1 10:11
 */
public class BancorFormulaTest {

    @Before
    public void setUp() throws Exception {
//        BancorFormula.supply = 300000d;
//        BancorFormula.balance = 270000d;
//        BancorFormula.cw = 0.9d;
        BancorFormula.supply = 300000d;
        BancorFormula.balance = 60000d;
        BancorFormula.cw = 0.2d;
        BancorFormula.marketCap = 0d;
    }

    @Test
    public void BuySellTest() {
        Double paid = 1000d;
        Double sell = 1000d;
        for (int i = 1; i <= 100; i++) {
            if (i % 5 == 0) {
                // sell token
                Double reserve = BancorFormula.calculateSell(sell);
                System.out.println("get reserve: " + reserve + ", supply: " + StringUtils.format(BancorFormula.supply) + ", balance: " + StringUtils.format(BancorFormula.balance) + ", price: " + BancorFormula.price + ", marketCap: " + StringUtils.format(BancorFormula.marketCap));
            } else {
                // buy token
                Double tokens = BancorFormula.calculateBuy(paid);
                System.out.println("buy tokens: " + tokens + ", supply: " + StringUtils.format(BancorFormula.supply) + ", balance: " + StringUtils.format(BancorFormula.balance) + ", price: " + BancorFormula.price + ", marketCap: " + StringUtils.format(BancorFormula.marketCap));
            }
        }
    }

    /**
     * 测试白皮书中的数据用例，检查是否一致
     */
    @Test
    public void buyAndSellTest2() {
        BancorFormula.init(300000d, 60000d, 0.2d);
        Double paid = 300d;
        Double tokens = BancorFormula.calculateBuy(paid);
        System.out.println("buy tokens: " + tokens + ", supply: " + StringUtils.format(BancorFormula.supply) + ", balance: " + StringUtils.format(BancorFormula.balance) + ", price: " + BancorFormula.price + ", marketCap: " + StringUtils.format(BancorFormula.marketCap));
        tokens = BancorFormula.calculateBuy(700d);
        System.out.println("buy tokens: " + tokens + ", supply: " + StringUtils.format(BancorFormula.supply) + ", balance: " + StringUtils.format(BancorFormula.balance) + ", price: " + BancorFormula.price + ", marketCap: " + StringUtils.format(BancorFormula.marketCap));
        tokens = BancorFormula.calculateSell(1302d);
        System.out.println("buy tokens: " + tokens + ", supply: " + StringUtils.format(BancorFormula.supply) + ", balance: " + StringUtils.format(BancorFormula.balance) + ", price: " + BancorFormula.price + ", marketCap: " + StringUtils.format(BancorFormula.marketCap));
        tokens = BancorFormula.calculateBuy(100d);
        System.out.println("buy tokens: " + tokens + ", supply: " + StringUtils.format(BancorFormula.supply) + ", balance: " + StringUtils.format(BancorFormula.balance) + ", price: " + BancorFormula.price + ", marketCap: " + StringUtils.format(BancorFormula.marketCap));
    }

    /**
     * 测试：CRR为50%，Supply为300000，balance为150000
     */
    @Test
    public void buyAndSellTest3() {
        BancorFormula.init(300000d, 150000d, 0.5d);
        Double tokens = BancorFormula.calculateBuy(100000d);
        System.out.println("buy tokens: " + tokens + ", supply: " + StringUtils.format(BancorFormula.supply) + ", balance: " + StringUtils.format(BancorFormula.balance) + ", price: " + BancorFormula.price + ", marketCap: " + StringUtils.format(BancorFormula.marketCap));
        tokens = BancorFormula.calculateBuy(500000d);
        System.out.println("buy tokens: " + tokens + ", supply: " + StringUtils.format(BancorFormula.supply) + ", balance: " + StringUtils.format(BancorFormula.balance) + ", price: " + BancorFormula.price + ", marketCap: " + StringUtils.format(BancorFormula.marketCap));
        tokens = BancorFormula.calculateSell(200000d);
        System.out.println("buy tokens: " + tokens + ", supply: " + StringUtils.format(BancorFormula.supply) + ", balance: " + StringUtils.format(BancorFormula.balance) + ", price: " + BancorFormula.price + ", marketCap: " + StringUtils.format(BancorFormula.marketCap));
        tokens = BancorFormula.calculateBuy(300000d);
        System.out.println("buy tokens: " + tokens + ", supply: " + StringUtils.format(BancorFormula.supply) + ", balance: " + StringUtils.format(BancorFormula.balance) + ", price: " + BancorFormula.price + ", marketCap: " + StringUtils.format(BancorFormula.marketCap));
    }

}