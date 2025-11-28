package art.core.service;

import art.cli.ShowcaseCLI;
import art.core.port.LinkRepository;
import art.core.port.UserRepository;
import art.infra.MemoryLinkRepository;

import java.util.Scanner;

public class StartupService {
    Scanner scanner;
    UserRepository userRepository;
    HandleService handleService;
    LoginService loginService;
    LinkRepository linkRepository;
    ShowcaseCLI showcaseCLI;
    FileService fileService;
    public StartupService(UserRepository userRepository, LinkRepository linkRepository) {
         this.scanner = new Scanner(System.in);
         this.userRepository = userRepository;
         this.loginService = new LoginService(userRepository);
         this.linkRepository = linkRepository;
         this.handleService = new HandleService(loginService, linkRepository ,scanner);
         this.showcaseCLI = new ShowcaseCLI(linkRepository, userRepository);
         this.fileService = new FileService(linkRepository, userRepository);
    }

    public void start() {
        System.out.println("Добро пожаловать в приложение генерации коротких ссылок!");
        while (true){
            handleService.handleMainMenu();
        }
    }
}
