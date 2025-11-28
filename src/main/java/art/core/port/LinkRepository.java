package art.core.port;

import art.core.model.Link;
import art.core.model.User;

import java.util.List;
import java.util.Map;

public interface LinkRepository {

    void save(Link link);
    void saveAll(Map<Long,Link> links);
    Link findById(Long id);
    void delete(Long id);
    void update(Link link);
    void deleteAll();
    Map<Long,Link> findAll();
    boolean exists(Long linkId);
    List<Link> findAllByUser(User user);
    User getUserByLink(Link link);
    Link getLinkByUserandUrl(User user, String url);
    List<Link> getLinksByUrl(String url);
    boolean existByUserandUrl(User user, String url);
}
