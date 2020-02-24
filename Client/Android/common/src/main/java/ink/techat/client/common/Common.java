package ink.techat.client.common;

public class Common {

    /**
     * 一些不可变的永痕参数
     * 通常用于配置和正则表达式
     */
    public interface Constance{
        // 手机号正则
        String REGESX_MOBILE = "[0-9][0-9][0-9]{9}$";
    }
}
