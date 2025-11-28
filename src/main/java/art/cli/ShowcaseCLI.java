package art.cli;

import art.core.model.User;
import art.core.port.LinkRepository;
import art.core.port.UserRepository;
import art.core.service.LinksService;

import java.util.concurrent.atomic.AtomicInteger;

public class ShowcaseCLI {
    private static LinkRepository linkRepository;
    private static UserRepository userRepository;
    public ShowcaseCLI(LinkRepository linkRepository, UserRepository userRepository) {
        this.linkRepository = linkRepository;
        this.userRepository = userRepository;
    }

    public static void showMainMenu(){
        System.out.println("Главное меню");
        System.out.println("1. Создать ссылку");
        System.out.println("2. Перейти по ссылке");
        System.out.println("3. Выйти из аккаунта");
        System.out.println("4. Изменить ссылку");
        System.out.println("5. Авторизоваться");
        System.out.println("6. Выйти из системы");
    }
    public static void showLinksForUpdateMenu(User user){
        AtomicInteger atomicInteger = new AtomicInteger(0);
        linkRepository.findAllByUser(user).forEach(link -> {
            System.out.println(atomicInteger.incrementAndGet() + "Ссылка " + link.getUrl() + "Короткая ссылка " + LinksService.encode(link.getId()));
        });
    }
}
