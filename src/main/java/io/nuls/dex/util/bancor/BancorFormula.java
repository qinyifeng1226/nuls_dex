package io.nuls.dex.util.bancor;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * 类描述：
 *
 * @author qinyifeng
 * @version v1.0
 * @date 2019/6/26 21:31
 */
public class BancorFormula {

    /**
     * connector token：如：eth、btc
     * smart token：可以连接多个connector token，此时smart token也叫转换token，用于connector token之间的转换
     * balance：= price * supply
     */
    static BigInteger ONE = BigInteger.valueOf(1L);
    static BigInteger supply = BigInteger.valueOf(10000L);
    static BigInteger balance = BigInteger.valueOf(2500L);
    static BigInteger price = BigInteger.valueOf(1L);
    static Double cw = 0.5;

    //function calculatePurchaseReturn(uint256 _supply, uint256 _connectorBalance, uint32 _connectorWeight, uint256 _depositAmount) public view returns (uint256);
    //function calculateSaleReturn(uint256 _supply, uint256 _connectorBalance, uint32 _connectorWeight, uint256 _sellAmount) public view returns (uint256);

    public static BigInteger calculateBuy(BigInteger supply, BigInteger balance, Double cw, BigInteger paid) {
        //Double a = (1 + paid.divide(balance).doubleValue());
        System.out.println(paid.divideAndRemainder(balance));
        BigInteger[] ssss=paid.divideAndRemainder(balance);
        Double ss=ssss[0].add(ssss[1]).doubleValue();
        System.out.println(ss);
        //System.out.println((String) (Math.pow(1 + paid.divide(balance).doubleValue(), cw)));
        new BigInteger("1.1");
        BigInteger tokens = supply.multiply(new BigInteger(String.valueOf(Math.pow(1 + paid.divide(balance).doubleValue(), cw))).subtract(ONE));
        return tokens;
    }

    public static BigInteger calculateSell(BigInteger supply, BigInteger balance, Double cw, BigInteger sellAmount) {
        //$paid = $balance * (1 - pow(1 - $token / $supply, 1 / $cw));
        BigInteger tokens = balance.multiply(new BigInteger(String.valueOf(1 - Math.pow(1 - sellAmount.divide(supply).doubleValue(), 1 / cw))));
        return tokens;
    }

    public static void main(String[] args) {
        System.out.println(BancorFormula.calculateBuy(supply,balance,cw,BigInteger.valueOf(100L)));
    }

//    public function buy() {
//        $supply = $this -> get["supply"];
//        $balance = $this -> get["balance"];
//        $paid = $this -> get["paid"];
//        $cw = 0.5;
//
//        $token = $supply * (pow(1 + $paid / $balance, $cw) - 1);
//        $price = $paid / $token;
//
//        ApiFunc::api_export ([
//                "token" = > $token,
//                "priceBefore" =>$balance / $supply / $cw,
//                "priceEnd" =>$price
//        ]);
//    }
//
//    public function sell() {
//        $cw = 0.5;
//        $supply = $this -> get["supply"];
//        $balance = $this -> get["balance"];
//        $token = $this -> get["token"];
//
//        $paid = $balance * (1 - pow(1 - $token / $supply, 1 / $cw));
//        $price = $paid / $token;
//        ApiFunc::api_export ([
//                "paid" = > $paid,
//                "priceBefore" =>$price,
//                "priceEnd" =>($balance - $paid) / ($supply - $token) / $cw
//        ]);
//    }

}