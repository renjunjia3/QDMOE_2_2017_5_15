package com.mzhguqvn.mzhguq.util;

import java.text.DecimalFormat;


/**
 * Case By:小数工具类
 * package:com.mzhguqvn.mzhguq.util
 * Author：scene on 2017/5/25 15:07
 */

public class DecimalUtils {
    /**
     * 提供精确的小数位四舍五入处理.
     *
     * @param number 需要四舍五入的数字
     * @return 四舍五入后的结果 如4.00
     */
    public static String formatPrice2BlankToBlank(double number) {
        DecimalFormat df = new DecimalFormat("###,##0.00");
        return df.format(number);
    }

    /**
     * 提供精确的小数位四舍五入处理.
     *
     * @param number 需要四舍五入的数字
     * @return 四舍五入后的结果 例：4.00 显示为4
     */
    public static String toRound2(double number) {
        DecimalFormat df = new DecimalFormat("0.##");
        return df.format(number);
    }

}
