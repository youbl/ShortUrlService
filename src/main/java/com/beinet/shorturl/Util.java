package com.beinet.shorturl;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

public class Util {
    private static String _arrChars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static Map<Character, Integer> ArrBitNum;

    // 用于生成短码
    private static Random random = new Random();

    static {
        ArrBitNum = InitArr();
    }


    /**
     * 获取url里的id，比如 ?abcd
     *
     * @param request
     * @return null或读取到的id
     */
    public static String getId(HttpServletRequest request) {
        if (request == null)
            return null;
        for (Map.Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
            String ret = entry.getKey();
            String[] values = entry.getValue();
            if (values == null || (values.length == 1 && values[0].length() <= 0))
                return ret;
        }
        return null;
    }

    /**
     * 返回客户端的IP和代理IP。
     *
     * @param request 请求上下文
     * @return IP
     */
    public static String getClientIP(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        String addr = request.getRemoteAddr();
        sb.append(addr == null ? "" : addr).append(";");
        addr = request.getHeader("X-Real-IP");
        sb.append(addr == null ? "" : addr).append(";");
        addr = request.getHeader("X-Forwarded-For");
        sb.append(addr == null ? "" : addr).append(";");
        return sb.toString();
    }

    /**
     * 验证给出的字符串，是否url网址格式
     *
     * @param url 字符串
     * @return 是否
     */
    public static boolean isUrl(String url) {
        if (url == null || url.length() <= 0)
            return false;

        Pattern regex = Pattern.compile("(?i)https?://[^\\s]+\\.[^\\s]+");
        return regex.matcher(url).matches();
    }

    /**
     * 把62进制转换为十进制
     *
     * @param str 62进制的字符串
     * @return 十进制数
     */
    public static long ConvertToNum(String str) throws Exception {
        return ConvertToNum(str, _arrChars.length());
    }

    /**
     * 把n进制转换为十进制
     *
     * @param str n进制的字符串
     * @param n   n进制
     * @return 十进制数
     */
    public static long ConvertToNum(String str, int n) throws Exception {
        if (n <= 0)
            n = _arrChars.length();

        long ret = 0L;
        if (str == null || (str = str.trim()).length() <= 0)
            return ret;
        //1234 = 1* 62^(4-1-0) + 1* 62^(4-1-1)
        for (int i = 0, j = str.length(); i < j; i++) {
            Integer num = ArrBitNum.get(str.charAt(i));
//            if (num == null)
//                throw new Exception("Contains unknown char: " + str.charAt(i));
            if (num != null && num > 0)
                ret += (long) (num * Math.pow(n, j - 1 - i));
        }

        return ret;
    }

    /**
     * 把十进制数转换为62进制
     *
     * @param num 十进制数
     * @return 62进制字符串
     */
    public static String ConvertToStr(long num) throws Exception {
        return ConvertToStr(num, _arrChars.length());
    }

    /**
     * 把十进制数转换为n进制
     *
     * @param num 十进制数
     * @param n   要转换为几进制
     * @return n进制字符串
     */
    public static String ConvertToStr(long num, int n) throws Exception {
        if (num < 0)
            throw new Exception("不支持负数转换");

        if (n <= 0)
            n = _arrChars.length();

        StringBuilder ret = new StringBuilder();
        do {
            int perNum = (int) (num % n);
            num = num / n;
            ret.insert(0, _arrChars.charAt(perNum));
        } while (num > 0);

        return ret.toString();
    }

    static Map<Character, Integer> InitArr() {
        Map<Character, Integer> ret = new HashMap<>();
        for (int i = 0, j = _arrChars.length(); i < j; i++) {
            ret.put(_arrChars.charAt(i), i);
        }

        return ret;
    }

    /**
     * 获得指定位数的随机串
     * @param len 指定长度
     * @return 随机串
     */
    public static String generateCode(int len) {
        int maxNum = _arrChars.length();

        String ret = "";
        for (int i = 0; i < len; i++) {
            int num = random.nextInt(maxNum);
            ret += String.valueOf(_arrChars.charAt(num));
        }
        return ret;
    }

}
