package studio.xiaoyun.core.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import studio.xiaoyun.core.pojo.RoleDO;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class AbstractDaoImplTest {
    @Resource
    private DaoUtil daoUtil;
    @Resource
    private TestUtil testUtil;

    @Test
    public void listByHQL() throws Exception {
        RoleDO role = testUtil.createRole();
        String hql = "from RoleDO where roleId='"+role.getRoleId()+"'";
        List<RoleDO> list = daoUtil.listByHQL(hql,RoleDO.class);
        assertEquals(1,list.size());
        assertEquals(role.getRoleId(),list.get(0).getRoleId());
    }

    @Test
    public void listBySQL() throws Exception {
        RoleDO role = testUtil.createRole();
        String sql = "select name from xy_role where roleId='"+role.getRoleId()+"'";
        List<Object> list = daoUtil.listBySQL(sql);
        assertEquals(1,list.size());
        assertEquals(role.getName(),list.get(0).toString());
    }

    @Test
    public void listBySQL1() throws Exception {
        RoleDO role = testUtil.createRole();
        String sql = "select a.* from xy_role a where a.roleId='"+role.getRoleId()+"'";
        List<RoleDO> list = daoUtil.listBySQL(sql,"a",RoleDO.class);
        assertEquals(1,list.size());
        assertEquals(role.getRoleId(),list.get(0).getRoleId());
    }

    @Test
    public void countByHQL() throws Exception {
        RoleDO role = testUtil.createRole();
        String hql = "select count(*) from RoleDO where roleId='"+role.getRoleId()+"'";
        long count = daoUtil.countByHQL(hql);
        assertEquals(1L,count);
    }

    @Test
    public void countBySQL() throws Exception {
        RoleDO role = testUtil.createRole();
        String sql = "select count(*) from xy_role where roleId='"+role.getRoleId()+"'";
        long count = daoUtil.countBySQL(sql);
        assertEquals(1L,count);
    }

    @Test
    public void delete() throws Exception {
        RoleDO role = testUtil.createRole();
        String hql = "select count(*) from RoleDO where roleId='" + role.getRoleId() + "'";
        daoUtil.delete(role);
        long count = daoUtil.countByHQL(hql);
       assertEquals(0L, count);
    }

    @Test
    public void save() throws Exception {
        RoleDO role = new RoleDO();
        role.setName("aaa");
        role.setDescription("bbb");
        daoUtil.save(role);
        assertNotNull(role.getRoleId());
        RoleDO role2 = daoUtil.getById(role.getRoleId());
        assertEquals(role2.getRoleId(), role.getRoleId());
    }

    @Test
    public void update() throws Exception {
        RoleDO role = testUtil.createRole();
        role.setName("aaa");
        daoUtil.update(role);
        RoleDO role2 = daoUtil.getById(role.getRoleId());
        assertEquals("aaa", role2.getName());
    }

}