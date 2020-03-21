package ink.techat.client.push.fragments.main;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import ink.techat.client.common.app.PresenterFragment;
import ink.techat.client.common.widget.EmptyView;
import ink.techat.client.common.widget.PortraitView;
import ink.techat.client.common.widget.recycler.RecycierAdapter;
import ink.techat.client.common.widget.recycler.WrapContentLinearLayoutManager;
import ink.techat.client.factory.model.db.Session;
import ink.techat.client.factory.presenter.message.SessionContract;
import ink.techat.client.factory.presenter.message.SessionPresenter;
import ink.techat.client.push.R;
import ink.techat.client.push.activities.MessageActivity;
import ink.techat.client.utils.DateTimeUtil;

/**
 * @author NickCharlie
 */
public class ActiveFragment extends PresenterFragment<SessionContract.Presenter> implements SessionContract.View{

    @BindView(R.id.active_empty)
    EmptyView mEmptyView;

    @BindView(R.id.active_recyclerView)
    RecyclerView mRecycler;

    private RecycierAdapter<Session> mAdapter;

    public ActiveFragment() {

    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_active;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        // 初始化Recycler
        mRecycler.setLayoutManager(new WrapContentLinearLayoutManager(getContext()));
        mRecycler.setAdapter(mAdapter = new RecycierAdapter<Session>() {
            @Override
            protected int getItemViewType(int position, Session session) {
                return R.layout.cell_session_list;
            }

            @Override
            protected ViewHolder<Session> onCreateiewHolder(View root, int viewType) {
                return new ActiveFragment.ViewHolder(root);
            }
        });

        mAdapter.setListener(new RecycierAdapter.AdapterListerImpl<Session>() {
            @Override
            public void onItemClick(RecycierAdapter.ViewHolder holder, Session session) {
                super.onItemClick(holder, session);
                // 跳转到聊天界面
                MessageActivity.show(getContext(), session);
            }
        });

        // 初始化占位布局
        mEmptyView.bind(mRecycler);
        setmPlaceHolderView(mEmptyView);
    }

    @Override
    protected void onFirstInitData() {
        super.onFirstInitData();
        mPresenter.start();
    }

    @Override
    protected SessionContract.Presenter initPresenter() {
        return new SessionPresenter(this);
    }

    @Override
    public RecycierAdapter<Session> getRecyclerAdapter() {
        return mAdapter;
    }

    @Override
    public void onAdapterDataChanged() {
        mPlaceHolderView.triggerOkOrEmpty(mAdapter.getItemCount() > 0);
    }

    class ViewHolder extends RecycierAdapter.ViewHolder<Session>{

        @BindView(R.id.img_session_portrait)
        PortraitView mPortraitView;

        @BindView(R.id.txt_session_name)
        TextView mName;

        @BindView(R.id.txt_session_content)
        TextView mContent;

        @BindView(R.id.txt_session_time)
        TextView mTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Session session) {
            mPortraitView.setup(Glide.with(ActiveFragment.this), session.getPicture());
            mName.setText(session.getTitle());
            mContent.setText(TextUtils.isEmpty(session.getContent()) ? session.getContent() : "");
            mTime.setText(DateTimeUtil.getSampleDate(session.getModifyAt()));
        }
    }
}
