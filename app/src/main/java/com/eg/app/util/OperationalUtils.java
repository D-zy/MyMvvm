package com.eg.app.util;

import android.text.TextUtils;

import java.math.BigDecimal;
import java.util.ArrayList;

public class OperationalUtils {

    /**
     * @param params 必须都是整数相加减 "＋－×÷"
     * @return
     */
    public static String element(ArrayList<String> params) {
        int length = params.size();
        if (length == 0) return "0";
        String sum = "0";
        for (String param : params) {
            if (TextUtils.isEmpty(param)) param = "0";
            if (!sum.startsWith("－") && !param.startsWith("－")) { //正+正
                sum = calculateAdd(sum, param);
            } else if (!sum.startsWith("－") && param.startsWith("－")) { //正+负
                sum = calculateSub(sum, param.substring(1));
            } else if (sum.startsWith("－") && !param.startsWith("－")) { //负+正
                sum = calculateSub(param, sum.substring(1));
            } else if (sum.startsWith("－") && param.startsWith("－")) { //负+负
                sum = "－" + calculateAdd(sum.substring(1), param.substring(1));
            }
        }
        return sum;
    }

    public static String element2(ArrayList<String> params) {
        int length = params.size();
        if (length == 0) return "1";
        String sum = "1";
        for (String param : params) {
            if (param.startsWith("÷")) {
//                BigDecimal b1 = new BigDecimal(sum);
//                BigDecimal b2 = new BigDecimal(param.substring(1));
//                sum = b1.divide(b2).toString();
//                sum = A.divide(sum, param.substring(1)).get(0);
//                sum = Double.parseDouble(sum) / Double.parseDouble( param.substring(1)) + "";
                sum = division(sum, param.substring(1),10)+"";

            } else {
                sum = multiply(sum, param);
            }
        }
        return sum;
    }

    public static double division(String v1, String v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }




    public static String multiply(String str1, String str2) {
        int[] num1 = new int[str1.length()];
        int[] num2 = new int[str2.length()];
        int[] result = new int[str1.length() + str2.length()];

        //将两个字符串转成整型数组，顺序转换，数组下标越小，数字对应的位数越高
        for (int i = 0; i < str1.length(); i++) {
            num1[i] = Integer.parseInt(str1.substring(i, i + 1));
        }
        for (int i = 0; i < str2.length(); i++) {
            num2[i] = Integer.parseInt(str2.substring(i, i + 1));
        }

        //两大数相乘
        for (int a = 0; a < str1.length(); a++) {
            for (int b = 0; b < str2.length(); b++) {
                result[a + b] += num1[a] * num2[b];
            }
        }

        ////判断是否需要进位，满10进1,因为存储顺序与位数高低相反，所以采用逆序进位
        int temp;
        for (int k = result.length - 1; k > 0; k--) {
            temp = result[k] / 10;  //数组下标大的向数组下标小的进位
            result[k - 1] += temp;
            result[k] = result[k] % 10;
        }

        //将结果数组逆序转化为字符串
        String resultstr = "";
        for (int i = 0; i < result.length - 1; i++) {
            resultstr += "" + result[i];
        }

        return resultstr;
    }

    /**
     * 提供精确的加法运算
     *
     * @return 两个参数的和
     */
    public static String addBigDoubleNum(String numA, String numB) {
        if (TextUtils.isEmpty(numA) || TextUtils.isEmpty(numB)) {
            return "0.00";
        }
        String numA1 = "0" + numA.substring(numA.length() - 3);
        String numB1 = "0" + numB.substring(numB.length() - 3);
        double v1 = Double.parseDouble(numA1);
        double v2 = Double.parseDouble(numB1);
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        String res = b1.add(b2).doubleValue() + "";

        String num1 = numA.substring(0, numA.length() - 3);
        String num2 = numB.substring(0, numB.length() - 3);
        if (res.startsWith("1")) {
            return too(addBigIntNum(addBigIntNum("1", num1), num2) + res.substring(1));
        } else {
            return too(addBigIntNum(num1, num2) + res.substring(1));
        }
    }

