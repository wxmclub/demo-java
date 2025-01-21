package com.demo.regex.breaker;

import java.util.regex.Matcher;

/**
 * @author wxmclub
 * @version 1.0
 * @date 2025-01-21
 */
public class RegexMatcherTest {

    public static void main(String[] args) {
        System.out.println("----------------");
        testMatch("x*", "xxxxxxx");

        System.out.println("----------------");
        testMatch("(x+x+)+y", "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
    }

    static void testMatch(String regex,
                          String input) {
        long start = SystemClock.currentTimeMillis();
        Matcher matcher = RegexMatcherUtils.matcher(regex, input, 1000L);
        //Matcher matcher = Pattern.compile(regex).matcher(input);
        try {
            if (matcher.find()) {
                System.out.printf("匹配成功: cost=%s%n", (SystemClock.currentTimeMillis() - start));
            } else {
                System.out.printf("匹配失败: cost=%s%n", (SystemClock.currentTimeMillis() - start));
            }
        } catch (RegexMatcherTimeoutException e) {
            System.out.printf("匹配超时: cost=%s, message=%s, regex=%s, input=%s%n",
                    (SystemClock.currentTimeMillis() - start), e.getMessage(), e.getRegex(), e.getInput());
        }

    }

}
