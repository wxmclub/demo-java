package com.demo.regex.breaker;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author wxmclub
 * @version 1.0
 * @date 2025-01-21
 */
public class RegexMatcherUtils {

    /**
     * 生成带超时的 Matcher 匹配对象
     *
     * @param regex         正则表达式
     * @param input         匹配字符串
     * @param timeoutMillis 超时时常，毫秒
     * @return Matcher对象
     */
    public static Matcher matcher(String regex,
                                  CharSequence input,
                                  long timeoutMillis) {
        Pattern pattern = Pattern.compile(regex);
        return matcher(pattern, input, timeoutMillis);
    }

    /**
     * 生成带超时的 Matcher 匹配对象
     *
     * @param pattern       正则表达式
     * @param input         匹配字符串
     * @param timeoutMillis 超时时常，毫秒
     * @return Matcher对象
     */
    public static Matcher matcher(Pattern pattern,
                                  CharSequence input,
                                  long timeoutMillis) {
        TimeoutRegexCharSequence timeoutInput = new TimeoutRegexCharSequence(input, timeoutMillis, input, pattern.pattern());
        return pattern.matcher(timeoutInput);
    }

    /**
     * 带超时时间的 CharSequence 代理对象
     */
    private static class TimeoutRegexCharSequence implements CharSequence {

        /**
         * 代理的字符串
         */
        private final CharSequence inner;
        /**
         * 超时时常
         */
        private final long timeoutMillis;
        /**
         * 超时时间戳
         */
        private final long timeoutTime;
        /**
         * 原始字符串，用于异常消息
         */
        private final CharSequence matchInput;
        /**
         * 匹配的正则，用于异常消息
         */
        private final String regex;

        public TimeoutRegexCharSequence(CharSequence inner,
                                        long timeoutMillis,
                                        CharSequence matchInput,
                                        String regex) {
            super();
            this.inner = inner;
            this.timeoutMillis = timeoutMillis;
            this.matchInput = matchInput;
            this.regex = regex;
            this.timeoutTime = SystemClock.currentTimeMillis() + timeoutMillis;
        }

        @Override
        public char charAt(int index) {
            if (SystemClock.currentTimeMillis() > this.timeoutTime) {
                throw new RegexMatcherTimeoutException("regex matcher timeout " + this.timeoutMillis + "ms",
                        regex, matchInput);
            }
            return inner.charAt(index);
        }

        @Override
        public int length() {
            return inner.length();
        }

        @Override
        public CharSequence subSequence(int start,
                                        int end) {
            return new TimeoutRegexCharSequence(inner.subSequence(start, end), timeoutMillis, matchInput, regex);
        }

        @Override
        public String toString() {
            return inner.toString();
        }

    }

}
