package com.eg.app.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class A {
    //@author Barry Wang
// all the four method have not considered the negative number yet.
// all the four method are implemented by manual calculate method.

    public static void main(String[] age) {
        String x = "6465708680075747657477";
        String y = "676894863054305436353";

        String a = add(x, y);
        System.out.println("1. x add y result is:" + a);
        String s = subtract(x, y);
        System.out.println("2. x subtract y result is:" + s);
        String m = multiply(x, y);
        System.out.println("3. x multiply y result is:" + m);
        List<String> d = divide(x, y);
        System.out.println("4. y divide x quotient is:" + d.get(0) + " reminder is:" + d.get(1));
    }

    //1. add----the method are implemented by manual calculate method.
    public static String add(String x, String y) {
        if (isNullAndNotNumber(x, y)) {
            return null;
        }

        if (x.equals("0")) {
            return y;
        }
        if (y.equals("0")) {
            return x;
        }

        if (x.length() > y.length()) {
            String tmp = x;
            x = y;
            y = tmp;
        }

        x = addZeroToFirst(x, y.length());
        String z = addHelp(x, y);
        return z;
    }

    //2. subtract----the method are implemented by manual subtract calculate method.
    public static String subtract(String x, String y) {
        if (isNullAndNotNumber(x, y)) {
            return null;
        }

        if (x.equals("0")) {
            return "-" + y;
        }
        if (y.equals("0")) {
            return x;
        }

        String sign = "";

        if (!isBig(x, y)) {
            sign = "-";
            String tmp = x;
            x = y;
            y = tmp;
        }

        int len = x.length();
        int diffLen = len - y.length();
        for (int i = 0; i < diffLen; i++) {
            y = "0" + y;
        }

        int[] a = toIntArray(x);
        int[] b = toIntArray(y);
        int[] c = new int[len];

        int borrow = 0;
        int result = 0;
        int i = len - 1;
        while (i >= 0) {
            result = a[i] - b[i] + borrow;
            borrow = 0;
            if (result < 0) {
                borrow = -1;
                result += 10;
            }

            c[i--] = result;
        }

        StringBuffer sb = new StringBuffer(32);
        for (int j = 0; j < len; j++) {
            if (c[j] == 0 && sb.length() == 0) {
                continue;
            } else {
                sb.append(c[j]);
            }
        }

        return sign + sb.toString();
    }

    //3. multiply----the method are implemented by manual multiply calculate method.
    public static String multiply(String x, String y) {
        if (isNullAndNotNumber(x, y)) {
            return null;
        }

        if (x.equals("0") || y.equals("0")) {
            return "0";
        }

        int[] a = toIntArray(x);
        int[] b = toIntArray(y);

        int[] temp1 = null;
        int[] temp2 = null;

        int enter = 0;
        int result = 0;
        int count = 1;
        for (int i = (b.length - 1); i >= 0; i--) {
            temp1 = new int[a.length + (++count)];
            enter = 0;
            for (int j = a.length - 1; j >= 0; j--) {
                result = a[j] * b[i] + enter;
                temp1[j + 2] = result;
                enter = result / 10;
            }
            temp1[1] = enter;
            temp1[0] = 0;
            temp2 = addIntArray(temp1, temp2);
        }

        StringBuffer sb = new StringBuffer(32);
        for (int j = 0; j < temp2.length; j++) {
            if (temp2[j] == 0 && sb.length() == 0) {
                continue;
            } else {
                sb.append(temp2[j]);
            }
        }

        return sb.toString();
    }

    //4. divide----the method are implemented by manual divide calculate method.
    public static List<String> divide(String x, String y) {
        if (isNullAndNotNumber(x, y)) {
            return null;
        }

        List<String> returnList = new ArrayList<String>();
        if (y.equals("0") || x.equals("0")) {
            returnList.add("0");
            returnList.add("0");
            return returnList;
        }

        String quotient = "";//quotient
        String remainder = "";//remainder
        if (isBig(x, y) == false) {
            remainder = y;
            quotient = "0";

            returnList.add(quotient);
            returnList.add(remainder);
            return returnList;
        }
        int i = y.length();
        remainder = x.substring(0, i);

        do {
            for (int j = 9; j >= 1; j--) {
                if ((isBig(remainder, multiply(y, Integer.valueOf(j).toString())) == false) && (isBig(remainder, multiply(y, Integer.valueOf(j - 1).toString())) == true)) {
                    if ((j - 1) > 0) {
                        quotient += (j - 1);
                    }
                    remainder = subtract(remainder, multiply(y, Integer.valueOf(j - 1).toString()));
                    break;
                }
            }
            int len = remainder.length();
            for (int k = 0; (k < y.length() - len) && (i < x.length()); k++) {
                remainder += x.charAt(i);
                i++;
                if (isBig(remainder, y) == false) {
                    quotient += "0";
                }
            }
            if ((isBig(remainder, y) == false) && (i < x.length())) {
                remainder += x.charAt(i);
                i++;
            }
        } while (i < x.length());
        for (int j = 9; j >= 1; j--) {
            if ((isBig(remainder, multiply(y, Integer.valueOf(j).toString())) == false) && (isBig(remainder, multiply(y, Integer.valueOf(j - 1).toString())) == true)) {
                if ((j - 1) > 0) {
                    quotient += (j - 1);
                }
                remainder = subtract(remainder, multiply(y, Integer.valueOf(j - 1).toString()));
                break;
            }
        }

        returnList.add(quotient);
        returnList.add(remainder);
        return returnList;
    }

    private static boolean isNullAndNotNumber(String x, String y) {
        if (x == null || y == null) {
            return true;
        }

        if (!isNumeric(x) || !isNumeric(y)) {
            return true;
        }

        return false;
    }

    //if x > y return true.
    private static boolean isBig(String x, String y) {
        return (x.length() > y.length() || (x.length() == y.length() && x.compareTo(y) > 0));
    }

    public static String addHelp(String x, String y) {
        String z = "";
        int len = x.length();
        int[] a = toIntArray(x);
        int[] b = toIntArray(y);
        int[] c = addIntArray(a, b);

        StringBuilder sb = new StringBuilder(32);
        for (int i = 0; i <= len; i++) {
            sb.append(c[i]);
        }
        if (c[0] == 0) {//delete the first '0' in result string
            z = sb.substring(1);
        } else {
            z = sb.toString();
        }
        return z;
    }

    // {2, 3, 4}+{6, 7} = {3, 0, 1}
    private static int[] addIntArray(int[] a, int b[]) {
        if (a == null) {
            return b;
        }

        if (b == null) {
            return a;
        }

        if (a.length != b.length) {//add "0" before the less length array.
            int[] temp = null;
            if (a.length < b.length) {
                temp = a;
                a = b;
                b = temp;
            }
            temp = new int[a.length];
            for (int i = a.length - 1, j = b.length - 1; i >= 0; i--) {
                if (j >= 0) {
                    temp[i] = b[j--];
                } else {
                    temp[i] = 0;
                }
            }
            b = temp;
        }


        int len = a.length;
        int[] c = new int[len + 1];
        int d = 0;//to carry. No need to use int[]
        for (int i = 0; i < len; i++) {
            int tmpSum = a[len - 1 - i] + b[len - 1 - i] + d;
            c[len - i] = tmpSum;
            d = tmpSum / 10;
        }
        c[0] = d;

        return c;
    }


    //String - toCharArray - toIntArray
    public static int[] toIntArray(String str) {
        int len = str.length();
        int[] result = new int[len];
        for (int i = 0; i < len; i++) {
            result[i] = str.charAt(i) - '0';
        }
        return result;
    }

    //("123",5)-->"00123"
    public static String addZeroToFirst(String str, int length) {
        StringBuilder sb = new StringBuilder();
        int diff = length - str.length();
        while (diff > 0) {
            sb.append("0");
            diff--;
        }
        sb.append(str);
        return sb.toString();
    }

    public static boolean isNumeric(String str) {
        Pattern p = Pattern.compile("[0-9]*");
        Matcher isNum = p.matcher(str);
        return isNum.matches();
    }
}

