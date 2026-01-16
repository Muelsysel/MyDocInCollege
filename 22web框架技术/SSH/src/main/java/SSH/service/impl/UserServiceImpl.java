package SSH.service.impl;


import SSH.dao.UserDao;
import SSH.service.IUserService;
import SSH.entity.User;
import org.apache.struts2.ServletActionContext;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpSession;

@Transactional
public class UserServiceImpl implements IUserService {

    private UserDao userDao;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }


    /**
     * @param user
     * @return
     */
    @Override
    public boolean saveWithoutPassword(User user) {
        try {
            if (user.getId() == null) {
                HttpSession session = ServletActionContext.getRequest().getSession();
                Long id = (Long) session.getAttribute("UserId");
                user.setId(id);
            }
            User oldUser = userDao.getUserById(user.getId());
            user.setPassword(oldUser.getPassword());
            userDao.saveOrUpdate(user);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @param username
     * @param password
     * @return {@link User }
     */
    @Override
    public User login(String username, String password) {
        User user = userDao.getUserByUsername(username);
        if (user == null) {
            return null;
        }else {
            //将密码使用jwt加密后与数据库比对
            String passwordJWT= DigestUtils.md5DigestAsHex(password.getBytes());
            if (!passwordJWT.equals(user.getPassword())) {
                return null;
            }
        }
        return user;
    }
    @Override
    public User getUserById(Long id) {
        if (id != null) {
            //便于前端显示
            return userDao.getUserById(id);
        }
        return null;
    }

    @Override
    public User getUserByUsername(String username) {
        return userDao.getUserByUsername(username);
    }

    @Override
    public boolean register(User user) {
        try {
            //保证username不重复
            if (userDao.getUserByUsername(user.getUsername()) != null) {
                return false;
            }
            user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
            userDao.saveOrUpdate(user);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updatePassword(User user) {
        if (user.getPassword() != null) {
            user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
            userDao.saveOrUpdate(user);
            return true;
        }
        return false;
    }
}
