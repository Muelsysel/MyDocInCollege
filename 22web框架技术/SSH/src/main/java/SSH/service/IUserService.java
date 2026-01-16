package SSH.service;

import SSH.entity.User;


public interface IUserService {

    boolean saveWithoutPassword(User user);

    User login(String username, String password);

    User getUserById(Long id);

    User getUserByUsername(String username);

    boolean register(User user);

    boolean updatePassword(User user);
}
