package ink.techat.web.push.utils;

/**
 * 字符串快速匹配工具类
 *
 * @author NickCharlie
 */
public class TextMatch {
    public boolean sunday(String dest, String pattern) {

        char[] destchars = dest.toCharArray();
        char[] patternchars = pattern.toCharArray();

        int i = 0;
        int j = 0;

        while (i <= (dest.length() - pattern.length() + j)) {
            if (destchars[i] != patternchars[j]) {
                if (i == (dest.length() - pattern.length() + j)) {
                    break;
                }
                int pos = contains(patternchars, destchars[i + pattern.length() - j]);
                if (pos == -1) {
                    i = i + pattern.length() + 1 - j;
                    j = 0;
                } else {
                    i = i + pattern.length() - pos - j;
                    j = 0;
                }
            } else {
                if (j == (pattern.length() - 1)) {
                    i = i - j + 1;
                    j = 0;
                    return true;
                } else {
                    i++;
                    j++;
                }
            }
        }
        return false;
    }

    private int contains(char[] chars, char target) {
        for (int i = chars.length - 1; i >= 0; i--) {
            if (chars[i] == target) {
                return i;
            }
        }
        return -1;
    }
}
