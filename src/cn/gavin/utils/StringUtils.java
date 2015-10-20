package cn.gavin.utils;

/**
 * Created by gluo on 9/8/2015.
 */
public class StringUtils {
    public static String toHexString(String s) {
        String str = "";
        for (int i = 0; i < s.length(); i++) {
            int ch = (int) s.charAt(i);
            String s4 = Integer.toHexString(ch);
            str = str + s4;
        }
        return "0x" + str;//0x表示十六进制
    }

    //转换十六进制编码为字符串
    public static String toStringHex(String s) {
        if ("0x".equals(s.substring(0, 2))) {
            s = s.substring(2);
        }
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(
                        i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            s = new String(baKeyword, "utf-16");//UTF-16le:Not
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return s;
    }

    public static boolean isNotEmpty(String countStr) {
        return countStr !=null && !countStr.trim().isEmpty() && !"null".equalsIgnoreCase(countStr);
    }

    public static String[] split(String str,String regularExpression){
        if(isNotEmpty(str)){
            return str.split(regularExpression);
        }else{
            return new String[]{""};
        }
    }

    public static Long toLong(String number){
        try {
            return Long.parseLong(number);
        }catch (Exception e){
            try {
                return Double.valueOf(number).longValue();
            }catch (Exception e1){
                return 1l;
            }
        }
    }

    public static void main(String...args){
        System.out.print(toStringHex("0x6c81739f"));
    }
}
