package art.core.model;

import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class Link {
    Long id;
    String url;
    User user;
    Long forwaredTimes;
    Long forwardLimit;
    Timestamp createdTime;
    LocalDateTime survivalTime;

    public Link(String url, User user, Long forwardLimit, LocalDateTime survivalTime) {
        id = Math.abs(UUID.randomUUID().getLeastSignificantBits());
        this.url = url;
        this.user = user;
        this.forwaredTimes = 0l;
        this.forwardLimit = forwardLimit;
        this.createdTime = Timestamp.valueOf(LocalDateTime.now());
        this.survivalTime = survivalTime;
    }
}
