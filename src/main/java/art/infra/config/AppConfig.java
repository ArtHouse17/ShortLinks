package art.infra.config;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class AppConfig {
    private long forwardLimit;

    private TimeLimit timeLimit;
    @Data
    public static class TimeLimit {
        private int year;
        private int month;
        private int day;
        private int hour;
        private int minute;
        private int second;
        public LocalDateTime getLimitTime() {
            return LocalDateTime.of(year, month,day,hour,minute,second);
        }
    }
}
