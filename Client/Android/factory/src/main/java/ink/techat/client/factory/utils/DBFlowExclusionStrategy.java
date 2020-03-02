package ink.techat.client.factory.utils;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.raizlabs.android.dbflow.structure.ModelAdapter;

/**
 * DBFlow数据库字段过滤 GSON
 * 使GSON在自动将内容转换为json的时候会忽略到DBFlow的字段
 * @author NickCharlie
 */
public class DBFlowExclusionStrategy implements ExclusionStrategy {

    @Override
    public boolean shouldSkipField(FieldAttributes f) {
        // 被跳过的字段, 只要是属于DBFlow数据的跳过
        return f.getDeclaredClass().equals(ModelAdapter.class);
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
        // 被跳过的Class
        return false;
    }
}
