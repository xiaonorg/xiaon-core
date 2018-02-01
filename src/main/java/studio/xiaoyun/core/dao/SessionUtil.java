package studio.xiaoyun.core.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;

/**
 * dao层的session辅助类，用于获得session
 */
@Repository
public class SessionUtil {
    @Resource
    private EntityManagerFactory entityManagerFactory;
    private SessionFactory sessionFactory;

    /**
     * 获得当前事务中的session
     * @return session
     */
    protected Session getSession() {
        if(sessionFactory==null){
            sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
        }
        return sessionFactory.getCurrentSession();
    }

}