    private static String too(String result) {
        if (result.contains(".")) {
            String s1 = result.split("\\.")[1];
            if (s1.length() == 1) {
                result = result + "0";
            }
        } else {
            result = result + ".00";
        }
        return result;
    }

    /**
     * @param numa 必须整数
     * @param numb 必须整数
     * @return
     */
    public static String addBigIntNum(String numa, String numb) {
        if (TextUtils.isEmpty(numa) || TextUtils.isEmpty(numb)) {
            return "0.00";
        }
        String num1 = numa.replaceAll(",", "");
        String num2 = numb.replaceAll(",", "");

        //1. String转为char数组
        //因为num1, num2 可能位数不一样，比如num1=123， num=1234, 翻转两个字符串后，个位十位相加比较方便
        char[] num1Chars = new StringBuffer(num1).reverse().toString().toCharArray();
        char[] num2Chars = new StringBuffer(num2).reverse().toString().toCharArray();
        int num1Length = num1Chars.length;
        int num2Length = num2Chars.length;
        //2. 在长的数组长度上加一来存新数组
        int maxLength = num1Length;
        if (num2Length > num1Length)
            maxLength = num2Length;
        int[] result = new int[maxLength + 1];
        //3. 对位相加
        for (int i = 0; i < result.length; i++) {
            // 如果当前的i超过了某个数组的长度，就用0代替高位了，和另一个字符数组中的数字相加
            int aint = i < num1Length ? (num1Chars[i] - '0') : 0;
            int bint = i < num2Length ? (num2Chars[i] - '0') : 0;
            //result[i]可能已经有值了，是前面一位运算(i-1)进位过来的，所以不能直接赋值，要 +=
            result[i] += aint + bint;
            //如果大于10的就向前一位进位，本身进行除10取余
            if (result[i] >= 10) {
                result[i + 1] += result[i] / 10;
                result[i] %= 10;
            }
        }
        //4. 存储最后的结果
        StringBuilder sb = new StringBuilder();
        //判断最高位是0还是1, 0无需保存
        if (result[result.length - 1] == 1)
            sb.append(1);
        for (int i = result.length - 2; i >= 0; i--) {
            sb.append(result[i]);
        }

        return sb.toString();
    }

    private static String tooSub(String result) {
        if (result.startsWith("-")) {
            result = result.replace("-", "");
            if (result.contains(".")) {
                String s1 = result.split("\\.")[1];
                if (s1.length() == 1) {
                    result = result + "0";
                }
            } else {
                result = result + ".00";
            }
            return "-" + result;
        } else {
            if (result.contains(".")) {
                String s1 = result.split("\\.")[1];
                if (s1.length() == 1) {
                    result = result + "0";
                }
            } else {
                result = result + ".00";
            }
            return result;
        }
    }

    public static final String SCALE_STR = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";


    public static String calculateAdd(String a, String b) {
        return calculateAdd(a, b, 10);
    }

