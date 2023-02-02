package life.zengc.community.community.service;

import life.zengc.community.community.common.CommonMethods;
import life.zengc.community.community.exception.CustomizeErrorCode;
import life.zengc.community.community.exception.CustomizeException;
import life.zengc.community.community.mapper.UserMapper;
import life.zengc.community.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CommonMethods commonMethods;

    public Boolean createUser(User user) {
        try {
            createIdAndGmt(user);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    private void createIdAndGmt(User user) {
        String idKey = commonMethods.randomId();
        while (userMapper.findById(idKey) != null) {
            idKey = commonMethods.randomId();
        }
        user.setId(idKey);
        user.setGmtCreate(System.currentTimeMillis());
        user.setGmtModified(user.getGmtCreate());
        userMapper.insert(user);
    }

    public User createOrUpdate(User user) {
        User DBuser = userMapper.findByAccountId(user.getAccountId());
        if (DBuser == null) {
            // 向数据库插入新用户
            createIdAndGmt(user);
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

    public boolean selectUser(String token) {
        User user = userMapper.selectByToken(token);
        return user != null;
    }

    public User selectUser(User user) {
        return userMapper.selectByToken(user.getToken());
    }

    public boolean checkPerson(User user) {
        return userMapper.selectByToken(user.getToken()) != null || userMapper.selectByAccountId(user.getAccountId()) != null;
    }

    public Object selectUserById(String userId) {
        User user = userMapper.selectUserById(userId);
        Map<String, String> result = new HashMap<>();
        result.put("name", user.getName());
        result.put("avatarUrl", user.getAvatarUrl());
        return result;
    }

    public String getUser(String username, String password) {
        User user = userMapper.findByName(username);
        if (user == null) {
            throw new CustomizeException(CustomizeErrorCode.USER_NOT_EXIST);
        }
        if (Objects.equals(user.getPassword(), password)) {
            String token = commonMethods.randomToken();
            while (userMapper.selectByToken(token) != null) {
                token = commonMethods.randomToken();
            }
            userMapper.updateToken(user.getId(), token);
            return token;
        } else {
            throw new CustomizeException(CustomizeErrorCode.PASSWORD_NOT_RIGHT);
        }
    }

    public Map<String, String> getUserInfo(String id) {
        User user = userMapper.selectByToken(id);
        Map<String, String> data = new HashMap<>();
        data.put("name", user.getName());
        data.put("avatar", user.getAvatarUrl());
        return data;
    }

    public boolean checkExist(String username, String phone) {
        Boolean userCheck = userMapper.selectByUsername(username);
        Boolean phoneCheck = userMapper.selectByPhone(phone);
        return userCheck || phoneCheck;
    }
}
