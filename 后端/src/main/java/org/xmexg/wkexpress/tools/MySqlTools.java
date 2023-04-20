package org.xmexg.wkexpress.tools;

/**
 * 存放一些关于sql的小工具
 */
public class MySqlTools {

    /**
     * 把要拼接进sql的文本进行替换，防止sql注入
     */
    public static String TransactSQLInjection(String text) {
        return text.replaceAll(".*([';]+|(--)+).*", " ");
    }
}
