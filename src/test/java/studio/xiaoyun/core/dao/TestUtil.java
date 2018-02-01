package studio.xiaoyun.core.dao;

import org.hibernate.query.Query;
import org.springframework.stereotype.Service;
import studio.xiaoyun.core.pojo.RoleDO;

import javax.annotation.Resource;
import java.util.Random;

@Service
public class TestUtil {
    @Resource
    private RoleDao roleDao;
    @Resource
    private SessionUtil sessionUtil;

    /**
     *
     * @return 创建一个角色，名称和描述随机生成
     */
    public RoleDO createRole() {
        RoleDO role = new RoleDO();
        Random random = new Random();
        role.setName("role" + random.nextInt(999999));
        role.setDescription("roleDes" + random.nextInt(999999));
        roleDao.save(role);
        return role;
    }

}
