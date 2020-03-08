package ink.techat.client.push.activities;

import android.content.Context;

import ink.techat.client.common.app.Activity;
import ink.techat.client.factory.model.Author;
import ink.techat.client.push.R;

/**
 * 聊天界面
 * @author NickCharlie
 */
public class MessageActivity extends Activity {

    /**
     * 显示与对应联系人的聊天界面
     * @param context Context
     * @param author 联系人的信息
     */
    public static void show(Context context, Author author){

    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_message;
    }
}
