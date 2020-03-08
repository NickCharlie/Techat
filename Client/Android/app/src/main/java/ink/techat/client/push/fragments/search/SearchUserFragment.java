package ink.techat.client.push.fragments.search;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import net.qiujuer.genius.ui.Ui;
import net.qiujuer.genius.ui.compat.UiCompat;
import net.qiujuer.genius.ui.drawable.LoadingCircleDrawable;
import net.qiujuer.genius.ui.drawable.LoadingDrawable;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import ink.techat.client.common.app.PresenterFragment;
import ink.techat.client.common.widget.EmptyView;
import ink.techat.client.common.widget.PortraitView;
import ink.techat.client.common.widget.recycler.RecycierAdapter;
import ink.techat.client.factory.model.card.UserCard;
import ink.techat.client.factory.presenter.contact.FollowContract;
import ink.techat.client.factory.presenter.contact.FollowPresenter;
import ink.techat.client.factory.presenter.search.SearchContract;
import ink.techat.client.factory.presenter.search.SearchUserPresenter;
import ink.techat.client.push.R;
import ink.techat.client.push.activities.SearchActivity;
import ink.techat.client.push.fragments.main.ContactFragment;

/**
 * @author NickCharlie
 */
public class SearchUserFragment extends PresenterFragment<SearchContract.Presenter>
        implements SearchActivity.SearchFragment, SearchContract.UserView {

    @BindView(R.id.empty)
    EmptyView mEmptyView;

    @BindView(R.id.search_user_recyclerView)
    RecyclerView mRecycler;

    private RecycierAdapter<UserCard> mAdapter;

    public SearchUserFragment() {

    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        // 初始化Recycler
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecycler.setAdapter(mAdapter = new RecycierAdapter<UserCard>() {
            @Override
            protected int getItemViewType(int position, UserCard userCard) {
                return R.layout.cell_search_list;
            }

            @Override
            protected ViewHolder<UserCard> onCreateiewHolder(View root, int viewType) {
                return new SearchUserFragment.ViewHolder(root);
            }
        });
        // 初始化占位布局
        mEmptyView.bind(mRecycler);
        setmPlaceHolderView(mEmptyView);
    }

    @Override
    protected void initData() {
        super.initData();
        // 发起首次搜索
        search("");
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_search_user;
    }


    @Override
    public void search(String content) {
        mPresenter.search(content);
    }

    @Override
    public void onSearchDone(List<UserCard> userCards) {
        mAdapter.replace(userCards);
        mPlaceHolderView.triggerOkOrEmpty(mAdapter.getItemCount() > 0);
    }

    @Override
    protected SearchContract.Presenter initPresenter() {
        return new SearchUserPresenter(this);
    }

    /**
     * 每一个cell的布局操作
     */
    class ViewHolder extends RecycierAdapter.ViewHolder<UserCard> implements FollowContract.View {

        @BindView(R.id.search_img_portrait)
        PortraitView mPortraitView;

        @BindView(R.id.search_txt_name)
        TextView mName;

        @BindView(R.id.img_follow)
        ImageView mFollow;

        private FollowContract.Presenter mFollowPresenter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            new FollowPresenter(this);
        }

        @Override
        protected void onBind(UserCard userCard) {
            mPortraitView.setup(Glide.with(SearchUserFragment.this), userCard);
            mName.setText(userCard.getName());
            mFollow.setEnabled(!userCard.getIsFollow());
        }

        /**
         * 发起关注
         */
        @OnClick(R.id.img_follow)
        void onFollowClick(){
            mFollowPresenter.follow(mData.getId());
        }

        @Override
        public void onFollowSucceed(UserCard userCard) {
            // 更改Drawable状态
            if (mFollow.getDrawable() instanceof LoadingDrawable){
                ((LoadingDrawable)mFollow.getDrawable()).stop();
                // 设置为默认的样式
                mFollow.setImageResource(R.drawable.sel_opt_done_add);
            }
            // 发起更新
            updataData(userCard);
        }

        @Override
        public void showError(int str) {
            // 更改Drawable状态
            if (mFollow.getDrawable() instanceof LoadingDrawable){
                // 失败则停止动画并且显示一个圆圈
                LoadingDrawable drawable = (LoadingDrawable)mFollow.getDrawable();
                drawable.setProgress(1);
                drawable.stop();
            }
        }

        @Override
        public void showLoading() {
            int minSize = (int) Ui.dipToPx(getResources(), 22);
            int maxSize = (int) Ui.dipToPx(getResources(), 30);
            int[] color = new int[]{UiCompat.getColor(getResources(), R.color.white_alpha_208)};
            LoadingDrawable drawable = new LoadingCircleDrawable(minSize, maxSize);
            drawable.setBackgroundColor(0);
            drawable.setForegroundColor(color);
            mFollow.setImageDrawable(drawable);
            drawable.start();
        }

        @Override
        public void setPresenter(FollowContract.Presenter presenter) {
            mFollowPresenter = presenter;
        }
    }
}

