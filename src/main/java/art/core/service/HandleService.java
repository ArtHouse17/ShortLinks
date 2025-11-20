package art.core.service;

import art.core.model.Link;
import art.core.model.User;
import art.core.port.LinkRepository;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.Scanner;

public class HandleService {
    private static LoginService loginService;
    private static LinksService linksService;
    private static LinkRepository linkRepository;
    private Scanner scanner;
    public HandleService(LoginService loginService, LinkRepository linkRepository ,Scanner scanner) {
        this.loginService = loginService;
        this.linksService = new LinksService();
        this.linkRepository = linkRepository;
        this.scanner = scanner;
    }

    public void handleLoginMenu(){
        switch(scanner.nextLine().trim()){
            case "1":
                loginService.login(scanner);
                break;
            case "2":
                loginService.register(scanner);
                break;
            case "3":
                System.exit(0);
                break;
            default:
                System.out.println("Invalid input");
                break;
        }
    }

    public void handleMainMenu() {
        switch(scanner.nextLine().trim()){
            case "1":
                handleGenerateLink();
                break;
            case "2":
                handleTransferURL();
                break;
        }
    }

    private void handleTransferURL() {
        String link = scanner.nextLine().trim();
        try {
            Desktop.getDesktop().browse(new URI(linkRepository.findById(LinksService.decode(link)).getUrl()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

    }

    private void handleGenerateLink() {
        User currentUser = loginService.getCurrentUser();
        System.out.println("Введите ссылку");
        String link = scanner.nextLine().trim();
        if (!linkRepository.existByUserandUrl(currentUser, link)) {
            System.out.println("Напишите количество переходов");
            Long timesToLink = scanner.nextLong();
            Link newShortLink = new Link(link, currentUser,timesToLink, LocalDateTime.MAX);
            linkRepository.save(newShortLink);
            System.out.println("Ссылка создана" + LinksService.encode(newShortLink.getId()));
        }else{
            System.out.println("Link already exists");
        }
    }
}
