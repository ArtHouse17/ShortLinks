package art.core.service;

import art.core.model.Link;
import art.infra.config.AppConfig;
import art.infra.config.ConfigLoader;

import java.time.Duration;
import java.time.LocalDateTime;


public class LinksService {
    private static final String CHARSET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int BASE = CHARSET.length(); // 62
    public static String encode(long num) {
        if (num == 0) return "0";

        StringBuilder sb = new StringBuilder();

        while (num > 0) {
            int index = (int)(num % BASE);
            sb.append(CHARSET.charAt(index));
            num /= BASE;
        }

        return sb.reverse().toString();
    }

    public static long decode(String str) {
        long result = 0;

        for (int i = 0; i < str.length(); i++) {
            result = result * BASE + CHARSET.indexOf(str.charAt(i));
        }

        return result;
    }
    public boolean isValid(String str) {
        if (str.length() == 0) return false;
        String regex = "[0-9]";
        return false;
    }
    public boolean isExpired(Link link) {
        return link.getSurvivalTime().isBefore(LocalDateTime.now());
    }

    public boolean isReachedLimit(Link link) {
        return link.getForwaredTimes() > link.getForwardLimit();
    }
    public boolean canCreateLink(LocalDateTime linkTime, long limit) {
        AppConfig appConfig = ConfigLoader.load();
        AppConfig.TimeLimit timeLimit = appConfig.getTimeLimit();
        LocalDateTime nowPlusLimit = LocalDateTime.now().plusYears(timeLimit.getYear())
                .plusMonths(timeLimit.getMonth())
                .plusDays(timeLimit.getDay())
                .plusHours(timeLimit.getHour())
                .plusMinutes(timeLimit.getMinute())
                .plusSeconds(timeLimit.getSecond());
        return appConfig.getForwardLimit() >= limit && nowPlusLimit.isAfter(linkTime) && linkTime.isAfter(LocalDateTime.now()) ;
    }
}
