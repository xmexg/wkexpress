package org.xmexg.wkexpress.tools;

import java.util.Random;

public class RandomCreate {
    public static String getRandomString(int length) { //length表示生成字符串的长度
        String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";   //生成字符串从此序列中取
        Random random = new Random();   //生成随机类
        StringBuffer sb = new StringBuffer();   //生成的随机字符串
        int baseLength = base.length();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(baseLength);
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    public static String createToken(){
        return getRandomString(32);
    }
}
