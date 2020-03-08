package ink.techat.client.push.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.widget.SearchView;

import ink.techat.client.common.app.Fragment;
import ink.techat.client.common.app.ToolbarActivity;
import ink.techat.client.push.R;
import ink.techat.client.push.fragments.search.SearchGroupFragment;
import ink.techat.client.push.fragments.search.SearchUserFragment;

/**
 * 搜索界面Activity
 * @author NickCharie
 */
public class SearchActivity extends ToolbarActivity {

    public static final int TYPE_USER = 1;
    public static final int TYPE_GROUP = 2;
    private static final String EXTRA_TYPE = "EXTRA_TYPE";
    private SearchFragment mSearchFragment;
    private int type;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_search;
    }

    /**
     * 显示搜索界面
     * @param context Context
     * @param type 显示的类型
     */
    public static void show(Context context, int type){
        Intent intent = new Intent(context, SearchActivity.class);
        intent.putExtra(EXTRA_TYPE, type);
        context.startActivity(intent);
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        this.type = bundle.getInt(EXTRA_TYPE);
        return this.type == TYPE_USER || this.type == TYPE_GROUP;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        Fragment fragment;
        if (type == TYPE_USER){
            SearchUserFragment searchUserFragment = new SearchUserFragment();
            fragment = searchUserFragment;
            mSearchFragment = searchUserFragment;
        }else {
            SearchGroupFragment searchGroupFragment = new SearchGroupFragment();
            fragment = searchGroupFragment;
            mSearchFragment = searchGroupFragment;
        }
        getSupportFragmentManager().beginTransaction().add(R.id.lay_search_container, fragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 初始化菜单
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search, menu);
        // 找到搜索菜单
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        if (searchView != null){
            // 拿到搜索管理器
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            // 添加搜索监听
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    search(query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    if (!TextUtils.isEmpty(newText)){
                        search("");
                        return true;
                    }
                    return false;
                }
            });
        }
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 搜索的发七点
     * @param query 搜索的文字
     */
    private void search(String query){
        if (mSearchFragment == null) {
            return;
        }
        mSearchFragment.search(query);
    }

    /**
     * 搜索的Fragment必须继承这玩意
     */
    public interface SearchFragment{
        void search(String content);
    }
}
