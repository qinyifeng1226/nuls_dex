package io.nuls.dex.util.bancor;

import io.nuls.dex.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Scanner;

/**
 * 类描述：
 *
 * @author qinyifeng
 * @version v1.0
 * @date 2019/6/26 21:31
 */
public class BancorFormula {

    /**
     * connector token：准备金代币，如：eth、btc
     * balance：准备金代币余额数量
     * supply： 智能代币总数量
     * CRR：恒定准备金率（Constant Reserve Ratio），也叫做连接器权重（connector weight）,  = balance / (supply * price) , 0.2 = 200 / (1000*1)
     * price:  balance / (supply * CRR),  1 = 200 / (1000*0.2)
     * balance：= price * supply * CRR , 200 = 1 * 1000 * 0.2
     * smart token：可以连接多个connector token，此时smart token也叫转换token，用于connector token之间的转换
     * effectivePrice: connector tokens exchanged / smart tokens exchanged , 2 = 200(eth) / 100(bancor)
     */
//    static BigInteger ONE = BigInteger.valueOf(1L);
//    static BigInteger supply = BigInteger.valueOf(10000L);
//    static BigInteger balance = BigInteger.valueOf(2500L);
//    static BigInteger price = BigInteger.valueOf(1L);
//    static BigInteger ONE = BigInteger.valueOf(1L);

    /* CSDN示例*/
    /*
    static Double supply = 10000d;
    static Double balance = 2500d;
    static Double price = 0.5d;
    static Double cw = 0.5;
    */

    /*白皮书示例*/
    static Double supply = 300000d;
    static Double balance = 60000d;
    static Double price = 1d;
    static Double cw = 0.2;

    static Double marketCap = 0d;
    static int scale = 4;

    /**
     * 购买智能代币
     *
     * @param supply  智能代币总量
     * @param balance 准备金余额总量
     * @param cw      连接器权重
     * @param paid    支付准备金数量
     * @return 获得智能代币数量
     */
    public static Double calculateBuy(Double supply, Double balance, Double cw, Double paid) {
        Double tokens = BancorFormula.mul(supply, Math.pow(1 + BancorFormula.divides(String.valueOf(paid), String.valueOf(balance)), cw) - 1, 0);
        BancorFormula.supply = BancorFormula.add(BancorFormula.supply, tokens);
        BancorFormula.balance = BancorFormula.add(BancorFormula.balance, paid);
        BancorFormula.price = BancorFormula.divide(BancorFormula.balance, (BancorFormula.mul(BancorFormula.supply, cw)), BancorFormula.scale);
        BancorFormula.marketCap = BancorFormula.mul(BancorFormula.supply, BancorFormula.price);
        // 有效价格
        Double effectivePrice = BancorFormula.divide(paid, tokens, BancorFormula.scale);
        System.out.println("effectivePrice: " + effectivePrice);
        return tokens;
    }

