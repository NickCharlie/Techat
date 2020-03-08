package ink.techat.client.push.fragments.main;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import ink.techat.client.common.app.PresenterFragment;
import ink.techat.client.common.widget.EmptyView;
import ink.techat.client.common.widget.PortraitView;
import ink.techat.client.common.widget.recycler.RecycierAdapter;
import ink.techat.client.factory.model.db.User;
import ink.techat.client.factory.presenter.contact.ContactContract;
import ink.techat.client.factory.presenter.contact.ContactPresenter;
import ink.techat.client.push.R;
import ink.techat.client.push.activities.MessageActivity;

/**
 * @author NickCharlie
 */
public class ContactFragment extends PresenterFragment<ContactContract.Presenter> implements ContactContract.View {

    @BindView(R.id.contact_empty)
    EmptyView mEmptyView;

    @BindView(R.id.contact_user_recyclerView)
    RecyclerView mRecycler;

    private RecycierAdapter<User> mAdapter;

    public ContactFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_contact;
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        // 初始化Recycler
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecycler.setAdapter(mAdapter = new RecycierAdapter<User>() {
            @Override
            protected int getItemViewType(int position, User userCard) {
                return R.layout.cell_contact_list;
            }

            @Override
            protected ViewHolder<User> onCreateiewHolder(View root, int viewType) {
                return new ContactFragment.ViewHolder(root);
            }
        });

        mAdapter.setListener(new RecycierAdapter.AdapterListerImpl<User>() {
            @Override
            public void onItemClick(RecycierAdapter.ViewHolder holder, User user) {
                super.onItemClick(holder, user);
                // 跳转到聊天界面
                MessageActivity.show(getContext(), user);
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
    protected ContactContract.Presenter initPresenter() {
        // 初始化Presenter
        return new ContactPresenter(this) {
        };
    }

    @Override
    public RecycierAdapter<User> getRecyclerAdapter() {
        return mAdapter;
    }

    @Override
    public void onAdapterDataChanged() {
        // 界面操作
        mPlaceHolderView.triggerOkOrEmpty(mAdapter.getItemCount() > 0);
    }

    class ViewHolder extends RecycierAdapter.ViewHolder<User>{

        @BindView(R.id.img_contact_portrait)
        PortraitView mPortraitView;

        @BindView(R.id.txt_contact_name)
        TextView mName;

        @BindView(R.id.txt_contact_desc)
        TextView mDesc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(User user) {
            mPortraitView.setup(Glide.with(ContactFragment.this), user);
            mName.setText(user.getName());
            mDesc.setText(user.getDescription());
        }
    }
}
