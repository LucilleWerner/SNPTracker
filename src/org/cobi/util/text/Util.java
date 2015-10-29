//Source code of HaploView4.0, Copied in April 2008
//Modified by MXLi in May 2008 
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cobi.util.text;

import java.io.File;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author Miaoxin Li
 */
public class Util {

    private static ThreadLocal<String[]> tempArray = new ThreadLocal<String[]>();
    //This cannot improve the speed
    //  private static String[] temp = null;

    public static String[] tokenize(String string, char delimiter) {     
        String[] temp = tempArray.get();
        int tempLength = (string.length() / 2) + 1;

        if (temp == null || temp.length < tempLength) {
            temp = new String[tempLength];
            tempArray.set(temp);
        }


        int wordCount = 0;
        int i = 0;
        int j = string.indexOf(delimiter);

        while (j >= 0) {
            temp[wordCount++] = string.substring(i, j);
            i = j + 1;
            j = string.indexOf(delimiter, i);
        }

        if (i < string.length()) {
            temp[wordCount++] = string.substring(i);
        }

        String[] result = new String[wordCount];
        System.arraycopy(temp, 0, result, 0, wordCount);
        return result;
    }

    public static String[] tokenize(String string, char delimiter, int maxIndex) {
        String[] temp = tempArray.get();
        int tempLength = (string.length() / 2) + 1;

        if (temp == null || temp.length < tempLength) {
            temp = new String[tempLength];
            tempArray.set(temp);
        }


        int wordCount = 0;
        int i = 0;
        int j = string.indexOf(delimiter);

        while (j >= 0) {
            temp[wordCount] = string.substring(i, j);
            if (wordCount >= maxIndex) {
                wordCount++;
                break;
            }
            wordCount++;
            i = j + 1;
            j = string.indexOf(delimiter, i);

        }
        if (wordCount <= maxIndex) {
            if (i < string.length()) {
                temp[wordCount++] = string.substring(i);
            }
        }

        String[] result = new String[wordCount];
        System.arraycopy(temp, 0, result, 0, wordCount);
        return result;
    }

    public static String[] tokenizeIngoreConsec(String string, char delimiter) {
        String[] temp = tempArray.get();
        int tempLength = (string.length() / 2) + 1;

        if (temp == null || temp.length < tempLength) {
            temp = new String[tempLength];
            tempArray.set(temp);
        }


        int wordCount = 0;
        int i = 0;
        int j = string.indexOf(delimiter);

        while (j >= 0) {
            if (i < j) {
                temp[wordCount++] = string.substring(i, j);
            }
            i = j + 1;
            j = string.indexOf(delimiter, i);
        }

        if (i < string.length()) {
            temp[wordCount++] = string.substring(i);
        }

        String[] result = new String[wordCount];
        System.arraycopy(temp, 0, result, 0, wordCount);
        return result;
    }

    public static String[] tokenizeIngoreConsec(String string, char delimiter, int maxIndex) {
        String[] temp = tempArray.get();
        int tempLength = (string.length() / 2) + 1;

        if (temp == null || temp.length < tempLength) {
            temp = new String[tempLength];
            tempArray.set(temp);
        }


        int wordCount = 0;
        int i = 0;
        int j = string.indexOf(delimiter);

        while (j >= 0) {
            if (i < j) {
                temp[wordCount] = string.substring(i, j);
                if (wordCount >= maxIndex) {
                    wordCount++;
                    break;
                }
            }
            i = j + 1;
            j = string.indexOf(delimiter, i);
        }
        if (wordCount <= maxIndex) {
            if (i < string.length()) {
                temp[wordCount++] = string.substring(i);
            }
        }

        String[] result = new String[wordCount];
        System.arraycopy(temp, 0, result, 0, wordCount);
        return result;
    }

