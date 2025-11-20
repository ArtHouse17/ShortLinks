package art.infra;

import art.core.model.User;
import art.core.port.UserRepository;

import java.util.HashMap;
import java.util.Map;

public class MemoryUserRepository implements UserRepository {

    Map<String, User> users;
    public MemoryUserRepository() {
        users = new HashMap<>();
    }
    @Override
    public void save(User user) {
        users.put(user.getName(), user);
    }

    @Override
    public User findByUsername(String username) {
        return users.get(username);
    }

    @Override
    public User findById(int id) {
        return users.values().stream().filter(u -> u.getUuid() == id).findFirst().orElse(null);
    }

    @Override
    public void delete(String username) {
        users.remove(username);
    }

    @Override
    public void update(User user) {
        users.put(user.getName(), user);
    }

    @Override
    public void deleteAll() {
        users.clear();
    }

    @Override
    public Map<String,User> findAll() {
        return users;
    }

    @Override
    public boolean exists(String username) {
        return users.containsKey(username);
    }
}
