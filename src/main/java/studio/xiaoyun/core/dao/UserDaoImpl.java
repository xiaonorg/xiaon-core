package studio.xiaoyun.core.dao;

import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import studio.xiaoyun.core.exception.InvalidParameterException;
import studio.xiaoyun.core.exception.XyException;
import studio.xiaoyun.core.pojo.ArticleDO;
import studio.xiaoyun.core.pojo.UserDO;
import studio.xiaoyun.core.query.UserQuery;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Repository("userDao")
public class UserDaoImpl extends AbstractDaoImpl<UserDO> implements UserDao {

    @Override
    public UserDO getById(String id) throws InvalidParameterException {
        UserDO user = getSession().get(UserDO.class,id);
        if(user==null){
            throw new InvalidParameterException(id+"不存在");
        }
        return user;
    }

    @Override
    public UserDO loadById(String ID) {
        return getSession().load(UserDO.class,ID);
    }

    @Override
    public Optional<UserDO> getUserByName(String name) {
        Optional<UserDO> user;
        String hql = "from UserDO where name=:name";
        Query<UserDO> query = getSession().createQuery(hql,UserDO.class);
        query.setParameter("name",name);
        List<UserDO> list = query.list();
        if(list.size()>1){
            throw new XyException("根据用户名:"+name+"获得的结果不止一个！");
        }else if(list.size()==1){
            user = Optional.of(list.get(0));
        }else{
            user = Optional.empty();
        }
        return user;
    }

    @Override
    public Optional<UserDO> getUserByEmail(String email) {
        Optional<UserDO> user;
        String hql = "from UserDO where email=:email";
        Query<UserDO> query = getSession().createQuery(hql,UserDO.class);
        query.setParameter("email",email);
        List<UserDO> list = query.list();
        if(list.size()>1){
            throw new XyException("根据邮箱:"+email+"获得的结果不止一个！");
        }else if(list.size()==1){
            user = Optional.of(list.get(0));
        }else{
            user = Optional.empty();
        }
        return user;
    }

    @Override
    public long countUserByParameter(UserQuery parameter) {
        return countByParameter(null,null,parameter);
    }

    @Override
    public List<UserDO> listUserByParameter(UserQuery parameter) {
        return listByParameter(null,null,parameter,UserDO.class);
    }

    @Override
    public List<UserDO> listNameByUserIds(List<String> userIds) {
        if(userIds==null || userIds.isEmpty()){
            return Collections.emptyList();
        }
        String hql = "select userId,name from UserDO where userId in (:userIds)";
        Query query = getSession().createQuery(hql);
        query.setParameterList("userIds",userIds);
        List list = query.list();
        List<UserDO> users = new LinkedList<>();
        for(Object obj : list){
            Object[] row = (Object[])obj;
            UserDO user = new UserDO();
            user.setUserId(row[0].toString());
            user.setName(row[1].toString());
            users.add(user);
        }
        return users;
    }

    @Override
    String getQuerySql() {
        return "select user_0.* from xy_user as user_0";
    }

}
