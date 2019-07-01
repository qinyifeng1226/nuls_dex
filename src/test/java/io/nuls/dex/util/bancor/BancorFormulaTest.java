package io.nuls.dex.util.bancor;

import io.nuls.dex.util.StringUtils;
import org.junit.Before;
import org.junit.Test;

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
                Double reserve = BancorFormula.calculateSell(BancorFormula.supply, BancorFormula.balance, BancorFormula.cw, sell);
                System.out.println("get reserve: " + reserve + ", supply: " + StringUtils.format(BancorFormula.supply) + ", balance: " + StringUtils.format(BancorFormula.balance) + ", price: " + BancorFormula.price + ", marketCap: " + StringUtils.format(BancorFormula.marketCap));
            } else {
                // buy token
                Double tokens = BancorFormula.calculateBuy(BancorFormula.supply, BancorFormula.balance, BancorFormula.cw, paid);
                System.out.println("buy tokens: " + tokens + ", supply: " + StringUtils.format(BancorFormula.supply) + ", balance: " + StringUtils.format(BancorFormula.balance) + ", price: " + BancorFormula.price + ", marketCap: " + StringUtils.format(BancorFormula.marketCap));
            }
        }
    }

}