    /**
     * 计算A+B的结果
     *
     * @param a     ,可为小数,或负数
     * @param b
     * @param scale 进制 支持2-62进制
     * @return
     */
    public static String calculateAdd(String a, String b, int scale) {
        //两个负数相加
        if (a.startsWith("-") && b.startsWith("-")) {
            // -1 + -1 ==> - (1+1)
            return "-" + calculateAdd(a.replace("-", ""),
                    b.replace("-", ""), scale);
        } else if (a.startsWith("-")) {
            //转减法 -1+1 ==>1-1
            return calculateSub(b, a.replace("-", ""), scale);
        } else if (b.startsWith("-")) {
            //转减法,1+-1 ==>1-1
            return calculateSub(a, b.replace("-", ""), scale);
        }
        String supportChar = SCALE_STR.substring(0, scale);
        //检查字符是否输入合理
        checkFormat(a, b, scale, SCALE_STR.length(), supportChar);
        //计算小数点后的位数
        //小数的位数
        int s = 0;
        int b1 = a.indexOf(".");
        int b2 = b.indexOf(".");
        b1 = b1 == -1 ? 0 : a.length() - b1 - 1;
        b2 = b2 == -1 ? 0 : b.length() - b2 - 1;
        //System.out.println("b1="+b1+",b2="+b2);
        //调整小数位
        if (b1 > 0 || b2 > 0) {
            s = b1 > b2 ? b1 : b2;
            if (b1 == 0) {
                a += ".";
            }
            while (b1 < s) {
                a += "0";
                b1++;
            }
            if (b2 == 0) {
                b += ".";
            }
            while (b2 < s) {
                b += "0";
                b2++;
            }
        }
        //System.out.println("a="+a+",b="+b);
        //运算过程中去除小数点
        a = a.replace(".", "");
        b = b.replace(".", "");
        char[] aC = a.toCharArray();
        char[] bC = b.toCharArray();

        int aL = aC.length;
        int bL = bC.length;
        int maxL = aL > bL ? aL : bL;
        int[] aI = new int[maxL + 1];
        int[] bI = new int[maxL + 1];
        //结果列
        int[] sI = new int[maxL + 1];

        //将a填入数组
        for (int i = 0; i < aL; i++) {
            aI[maxL - i] = supportChar.indexOf(aC[aL - i - 1]);
        }
        //将b填入数组
        for (int i = 0; i < bL; i++) {
            bI[maxL - i] = supportChar.indexOf(bC[bL - i - 1]);
        }
        //从个位数起,进行相同位数相加
        for (int i = 0; i < maxL + 1; i++) {
            int sum = aI[maxL - i] + bI[maxL - i];
            sI[maxL - i] = sum % scale;
            if (i < maxL) {
                //将进位的结果放在aI上
                aI[maxL - i - 1] += sum / scale;
            }
        }
        //调整返回结果
        return adjustResult(supportChar, s, sI);


    }

    public static String calculateSub(String a, String b) {
        return calculateSub(a, b, 10);
    }

    /**
     * 计算A-B的结果
     *
     * @param a     ,a
     * @param b
     * @param scale 进制 支持2-62进制
     * @return
     */
    public static String calculateSub(String a, String b, int scale) {

        if (a.startsWith("-") && b.startsWith("-")) {
            //-1 - -2 ==>2-1
            return calculateSub(b.replace("-", ""),
                    a.replace("-", ""), scale);
        } else if (a.startsWith("-")) {
            //转加法
            //  -1 - 1 ==> -(1+1)
            return "-" + calculateAdd(a.replace("-", ""), b, scale);
        } else if (b.startsWith("-")) {
            //转加法 1 - -1 ==> 1+1
            return calculateAdd(a, b.replace("-", ""), scale);
        }

        String supportChar = SCALE_STR.substring(0, scale);
        //检查字符是否输入合理
        checkFormat(a, b, scale, SCALE_STR.length(), supportChar);
        //计算小数点后的位数
        //小数的位数
        int s = 0;
        int b1 = a.indexOf(".");
        int b2 = b.indexOf(".");
        b1 = b1 == -1 ? 0 : a.length() - b1 - 1;
        b2 = b2 == -1 ? 0 : b.length() - b2 - 1;
        // System.out.println("b1="+b1+",b2="+b2);

        //调整小数位
        if (b1 > 0 || b2 > 0) {
            s = Math.max(b1, b2);
            if (b1 == 0) {
                a += ".";
            }
            StringBuilder aBuilder = new StringBuilder(a);
            while (b1 < s) {
                aBuilder.append("0");
                b1++;
            }
            a = aBuilder.toString();
            if (b2 == 0) {
                b += ".";
            }
            StringBuilder bBuilder = new StringBuilder(b);
            while (b2 < s) {
                bBuilder.append("0");
                b2++;
            }
            b = bBuilder.toString();
        }
        //System.out.println("a="+a+",b="+b);
        //运算过程中去除小数点
        a = a.replace(".", "");
        b = b.replace(".", "");
        char[] aC = a.toCharArray();
        char[] bC = b.toCharArray();

        int aL = aC.length;
        int bL = bC.length;
        int maxL = aL > bL ? aL : bL;
        int[] aI = new int[maxL];
        int[] bI = new int[maxL];
        //结果列
        int[] sI = new int[maxL];

        //将a填入数组
        for (int i = 0; i < aL; i++) {
            aI[maxL - i - 1] = supportChar.indexOf(aC[aL - i - 1]);
        }
        //将b填入数组
        for (int i = 0; i < bL; i++) {
            bI[maxL - i - 1] = supportChar.indexOf(bC[bL - i - 1]);
        }
        //比较aI和bI大小
        boolean isMaxA = true;
        for (int i = 0; i < maxL; i++) {
            if (aI[i] < bI[i]) {
                isMaxA = false;
                break;
            } else if (aI[i] > bI[i]) {
                break;
            }
        }
        for (int i = 0; i < maxL; i++) {
            int aS = aI[maxL - i - 1];
            int bS = bI[maxL - i - 1];
            if (isMaxA) {
                //够减,则直接减后的结果作为值
                if (aS >= bS) {
                    sI[maxL - i - 1] = aS - bS;
                } else {
                    aI[maxL - i - 2] -= 1;
                    sI[maxL - i - 1] = aS + scale - bS;
                }
            } else {
                if (bS >= aS) {
                    sI[maxL - i - 1] = bS - aS;
                } else {
                    bI[maxL - i - 2] -= 1;
                    sI[maxL - i - 1] = bS + scale - aS;
                }
            }
        }
        if (!isMaxA) {
            //如果a 小于b,则返回负数的结果
            return "-" + adjustResult(supportChar, s, sI);
        }
        //调整返回结果
        return adjustResult(supportChar, s, sI);
    }

