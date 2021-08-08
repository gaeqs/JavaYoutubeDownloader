package io.github.gaeqs.javayoutubedownloader.util;

/**
 * An util class for numbers.
 */
public class NumericUtils {

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (Throwable ex) {
            return false;
        }
    }


    public static boolean isDouble(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (Throwable ex) {
            return false;
        }
    }


    public static boolean isFloat(String s) {
        try {
            Float.parseFloat(s);
            return true;
        } catch (Throwable ex) {
            return false;
        }
    }


    public static boolean isShort(String s) {
        try {
            Short.parseShort(s);
            return true;
        } catch (Throwable ex) {
            return false;
        }
    }


    public static boolean isLong(String s) {
        try {
            Long.parseLong(s);
            return true;
        } catch (Throwable ex) {
            return false;
        }
    }


    public static boolean isByte(String s) {
        try {
            Byte.parseByte(s);
            return true;
        } catch (Throwable ex) {
            return false;
        }
    }


    public static String toRomanNumeral(int input) {
        if (input < 1 || input > 3999) return "X";
        StringBuilder s = new StringBuilder();
        while (input >= 1000) {
            s.append("M");
            input -= 1000;
        }
        while (input >= 900) {
            s.append("CM");
            input -= 900;
        }
        while (input >= 500) {
            s.append("D");
            input -= 500;
        }
        while (input >= 400) {
            s.append("CD");
            input -= 400;
        }
        while (input >= 100) {
            s.append("C");
            input -= 100;
        }
        while (input >= 90) {
            s.append("XC");
            input -= 90;
        }
        while (input >= 50) {
            s.append("L");
            input -= 50;
        }
        while (input >= 40) {
            s.append("XL");
            input -= 40;
        }
        while (input >= 10) {
            s.append("X");
            input -= 10;
        }
        while (input >= 9) {
            s.append("IX");
            input -= 9;
        }
        while (input >= 5) {
            s.append("V");
            input -= 5;
        }
        while (input >= 4) {
            s.append("IV");
            input -= 4;
        }
        while (input >= 1) {
            s.append("I");
            input -= 1;
        }
        return s.toString();
    }


    public static int floor(double num) {
        int floor = (int) num;
        return (double) floor == num ? floor : floor - (int) (Double.doubleToRawLongBits(num) >>> 63);
    }
}

