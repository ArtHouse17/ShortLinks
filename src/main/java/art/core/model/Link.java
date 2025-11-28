package art.core.model;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class Link implements Serializable {
    private static final long serialVersionUID = 1L;
    Long id;
    String url;
    User user;
    Long forwaredTimes;
    Long forwardLimit;
    Timestamp createdTime;
    LocalDateTime survivalTime;

    public Link(String url, User user, String forwardLimit, LocalDateTime survivalTime) {
        id = Math.abs(UUID.randomUUID().getLeastSignificantBits());
        this.url = url;
        this.user = user;
        this.forwaredTimes = 0l;
        this.forwardLimit = Long.parseLong(forwardLimit);
        this.createdTime = Timestamp.valueOf(LocalDateTime.now());
        this.survivalTime = survivalTime;
    }
}
