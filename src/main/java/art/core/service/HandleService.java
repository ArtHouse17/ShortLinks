package art.core.service;

import art.cli.ShowcaseCLI;
import art.core.exception.CannotAllowToCreateLink;
import art.core.model.Link;
import art.core.model.User;
import art.core.port.LinkRepository;
import art.infra.config.ConfigLoader;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class HandleService {
    private static LoginService loginService;
    private static LinksService linksService;
    private static LinkRepository linkRepository;
    private static ShowcaseCLI showcaseCLI;
    private static File file;
    private Scanner scanner;
    public HandleService(LoginService loginService, LinkRepository linkRepository ,Scanner scanner) {
        this.loginService = loginService;
        this.linksService = new LinksService();
        this.linkRepository = linkRepository;
        file = new File("users.dat");
        this.scanner = scanner;
    }


    public void handleMainMenu() {
        FileService.load(file);
        ShowcaseCLI.showMainMenu();
        switch(scanner.nextLine().trim()){
            case "1":
                handleGenerateLink();
                break;
            case "2":
                handleTransferURL();
                break;
            case "3":
                loginService.unlogin();
                break;
            case "4":
                HandleChangingURLs();
                break;
            case "5":
                loginService.login(scanner);
                break;
            case "6":
                FileService.save(file, linkRepository.findAll());
                System.exit(0);
                break;
        }
    }

    private void HandleChangingURLs() {
        if (loginService.login(scanner)){
            System.out.println("Выберите ссылку что вы хотите заменить");
            ShowcaseCLI.showLinksForUpdateMenu(loginService.getCurrentUser());
            switch(scanner.nextLine().trim()){
                case "1":
                    handleUpdateURL();
                    break;
                case "2":
                    handleRemovalURL();
                    break;
            }
        }else{
            System.out.println("Авторизуйтесь для работы в этом разделе!");
        }
    }

    private void handleUpdateURL() {
        System.out.println("Введите короткую ссылку для изменения!");
        String linkId = scanner.nextLine().trim();
        handleUpdateURLMenu(linkId);
    }

    private void handleUpdateURLMenu(String linkId) {
        Long decodedLinkId = LinksService.decode(linkId);
        Link link = linkRepository.findById(decodedLinkId);
        if (link == null)
            return;
        System.out.println("Выберите дальнейшее действие:");
        String param = scanner.nextLine().trim();
        switch(param){
            case "1":
                String newForwardLimit = scanner.nextLine().trim();
                if (linksService.canCreateLink(LocalDateTime.now(), Long.parseLong(newForwardLimit))) {
                    link.setForwardLimit(Long.parseLong(newForwardLimit));
                }else{
                    System.out.println("Вы ввели значение превышающее лимит!");
                }
                break;
            case "2":
                String newSurvivalLimitString = scanner.nextLine().trim();
                LocalDateTime newSurvivalLimit = LocalDateTime.parse(newSurvivalLimitString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                if (linksService.canCreateLink(newSurvivalLimit, -1)) {
                    link.setSurvivalTime(newSurvivalLimit);
                }else{
                    System.out.println("Вы ввели значение превышающее лимит!");
                }
                break;
        }
    }

    private void handleRemovalURL() {
        System.out.println("Введите короткую ссылку для удаления!!");
        String linkId = scanner.nextLine().trim();
        handleRemovalMenuURL(linkId);
    }

    private void handleRemovalMenuURL(String linkId) {
        Long decodedLinkId = LinksService.decode(linkId);
        Link link = linkRepository.findById(decodedLinkId);
        if (link == null)
            return;
        System.out.println("Вы уверены что вы хотите удалить ссылку?");
        switch(scanner.nextLine().trim().toLowerCase()){
            case "y":
                linkRepository.delete(Long.parseLong(linkId));
                break;
            case "n":
                break;
        }
    }

    private void handleTransferURL() {
        String link = scanner.nextLine().trim();
        try {
            Link currentLink = linkRepository.findById(LinksService.decode(link.replace(InetAddress.getLocalHost().getHostAddress() + "/", "")));
            if (linksService.isExpired(currentLink)) {
                System.out.println("Время ссылки окончено!");
                linkRepository.delete(currentLink.getId());
                return;
            }
            if (linksService.isReachedLimit(currentLink)) {
                System.out.println("Количество переходов превышено");
                linkRepository.delete(currentLink.getId());
                return;
            }
            Desktop.getDesktop().browse(new URI(currentLink.getUrl()));
            currentLink.setForwaredTimes(currentLink.getForwaredTimes()+1);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (NullPointerException e){
            System.out.println("Вы ввели неверную ссылку!");
        }

    }

    private void handleGenerateLink() {
        try {
            User currentUser = loginService.getCurrentUser();
            if (currentUser == null) {
                currentUser = loginService.register();
            }
            System.out.println("Введите ссылку");
            String link = scanner.nextLine().trim();
            if (!linkRepository.existByUserandUrl(currentUser, link)) {
                System.out.println("Напишите количество переходов, не превышающее" + ConfigLoader.load().getForwardLimit());
                String timesToLink = scanner.nextLine().trim();
                System.out.println("Введите время жизни ссылки не превышающее" );
                LocalDateTime forwardedTime = LocalDateTime.parse(scanner.nextLine().trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                if (!linksService.canCreateLink(forwardedTime, Long.parseLong(timesToLink))) {
                    throw new CannotAllowToCreateLink("Пользователь превысил лимит");
                }
                System.out.println("Текущий пользователь " + currentUser);
                Link newShortLink = new Link(link, currentUser, timesToLink, forwardedTime);
                linkRepository.save(newShortLink);
                System.out.println("Ссылка создана" + InetAddress.getLocalHost().getHostAddress()+ LinksService.encode(newShortLink.getId()));
            } else {
                System.out.println("Link already exists");
            }
        }catch (CannotAllowToCreateLink e) {
            System.out.println("Вы ввели значение больше лимита!");
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }
}
