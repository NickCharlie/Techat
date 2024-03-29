package ink.techat.client.common;

/**
 * @author NickCharlie
 */
public class Common {

    /**
     * 一些不可变的永痕参数
     * 通常用于配置和正则表达式
     */
    public interface Constance{
        // 手机号正则
        String REGEX_MOBILE = "[0-9][0-9][0-9]{9}$";
        // 基础的网络请求地址
        String API_URL = "http://122.114.81.108:8080/Techat/api/";
    }
}
