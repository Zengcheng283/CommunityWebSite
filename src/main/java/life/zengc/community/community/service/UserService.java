package life.zengc.community.community.service;

import life.zengc.community.community.mapper.UserMapper;
import life.zengc.community.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public User createOrUpdate(User user) {
        User DBuser = userMapper.findByAccountId(user.getAccountId());
        if (DBuser == null) {
            // 向数据库插入新用户
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
            return user;
        } else {
            // 更新数据库用户token
            DBuser.setGmtModified(System.currentTimeMillis());
            DBuser.setAvatarUrl(user.getAvatarUrl());
            DBuser.setToken(user.getToken());
            DBuser.setName(user.getName());

            userMapper.update(DBuser);
            return DBuser;
        }
    }
}