    /**
     * 卖出智能代币
     *
     * @param supply     智能代币总量
     * @param balance    准备金余额总量
     * @param cw         连接器权重
     * @param sellAmount 卖出智能代币数量
     * @return 获得准备金数量
     */
    public static Double calculateSell(Double supply, Double balance, Double cw, Double sellAmount) {
        // Double reserve = BancorFormula.mul0(balance, 1 - Math.pow(1 - BancorFormula.divide(sellAmount, supply), 1 / cw), 0);
        Double reserve = BancorFormula.mul(balance, Math.pow(1 + BancorFormula.divide(sellAmount, supply), 1 / cw) - 1, 0);
        BancorFormula.supply = BancorFormula.subtract(BancorFormula.supply, sellAmount);
        BancorFormula.balance = BancorFormula.subtract(BancorFormula.balance, reserve);
        BancorFormula.price = BancorFormula.divide(BancorFormula.balance, (BancorFormula.mul(BancorFormula.supply, cw)), BancorFormula.scale);
        BancorFormula.marketCap = BancorFormula.mul(BancorFormula.supply, BancorFormula.price);
        // 有效价格
        Double effectivePrice = BancorFormula.divide(reserve, sellAmount, BancorFormula.scale);
        System.out.println("effectivePrice: " + effectivePrice);
        return reserve;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input;
        while (scanner.hasNext() && !(input = scanner.nextLine()).equals("0")) {
            if (input.contains(",")) {
                // init state 初始化状态 300000,60000,0.2
                String[] initState = input.split(",");
                BancorFormula.supply = Double.parseDouble(initState[0]);
                BancorFormula.balance = Double.parseDouble(initState[1]);
                BancorFormula.cw = Double.parseDouble(initState[2]);
            } else {
                //
                String[] payNumers = input.split("-");
                // buy token
                if (StringUtils.isNotBlank(payNumers[0])) {
                    Double paid = Double.parseDouble(payNumers[0]);
                    if (paid > 0) {
                        System.out.println("buy tokens: " + BancorFormula.calculateBuy(BancorFormula.supply, BancorFormula.balance, BancorFormula.cw, paid));
                    }
                }
                // sell token
                if (payNumers.length > 1 && StringUtils.isNotBlank(payNumers[1])) {
                    Double sell = Double.parseDouble(payNumers[1]);
                    if (sell > 0) {
                        System.out.println("get reserve: " + BancorFormula.calculateSell(BancorFormula.supply, BancorFormula.balance, BancorFormula.cw, sell));
                    }
                }

                System.out.println("supply: " + StringUtils.format(BancorFormula.supply) + ", balance: " + StringUtils.format(BancorFormula.balance) + ", price: " + BancorFormula.price + ", marketCap: " + StringUtils.format(BancorFormula.marketCap));
            }
        }

    }

    /**
     * 提供精确的乘法运算
     *
     * @param dou1
     * @param dou2
     * @return
     */
    protected static double muls(String dou1, String dou2) {
        BigDecimal big1 = new BigDecimal(dou1);
        BigDecimal big2 = new BigDecimal(dou2);
        return big1.multiply(big2).doubleValue();
    }

    protected static double add(double dou1, double dou2) {
        BigDecimal big1 = new BigDecimal(dou1);
        BigDecimal big2 = new BigDecimal(dou2);
        return big1.add(big2).setScale(BancorFormula.scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    protected static double subtract(double dou1, double dou2) {
        BigDecimal big1 = new BigDecimal(dou1);
        BigDecimal big2 = new BigDecimal(dou2);
        return big1.subtract(big2).setScale(BancorFormula.scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    protected static double mul(double dou1, double dou2) {
        BigDecimal big1 = new BigDecimal(dou1);
        BigDecimal big2 = new BigDecimal(dou2);
        return big1.multiply(big2).setScale(BancorFormula.scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

//    protected static double divide(double dou1, double dou2,int scale) {
//        BigDecimal big1 = new BigDecimal(dou1);
//        BigDecimal big2 = new BigDecimal(dou2);
//        return big1.divide(big2, scale, RoundingMode.HALF_UP).doubleValue();
//    }

    protected static double divide(double dou1, double dou2) {
        BigDecimal big1 = new BigDecimal(dou1);
        BigDecimal big2 = new BigDecimal(dou2);
        return big1.divide(big2, BancorFormula.scale, RoundingMode.HALF_UP).doubleValue();
    }

    protected static double divide(double dou1, double dou2, int scale) {
        BigDecimal big1 = new BigDecimal(dou1);
        BigDecimal big2 = new BigDecimal(dou2);
        return big1.divide(big2, scale, RoundingMode.HALF_UP).doubleValue();
    }

    protected static double mul(double dou1, double dou2, int scale) {
        BigDecimal big1 = new BigDecimal(dou1);
        BigDecimal big2 = new BigDecimal(dou2);
        return big1.multiply(big2).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    protected static double divides(String dou1, String dou2) {
        BigDecimal big1 = new BigDecimal(dou1);
        BigDecimal big2 = new BigDecimal(dou2);
        return big1.divide(big2, BancorFormula.scale, RoundingMode.HALF_UP).doubleValue();
    }

}