package ink.techat.client.push.helper;

import android.content.Context;
import android.util.SparseArray;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


/**
 * class NavHelper<T>
 * 用于解决对Fragment的调度与重用问题
 * 达到最优的Fragment切换
 *
 * @author NickCharlie
 */
public class NavHelper<T> {

    /**
     * SparseArray<Tab<T>> Fragment页面列表
     * FragmentManager Fragment管理器
     * containerId 容器Id
     * Context 对应的环境上下文
     * currentTab 存储当前选中的一个Tab
     */
    private final SparseArray<Tab<T>> tabs = new SparseArray<>();
    private final FragmentManager fragmentManager;
    private final int containerId;
    private final Context context;
    private OnTabChangeListener<T> listener;
    private Tab<T> currentTab;

    /**
     * NavHelper工具类构造函数
     * 用于初始化各种必要参数
     *
     * @param context         对应的环境上下文
     * @param containerId     目标容器Id
     * @param fragmentManager Fragment管理器
     * @param listener        回调接口监听器
     */
    public NavHelper(Context context, int containerId, FragmentManager fragmentManager,
                     OnTabChangeListener<T> listener) {
        this.fragmentManager = fragmentManager;
        this.containerId = containerId;
        this.context = context;
        this.listener = listener;
    }

    /**
     * 添加Tab
     *
     * @param menuId Tab对应的菜单Id
     * @param tab    Tab
     * @return this 当前辅助类
     */
    public NavHelper<T> add(int menuId, Tab<T> tab) {
        tabs.put(menuId, tab);
        return this;
    }

    /**
     * getCurrentTab()
     * 获取当前显示的Tab
     *
     * @return currentTab 当前显示的Tab
     */
    public Tab<T> getCurrentTab() {
        return currentTab;
    }

    /**
     * performClickMenu(int menuId)
     * 执行点击菜单操作
     *
     * @param menuId 菜单Item的Id
     * @return boolean 是否能够处理点击
     */
    public boolean performClickMenu(int menuId) {
        // 集合中寻找点击的菜单对应的Tab，如果有，则进行处理
        Tab<T> tab = tabs.get(menuId);
        if (tab != null) {
            doSelect(tab);
            return true;
        }
        return false;
    }

    /**
     * doSelect(Tab<T> tab)
     * 进行Tab选择操作
     *
     * @param tab 泰伯
     */
    private void doSelect(Tab<T> tab) {
        Tab<T> oldTab = null;
        if (currentTab != null) {
            // 默认currentTab等于旧的Tab
            oldTab = currentTab;
            if (oldTab == tab) {
                // 如果当前的tab就是点击的Tab,则刷新Tab
                notifyTabReselect(tab);
                return;
            }
        }
        // 赋值并调用切换方法
        currentTab = tab;
        doTabChanged(currentTab, oldTab);
    }

    /**
     * doTabChanged(Tab<T> newTab, Tab<T> oldTab)
     * 用于点击底部导航栏Item后进行Fragment的调度操作
     * 以及在Tab类中储存信息
     *
     * @param newTab 新的Tab
     * @param oldTab 旧的Tab
     */
    private void doTabChanged(Tab<T> newTab, Tab<T> oldTab) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (oldTab != null) {
            if (oldTab.fragment != null) {
                // 把旧的fragment从界面移除，但是还在Fragment的缓存空间中
                fragmentTransaction.detach(oldTab.fragment);
            }
        }

        if (newTab != null) {
            if (newTab.fragment == null) {
                // 此Fragment为 androidx.fragment.app.Fragment
                // 首次新建
                Fragment fragment = Fragment.instantiate(context, newTab.clx.getName(), null);
                // 缓存起来
                newTab.fragment = fragment;
                // 提交到 FragmentManager
                fragmentTransaction.add(containerId, fragment, newTab.clx.getName());
            } else {
                // 如果newTab.fragment不为null 说明有这个缓存则重新加载到界面
                fragmentTransaction.attach(newTab.fragment);
            }
        }
        // 提交事务
        fragmentTransaction.commit();
        // 通知回调
        notifyTabSelect(newTab, oldTab);
    }

    /**
     * notifyTabSelect(Tab<T> newTab, Tab<T> oldTab)
     * 用于通知界面,回调监听器
     *
     * @param newTab 新的Tab
     * @param oldTab 旧的Tab
     */
    private void notifyTabSelect(Tab<T> newTab, Tab<T> oldTab) {
        if (listener != null) {
            listener.onTabChanged(newTab, oldTab);
        }
    }

    /**
     * notifyTabReselect(Tab<T> tab)
     * 用于处理重复点击底部导航栏Item事件
     * 直接刷新Tab
     *
     * @param tab Tab
     */
    private void notifyTabReselect(Tab<T> tab) {
        // TODO 二次点击Tab的事件 现定为刷新Tab
    }

    /**
     * class NavHelper<T>的子类 class Tab<T>
     * class Tab<T>基础属性
     * clx 传入的class
     * extra 额外的自定义字段
     * fragment 缓存的fragment
     *
     * @param <T> 泛型的额外参数
     */
    public static class Tab<T> {
        public Class<? extends Fragment> clx;
        public T extra;
        Fragment fragment;

        public Tab(Class<? extends Fragment> clx, T extra) {
            this.clx = clx;
            this.extra = extra;
        }
    }

    /**
     * interface OnTabChangeListener<T>
     * 定义事件处理完成后的回调接口
     *
     * @param <T>
     */
    public interface OnTabChangeListener<T> {
        /**
         * onTabChanged(Tab<T> newTab, Tab<T> oldTab)
         * Tab改变以后的回调方法
         * @param newTab 新Tab
         * @param oldTab 旧Tab
         */
        void onTabChanged(Tab<T> newTab, Tab<T> oldTab);
    }
}