    /**
     * 检查输入自否合理
     *
     * @param a
     * @param b
     * @param scale
     * @param maxScale    最大的进制数
     * @param supportChar
     */
    private static void checkFormat(String a, String b, int scale, int maxScale, String supportChar) {
        if (scale < 2 || scale > maxScale) {
            throw new IllegalArgumentException("不支持的进制");
        }
        //校验字符是否正确,例如2进制,最大数为1,包含2就是错误的输入

        for (char c : a.replace(".", "").toCharArray()) {
            if (supportChar.indexOf(c) == -1) {
                throw new IllegalArgumentException("a 输入字符错误");
            }
        }
        for (char c : b.replace(".", "").toCharArray()) {
            if (supportChar.indexOf(c) == -1) {
                throw new IllegalArgumentException("b 输入字符错误");
            }
        }
    }

    /**
     * 调整返回结果
     *
     * @param supportChar 支持的字符
     * @param s           小数点后的位数
     * @param sI          计算后的结果
     * @return
     */
    private static String adjustResult(String supportChar, int s, int[] sI) {
        StringBuilder sb = new StringBuilder();
        char[] charArray = supportChar.toCharArray();
        for (int i = 0; i < sI.length; i++) {
            sb.append(charArray[sI[i]]);
            if (s > 0 && sI.length - i == s + 1) {
                sb.append(".");
            }
        }


        String rs = sb.toString();
        //去掉小数点后位为0的小数
        if (s > 0) {
            while (rs.endsWith("0") && rs.contains(".") || rs.endsWith(".")) {
                rs = rs.substring(0, rs.length() - 1);
            }
        }
        //去掉首位为0的数
        while (rs.length() > 1 && rs.startsWith("0") && rs.indexOf(".") != 1) {
            rs = rs.substring(1);
        }

        return rs;
    }

    public static void main(String[] args) {
        //可选数0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ,
        // 例如16进制,首位数最大为f,2进制,首位数最大为1,10进制首位数最大为9
        //支持小数,负数
        String a = "-10000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
        //支持小数,负数
        String b = "1";
        //进制数,只支持2进制到62进制,如果需要更大的进制运算,可以加入特殊字符到scaleStr中
        int scale = 10;
        String s = calculateAdd(a, b, scale);
        System.out.println(a + "加" + b + "的结果为:" + s);
        String s2 = calculateSub(a, b, scale);
        System.out.println(a + "减" + b + "的结果为:" + s2);
    }

}
