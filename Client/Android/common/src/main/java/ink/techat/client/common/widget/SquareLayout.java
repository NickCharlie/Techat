package ink.techat.client.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author NickCharlie
 */
public class SquareLayout extends FrameLayout {
    public SquareLayout(@NonNull Context context) {
        super(context);
    }

    public SquareLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 搞一个正方形 全用 widthMeasureSpec 宽度限定
        // 给父类传递的测量值都为宽度，那么就是个正方形控件了
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);

    }
}
