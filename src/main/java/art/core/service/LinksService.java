package art.core.service;

import art.core.model.Link;
import art.infra.config.AppConfig;
import art.infra.config.ConfigLoader;
import lombok.NonNull;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.regex.Matcher;


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

    /**
     * @param str ссылка на сайт, формат которой проверяется
     * @return возвращает true если ссылка удовлетворяет формату, иначе - false
     */
    public static boolean isValid(String str) {
        if (str.length() == 0) return false;
        String regex = "^(http|https)://.*\\.[a-z]{2,6}(/.*)?";
        return str.matches(regex);
    }

    /**
     * @param link ссылка которую мы проверяем
     * @return возвращает true если ссылка истекла по времени, false если ссылка ещё действительна
     */
    public static boolean isExpired(Link link) {
        return link.getSurvivalTime().isBefore(LocalDateTime.now());
    }

    /**
     * @param link ссылка которую мы проверяем
     * @return возвращает true если лимит переходов ссылки был исчерпан, false если лимит ещё не исчерпан.
     */
    public static boolean isReachedLimit(@NonNull Link link) {
        return link.getForwaredTimes() > link.getForwardLimit();
    }

    /**
     * @param linkTime параметр времени для проверки. Вводится время которое мы сравниваем с лимитом.
     * @param limit параметр лимита переходов. Вводится количество которое мы сравниваем с лимитом.
     * @return возвращает true если введеные параметры удовлетворяют конфигурациям и введеное время позже настоящего времени, иначе - false.
     */
    public static boolean canCreateLink(LocalDateTime linkTime, long limit) {
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
