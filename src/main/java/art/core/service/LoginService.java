package art.core.service;

import art.core.exception.UserAlreadyExistException;
import art.core.model.User;
import art.core.port.UserRepository;
import lombok.Data;

import java.util.Objects;
import java.util.Scanner;

@Data
public class LoginService {
    private UserRepository userRepository;
    private User currentUser;
    //Math.abs(UUID.randomUUID().getLeastSignificantBits());
    public LoginService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public boolean login(Scanner scanner) {
        if (getCurrentUser() != null)
            return true;
        System.out.println("Введите UUID");
        String uuid = scanner.nextLine().trim();
        if (getUserRepository().exists(uuid)){
            User user = getUserRepository().findById(uuid);
            currentUser = user;
        }else{
            System.out.println("Такого пользователя не существует!");
        }
        return currentUser != null;
    }
    public User register() {
        currentUser = new User();
        System.out.println("Ваш UUID: " + currentUser.getUuid());
        setCurrentUser(currentUser);
        getUserRepository().save(currentUser);
        return currentUser;
    }

    public void unlogin() {
        currentUser = null;
    }


    public boolean checkLogin() {
        return currentUser != null;
    }
}
