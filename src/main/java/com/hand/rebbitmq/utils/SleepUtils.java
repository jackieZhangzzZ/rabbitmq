package com.hand.rebbitmq.utils;

/**
 * @author zhuopeng.zhang@hand-china.com 2021/9/3 10:02
 */
public class SleepUtils {
    public static void sleep(int second) {
        try {
            Thread.sleep(1000 * second);
        } catch (InterruptedException _ignored) {
            Thread.currentThread().interrupt();
        }
    }
}
