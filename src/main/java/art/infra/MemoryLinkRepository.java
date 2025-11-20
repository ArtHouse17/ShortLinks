package art.infra;

import art.core.exception.LinkNotFoundException;
import art.core.model.Link;
import art.core.model.User;
import art.core.port.LinkRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MemoryLinkRepository implements LinkRepository {
    private final Map<Long,Link> links;
    public MemoryLinkRepository() {
        links = new HashMap<>();
    }
    @Override
    public void save(Link link) {
        links.put(link.getId(), link);
    }

    @Override
    public Link findById(Long id) {
        return links.get(id);
    }

    @Override
    public void delete(Long id) {
        links.remove(id);
    }

    @Override
    public void update(Link link) {
        links.put(link.getId(), link);
    }

    @Override
    public void deleteAll() {
        links.clear();
    }

    @Override
    public Map<Long, Link> findAll() {
        return links;
    }

    @Override
    public boolean exists(Long linkId) {
        if (links.containsKey(linkId))
            return true;
        return false;
    }

    @Override
    public List<Link> findAllByUser(User user) {
        return links.values().stream().filter(l -> l.getUser().equals(user)).collect(Collectors.toList());
    }

    @Override
    public User getUserByLink(Link link) {
        return null;
    }

    @Override
    public Link getLinkByUserandUrl(User user, String url) {
        return links.values().stream().filter(l -> l.getUser().equals(user) && l.getUrl().equals(url)).findFirst().orElseThrow(() -> new LinkNotFoundException("Link not found"));
    }

    @Override
    public List<Link> getLinksByUrl(String url) {
        return links.values().stream().filter(l -> l.getUrl().equals(url)).collect(Collectors.toList());
    }

    @Override
    public boolean existByUserandUrl(User user, String url) {
        return links.values().stream().anyMatch(l -> l.getUser().equals(user) && l.getUrl().equals(url));
    }

}
