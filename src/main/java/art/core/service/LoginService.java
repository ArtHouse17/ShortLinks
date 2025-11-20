package art.core.service;

import art.core.exception.UserAlreadyExistException;
import art.core.model.User;
import art.core.port.UserRepository;
import lombok.Data;

import java.util.Objects;
import java.util.Scanner;
import java.util.UUID;

@Data
public class LoginService {
    UserRepository userRepository;
    User currentUser;
    public LoginService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public void login(Scanner scanner) {
        System.out.println("Введите логин");
        String username = scanner.nextLine().trim();
        if (getUserRepository().exists(username)){
            User user = getUserRepository().findByUsername(username);
            System.out.println("Введите пароль!");
            String password = scanner.nextLine().trim();
            if (user.getPassword().equals(password)){
                setCurrentUser(user);
            }
        }else{
            System.out.println("Такого пользователя не существует!");
        }
    }
    public void register(Scanner scanner) {
        System.out.println("Введите логин");
        String username = scanner.nextLine().trim();
        if (getUserRepository().exists(username)){
            throw new UserAlreadyExistException("Пользователь уже существует!");
        }
        System.out.println("Введите пароль");
        String password = scanner.nextLine().trim();
        currentUser = new User(generateId(username), username, password);
        getUserRepository().save(currentUser);
    }

    public void unlogin() {
        currentUser = null;
    }

    public static int generateId(String name) {
        return Objects.hash(name);
    }

    public boolean checkLogin() {
        if (currentUser == null){
            return false;
        }
        return true;
    }
}