    public static void main(String[] args) {
        try {
            int runs = 10000000;
            String val = "" + Math.PI;
            val = "0.923423423423444334343444323432e+9";
            System.out.println(Util.parseFloat(val));
            long start = System.nanoTime();
            for (int i = 0; i < runs; i++) {
                Float.parseFloat(val);
            }
            long time = (System.nanoTime() - start) / runs;
            System.out.println("Average Float.parseFloat() time was " + time + " ns.");

            long start2 = System.nanoTime();
            for (int i = 0; i < runs; i++) {
                Double.parseDouble(val);
            }
            long time2 = (System.nanoTime() - start2) / runs;
            System.out.println("Average Double.parseDouble() time was " + time2 + " ns.");

            start2 = System.nanoTime();
            for (int i = 0; i < runs; i++) {
                Util.parseFloat(val);
            }
            time2 = (System.nanoTime() - start2) / runs;
            System.out.println("Average Util.parseFloat() time was " + time2 + " ns.");
            val = "" + (int) (Math.PI * 100000);
            start2 = System.nanoTime();
            for (int i = 0; i < runs; i++) {
                Integer.parseInt(val);
            }
            time2 = (System.nanoTime() - start2) / runs;
            System.out.println("Average Integer.parseInt() time was " + time2 + " ns.");
            start2 = System.nanoTime();
            for (int i = 0; i < runs; i++) {
                Util.parseInt(val);
            }
            time2 = (System.nanoTime() - start2) / runs;
            System.out.println("Average Util.parseInt() time was " + time2 + " ns.");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public static int parseInt(final String s) {
        // Check for a sign.
        int num = 0;
        int sign = -1;
        final int len = s.length();
        final char ch = s.charAt(0);
        int i = 1;
        if (ch == '-') {
            sign = 1;
        } else {
            num = '0' - ch;
        }

        // Build the number.

        while (i < len) {
            num = num * 10 + '0' - s.charAt(i++);
        }
        return sign * num;
    }

    public static float parseFloat(String f) {
        final int len = f.length();
        float ret = 0f;         // return value
        int pos = 0;          // read pointer position
        int part = 0;          // the current part (int, float and sci parts of the number)
        boolean neg = false;      // true if part is a negative number
        // the max long is 2147483647
        final int MAX_INT_BIT = 9;
        // find start
        while (pos < len && (f.charAt(pos) < '0' || f.charAt(pos) > '9') && f.charAt(pos) != '-' && f.charAt(pos) != '.') {
            pos++;
        }


        // sign
        if (f.charAt(pos) == '-') {
            neg = true;
            pos++;
        }

        // integer part
        while (pos < len && !(f.charAt(pos) > '9' || f.charAt(pos) < '0')) {
            part = part * 10 + (f.charAt(pos++) - '0');
        }
        ret = neg ? (float) (part * -1) : (float) part;

        // float part
        if (pos < len && f.charAt(pos) == '.') {
            pos++;
            int mul = 1;
            part = 0;
            int num = 0;
            while (pos < len && !(f.charAt(pos) > '9' || f.charAt(pos) < '0')) {
                num++;
                if (num <= MAX_INT_BIT) {
                    part = part * 10 + (f.charAt(pos) - '0');
                    mul *= 10;
                }
                pos++;
            }
            ret = neg ? ret - (float) part / (float) mul : ret + (float) part / (float) mul;
        }

        // scientific part
        if (pos < len && (f.charAt(pos) == 'e' || f.charAt(pos) == 'E')) {
            pos++;
            neg = (f.charAt(pos) == '-');
            pos++;
            part = 0;
            while (pos < len && !(f.charAt(pos) > '9' || f.charAt(pos) < '0')) {
                part = part * 10 + (f.charAt(pos++) - '0');
            }
            if (neg) {
                ret = ret / (float) Math.pow(10, part);
            } else {
                ret = ret * (float) Math.pow(10, part);
            }
        }
        return ret;
    }

    //reference http://www.boekhoff.info/?pid=data&dat=fasta-codes
    /*
     * Code 	Meaning 	Etymology 	Complement 	Opposite
    A 	A 	Adenosine 	T 	B
    T/U 	T 	Thymidine/Uridine 	A 	V
    G 	G 	Guanine 	C 	H
    C 	C 	Cytidine 	G 	D
    K 	G or T 	Keto 	M 	M
    M 	A or C 	Amino 	K 	K
    R 	A or G 	Purine 	Y 	Y
    Y 	C or T 	Pyrimidine 	R 	R
    S 	C or G 	Strong 	S 	W
    W 	A or T 	Weak 	W 	S
    B 	C or G or T 	not A (B comes after A) 	V 	A
    V 	A or C or G 	not T/U (V comes after U) 	B 	T/U
    H 	A or C or T 	not G (H comes after G) 	D 	G
    D 	A or G or T 	not C (D comes after C) 	H 	C
    X/N 	G or A or T or C 	any 	N 	.
    . 	not G or A or T or C 		. 	N
    - 	gap of indeterminate length 			
     * 
     */
    public static char getIUPACambiguityCodesChar(String seq) {
        int size = seq.length();
        if (size == 0) {
            return 'N';
        } else if (size == 1) {
            return seq.charAt(0);
        }
        char[] eles = new char[size];
        for (int i = 0; i < size; i++) {
            eles[i] = seq.charAt(i);
        }
        Arrays.sort(eles);
        if (eles[0] == 'G' && eles[1] == 'T') {
            return 'K';
        } else if (eles[0] == 'A' && eles[1] == 'C') {
            return 'M';
        } else if (eles[0] == 'A' && eles[1] == 'G') {
            return 'R';
        } else if (eles[0] == 'C' && eles[1] == 'T') {
            return 'Y';
        } else if (eles[0] == 'C' && eles[1] == 'G') {
            return 'S';
        } else if (eles[0] == 'A' && eles[1] == 'T') {
            return 'W';
        } else if (eles[0] == 'C' && eles[1] == 'G' && eles[2] == 'T') {
            return 'B';
        } else if (eles[0] == 'A' && eles[1] == 'C' && eles[2] == 'G') {
            return 'V';
        } else if (eles[0] == 'A' && eles[1] == 'C' && eles[2] == 'T') {
            return 'H';
        } else if (eles[0] == 'A' && eles[1] == 'G' && eles[2] == 'T') {
            return 'D';
        }
        return 'N';
    }

    public static String getByIUPACambiguityCode(char code) {
        switch (code) {
            case 'N':
                return "X/X";
            case 'A':
                return "A/A";
            case 'T':
                return "T/T";
            case 'G':
                return "G/G";
            case 'C':
                return "C/C";
            case 'K':
                return "G/T";
            case 'M':
                return "A/C";
            case 'R':
                return "A/G";
            case 'Y':
                return "C/T";
            case 'S':
                return "C/G";
            case 'W':
                return "A/T";
            default:
                return "X/X";
        }
    }

    public static char getComplementalChar(char ch1) {
        switch (ch1) {
            case 'A':
                return 'T';
            case 'T':
                return 'A';
            case 'G':
                return 'C';
            case 'C':
                return 'G';
            case 'R':
                return 'Y';
            case 'Y':
                return 'R';
            default:
                return ch1;
        }
    }

    public static String getReverseComplementalSquences(String str1) {
        if (str1 == null || (str1.length() == 0)) {
            return null;
        }
        int len = str1.length();
        StringBuilder complement = new StringBuilder();
        complement.setLength(len);
        len--;
        for (int i = len; i >= 0; i--) {
            switch (str1.charAt(i)) {
                case 'A':
                    complement.setCharAt(len - i, 'T');
                    break;
                case 'T':
                    complement.setCharAt(len - i, 'A');
                    break;
                case 'G':
                    complement.setCharAt(len - i, 'C');
                    break;
                case 'C':
                    complement.setCharAt(len - i, 'G');
                    break;
                case 'R':
                    complement.setCharAt(len - i, 'Y');
                    break;
                case 'Y':
                    complement.setCharAt(len - i, 'R');
                    break;
                case '[':
                    complement.setCharAt(len - i, ']');
                    break;
                case ']':
                    complement.setCharAt(len - i, '[');
                    break;
                default:
                    complement.setCharAt(len - i, str1.charAt(i));
            }
        }
        return complement.toString();
    }

    public static String formatPValue(double pval) {
        DecimalFormat df;
        //java truly sucks for simply restricting the number of sigfigs but still
        //using scientific notation when appropriate
        /*
        if (pval < 0.0001) {
        df = new DecimalFormat("0.000E0", new DecimalFormatSymbols(Locale.US));
        } else {
        df = new DecimalFormat("0.0000000", new DecimalFormatSymbols(Locale.US));
        }
         */
        df = new DecimalFormat("0.00E0", new DecimalFormatSymbols(Locale.US));
        String formattedNumber = df.format(pval, new StringBuffer(), new FieldPosition(NumberFormat.INTEGER_FIELD)).toString();
        return formattedNumber;
    }

    /**
     * Rounds a double and converts it into String.
     *
     * @param value the double value
     * @param afterDecimalPoint the (maximum) number of digits permitted
     * after the decimal point
     * @return the double as a formatted string
     */
    public static String doubleToString(double value, int afterDecimalPoint) {

        StringBuffer stringBuffer;
        double temp;
        int i, dotPosition;
        long precisionValue;

        temp = value * Math.pow(10.0, afterDecimalPoint);
        if (Math.abs(temp) < Long.MAX_VALUE) {
            precisionValue = (temp > 0) ? (long) (temp + 0.5)
                    : -(long) (Math.abs(temp) + 0.5);
            if (precisionValue == 0) {
                stringBuffer = new StringBuffer(String.valueOf(0));
            } else {
                stringBuffer = new StringBuffer(String.valueOf(precisionValue));
            }
            if (afterDecimalPoint == 0) {
                return stringBuffer.toString();
            }
            dotPosition = stringBuffer.length() - afterDecimalPoint;
            while (((precisionValue < 0) && (dotPosition < 1))
                    || (dotPosition < 0)) {
                if (precisionValue < 0) {
                    stringBuffer.insert(1, '0');
                } else {
                    stringBuffer.insert(0, '0');
                }
                dotPosition++;
            }
            stringBuffer.insert(dotPosition, '.');
            if ((precisionValue < 0) && (stringBuffer.charAt(1) == '.')) {
                stringBuffer.insert(1, '0');
            } else if (stringBuffer.charAt(0) == '.') {
                stringBuffer.insert(0, '0');
            }
            int currentPos = stringBuffer.length() - 1;
            while ((currentPos > dotPosition)
                    && (stringBuffer.charAt(currentPos) == '0')) {
                stringBuffer.setCharAt(currentPos--, ' ');
            }
            if (stringBuffer.charAt(currentPos) == '.') {
                stringBuffer.setCharAt(currentPos, ' ');
            }

            return stringBuffer.toString().trim();
        }
        return String.valueOf(value);
    }

    /**
     * Rounds a double and converts it into a formatted decimal-justified String.
     * Trailing 0's are replaced with spaces.
     *
     * @param value the double value
     * @param width the width of the string
     * @param afterDecimalPoint the number of digits after the decimal point
     * @return the double as a formatted string
     */
    public static String doubleToString(double value, int width,
            int afterDecimalPoint) {

        String tempString = doubleToString(value, afterDecimalPoint);
        char[] result;
        int dotPosition;

        if ((afterDecimalPoint >= width)
                || (tempString.indexOf('E') != -1)) { // Protects sci notation
            return tempString;
        }

        // Initialize result
        result = new char[width];
        for (int i = 0; i < result.length; i++) {
            result[i] = ' ';
        }

        if (afterDecimalPoint > 0) {
            // Get position of decimal point and insert decimal point
            dotPosition = tempString.indexOf('.');
            if (dotPosition == -1) {
                dotPosition = tempString.length();
            } else {
                result[width - afterDecimalPoint - 1] = '.';
            }
        } else {
            dotPosition = tempString.length();
        }


        int offset = width - afterDecimalPoint - dotPosition;
        if (afterDecimalPoint > 0) {
            offset--;
        }

        // Not enough room to decimal align within the supplied width
        if (offset < 0) {
            return tempString;
        }

        // Copy characters before decimal point
        for (int i = 0; i < dotPosition; i++) {
            result[offset + i] = tempString.charAt(i);
        }

        // Copy characters after decimal point
        for (int i = dotPosition + 1; i < tempString.length(); i++) {
            result[offset + i] = tempString.charAt(i);
        }

        return new String(result);
    }

    public static double roundDouble(double d, int places) {
        double factor = Math.pow(10, places);
        return Math.rint(d * factor) / factor;
    }

    public static String getTimeString() {
        /*
        Calendar c = Calendar.getInstance();
        int intYear = c.get(Calendar.YEAR);
        int intMonth = c.get(Calendar.MONTH+1);
        int intDate = c.get(Calendar.DATE);
        int intHour = c.get(Calendar.HOUR_OF_DAY);
        int intMinute = c.get(Calendar.MINUTE);
        int intSecond = c.get(Calendar.SECOND);
        StringBuffer str = new StringBuffer();
        str.append(intYear);
        str.append('_');
        str.append(intMonth);
        str.append('_');
        str.append(intDate);
        str.append('_');
        str.append(intHour);
        str.append('_');
        str.append(intMinute);
        str.append('_');
        str.append(intSecond);
        return str.toString();
         */

        Date d = new Date(System.currentTimeMillis());
        //SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        SimpleDateFormat format = new SimpleDateFormat("MM_dd_HH_mm_ss");
        return format.format(d);
    }

    public static boolean makeStorageLoc(String filePath) throws Exception {
        if (filePath == null) {
            return false;
        }

        File nwFile = new File(filePath);
        if (!nwFile.exists()) {
            if (!nwFile.mkdirs()) {
                return false;
            }
        }
        return true;
    }

    /**
     * support Numeric format:<br>
     * "33" "+33" "033.30" "-.33" ".33" " 33." " 000.000 "
     * @param str String
     * @return boolean
     */
    public static boolean isNumeric(String str) {
        int begin = 0;
        boolean once = true;
        if (str == null || str.trim().equals("")) {
            return false;
        }
        str = str.trim();
        if (str.startsWith("+") || str.startsWith("-")) {
            if (str.length() == 1) {
                // "+" "-"
                return false;
            }
            begin = 1;
        }
        //scientific formate like "2.7266453405784747E-4"
        if (str.indexOf("E-") >= 0) {
            str = str.replaceAll("E-", "");
        } else if (str.indexOf("e-") >= 0) {
            str = str.replaceAll("e-", "");
        } else if (str.indexOf("E+") >= 0) {
            str = str.replaceAll("E+", "");
        } else if (str.indexOf("e+") >= 0) {
            str = str.replaceAll("e+", "");
        } else if (str.indexOf("E") >= 0) {
            str = str.replaceAll("E", "");
        } else if (str.indexOf("e") >= 0) {
            str = str.replaceAll("e", "");
        }
        if (str.length() == 0) {
            return false;
        }
        for (int i = begin; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                if (str.charAt(i) == '.' && once) {
                    // '.' can only once
                    once = false;
                } else {
                    return false;
                }
            }
        }
        if (str.length() == (begin + 1) && !once) {
            // "." "+." "-."
            return false;
        }
        return true;

    }

    /**
     * support Numeric format:<br>
     * "33" "+33" "033.30" "-.33" ".33" " 33." " 000.000 "
     * @param str String
     * @return boolean
     */
    public static boolean isNumericSimple(StringBuilder str) {
        int begin = 0;
        boolean once = true;
        if (str == null || str.length() == 0) {
            return false;
        }

        for (int i = begin; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                if (str.charAt(i) == '.' && once) {
                    // '.' can only once
                    once = false;
                } else {
                    return false;
                }
            }
        }
        if (str.length() == (begin + 1) && !once) {
            // "." "+." "-."
            return false;
        }
        return true;

    }
}
