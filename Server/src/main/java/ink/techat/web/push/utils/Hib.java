package ink.techat.web.push.utils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.logging.Logger;

/**
 * Created by qiujuer
 * on 2017/2/17.
 */
public class Hib {
    private static final Logger LOGGER = Logger.getLogger(Hib.class.getName());
    // 全局SessionFactory
    private static SessionFactory sessionFactory;

    static {
        // 静态初始化sessionFactory
        init();
    }

    private static void init() {
        // 从hibernate.cfg.xml文件初始化
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                // configures settings from hibernate.cfg.xml
                .configure("/hibernate.cfg.xml")
                .build();
        try {
            // build 一个sessionFactory
            sessionFactory = new MetadataSources(registry)
                    .buildMetadata()
                    .buildSessionFactory();
        } catch (Exception e) {
            e.printStackTrace();
            // 错误则打印输出，并销毁
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    /**
     * 获取全局的SessionFactory
     *
     * @return SessionFactory
     */
    public static SessionFactory sessionFactory() {
        return sessionFactory;
    }

    /**
     * 从SessionFactory中得到一个Session会话
     *
     * @return Session
     */
    public static Session session() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * 关闭sessionFactory
     */
    public static void closeFactory() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    /**
     * interface Query
     * 用于用户实际操作的接口
     * 无返回值
     */
    public interface QueryOnly{
        void query(Session sessions);
    }

    /**
     * interface Query
     * 用于用户实际操作的接口
     */
    public interface Query<T>{
        T query(Session sessions);
    }

    /**
     * void queryOnly
     * 简化Session事务操作的方法
     * @param query Query
     */
    public static void queryOnly(Query query){
        // 重新开启一个 Session
        Session session = sessionFactory.openSession();
        // 开启事务
        final Transaction transaction = session.beginTransaction();

        try {
            // 调用传递进来的接口方法传递session
            query.query(session);
            transaction.commit();
        }catch (Exception e){
            e.printStackTrace();
            try {
                transaction.rollback();
            }catch (RuntimeException runTimeError){
                runTimeError.printStackTrace();
            }
        }finally {
            session.close();
        }
    }

    /**
     * T query
     * 简化Session事务操作的方法
     * @param query Query
     * @return t T
     */
    public static<T> T query(Query<T> query){
        // 重新开启一个 Session
        Session session = sessionFactory.openSession();
        // 开启事务
        final Transaction transaction = session.beginTransaction();

        T t = null;
        try {
            // 调用传递进来的接口方法传递session
            t = query.query(session);
            transaction.commit();
        }catch (Exception e){
            e.printStackTrace();
            try {
                transaction.rollback();
            }catch (RuntimeException runTimeError){
                runTimeError.printStackTrace();
            }
        }finally {
            session.close();
        }
        return t;
    }
}
