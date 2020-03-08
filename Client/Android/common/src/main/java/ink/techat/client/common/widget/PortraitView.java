package ink.techat.client.common.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import de.hdodenhof.circleimageview.CircleImageView;
import ink.techat.client.common.R;
import ink.techat.client.factory.model.Author;

/**
 * 头像控件
 * @author NickCharlie
 */
public class PortraitView extends CircleImageView {

    public PortraitView(Context context) {
        super(context);
    }

    public PortraitView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PortraitView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setup(RequestManager manager,int resourceId , String url){
        if (url == null){
            url = "";
        }
        manager.load(url)
                .placeholder(resourceId)
                .centerCrop()
                .dontAnimate()
                .into(this);
    }

    public void setup(RequestManager manager, Author author){
        if (author == null){
            return;
        }
        setup(manager, author.getPortrait());
    }

    public void setup(RequestManager manager,String url){
        setup(manager, R.drawable.default_portrait, url);
    }
}
