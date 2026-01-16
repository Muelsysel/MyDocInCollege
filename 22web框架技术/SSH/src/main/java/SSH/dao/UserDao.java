package SSH.dao;

import SSH.entity.User;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = false)
public class UserDao {

    private HibernateTemplate hibernateTemplate;

    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }

    public void saveOrUpdate(User user) {
        hibernateTemplate.merge(user);
    }

    public User getUserByUsername(String username) {
        String hql = "from User where username = :username";
        List<User> users = (List<User>) hibernateTemplate.findByNamedParam(hql, "username", username);
        User user = null;
        if (!users.isEmpty()) {
            user = users.get(0);
        }
        return user;
    }
    public User getUserById(Long id) {
        String hql = "from User where id = :id";
        List<User> users = (List<User>) hibernateTemplate.findByNamedParam(hql, "id", id);
        User user = null;
        if (!users.isEmpty()) {
            user = users.get(0);
        }
        return user;
    }
}
