package art.cli;

import art.core.model.Link;
import art.core.model.User;
import art.core.port.LinkRepository;
import art.core.port.UserRepository;
import art.core.service.LinksService;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ShowcaseCLI {
    private static LinkRepository linkRepository;
    private static UserRepository userRepository;
    public ShowcaseCLI(LinkRepository linkRepository, UserRepository userRepository) {
        this.linkRepository = linkRepository;
        this.userRepository = userRepository;
    }

    public static void showMainMenu() {
        System.out.println("╔══════════════════════════════╗");
        System.out.println("║          ГЛАВНОЕ МЕНЮ        ║");
        System.out.println("╠══════════════════════════════╣");
        System.out.println("║ 1. Создать ссылку            ║");
        System.out.println("║ 2. Перейти по ссылке         ║");
        System.out.println("║ 3. Выйти из аккаунта         ║");
        System.out.println("║ 4. Изменить ссылку           ║");
        System.out.println("║ 5. Авторизоваться            ║");
        System.out.println("║ 6. Выйти из системы          ║");
        System.out.println("╚══════════════════════════════╝");
        System.out.print("Выберите действие: ");
    }
    public static void showLinksForChangingMenu(User user) {
        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║               МЕНЮ УПРАВЛЕНИЯ ССЫЛКАМИ           ║");
        System.out.println("╠══════════════════════════════════════════════════╣");

        AtomicInteger atomicInteger = new AtomicInteger(0);
        List<Link> links = linkRepository.findAllByUser(user);
        if (links.isEmpty()) {
            System.out.println("║              Нет доступных ссылок                ║");
        }
        links.forEach(link -> {
            int id = atomicInteger.getAndIncrement();
            String url = link.getUrl();
            String shortUrl = LinksService.encode(link.getId());
            if (url.length() > 35) {
                url = url.substring(0, 32) + "...";
            }
            System.out.printf("║ %2d. %-44s ║%n", id, url);
            System.out.printf("║     Короткая: %-34s ║%n", shortUrl);

            if (atomicInteger.get() <= links.size()) {
                System.out.println("║══════════════════════════════════════════════════║");
            }
        });
        System.out.println("╚══════════════════════════════════════════════════╝");

        System.out.println("╔══════════════════════════════╗");
        System.out.println("║ 1. Обновить ссылку           ║");
        System.out.println("║ 2. Удалить ссылку            ║");
        System.out.println("╚══════════════════════════════╝");
        System.out.print("Выберите действие для работы с ссылкой: ");
    }

    public static void showLinksForUpdateMenu() {
        System.out.println("╔══════════════════════════════╗");
        System.out.println("║ 1. Обновить кол-во переходов ║");
        System.out.println("║ 2. Изменить время жизни      ║");
        System.out.println("║ ссылки                       ║");
        System.out.println("╚══════════════════════════════╝");
    }
    public static void showLinksForDeleteMenu() {
        System.out.println("╔══════════════════════════════╗");
        System.out.println("║ Вы уверены что хотите        ║");
        System.out.println("║ удалить ссылку?              ║");
        System.out.println("╠══════════════════════════════╣");
        System.out.println("║ Y. Да, удалить ссылку        ║");
        System.out.println("║ N. Не удалять ссылку         ║");
        System.out.println("╚══════════════════════════════╝");
    }
}
