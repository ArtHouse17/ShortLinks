package art.core.service;

import art.core.port.UserRepository;
import art.infra.MemoryLinkRepository;

import java.util.Scanner;

public class StartupService {
    Scanner scanner;
    UserRepository userRepository;
    HandleService handleService;
    LoginService loginService;
    public StartupService(UserRepository userRepository) {
         this.scanner = new Scanner(System.in);
         this.userRepository = userRepository;
         this.loginService = new LoginService(userRepository);
         this.handleService = new HandleService(loginService, new MemoryLinkRepository(),scanner);
    }

    public void start() {
        System.out.println("Добро пожаловать в приложение генерации коротких ссылок!");
        while (true){
            if (loginService.checkLogin()){
                handleService.handleMainMenu();
            }else{
                handleService.handleLoginMenu();
            }
        }
    }
}
