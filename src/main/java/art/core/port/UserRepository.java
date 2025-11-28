package art.core.port;

import art.core.model.User;

import java.util.Map;

public interface UserRepository {
    void save(User user);
    void saveAll(Map<String,User> users);
    User findById(String id);
    void delete(String uuid);
    void update(User user);
    void deleteAll();
    Map<String,User> findAll();
    boolean exists(String username);
}
