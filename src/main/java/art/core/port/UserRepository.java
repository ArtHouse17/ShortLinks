package art.core.port;

import art.core.model.User;

import java.util.Map;

public interface UserRepository {
    void save(User user);
    User findByUsername(String username);
    User findById(int id);
    void delete(String username);
    void update(User user);
    void deleteAll();
    Map<String,User> findAll();
    boolean exists(String username);
}
