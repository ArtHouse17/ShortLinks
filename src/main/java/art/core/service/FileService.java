package art.core.service;

import art.core.model.Link;
import art.core.model.User;
import art.core.port.LinkRepository;
import art.core.port.UserRepository;

import java.io.*;
import java.util.Map;
import java.util.stream.Collectors;

public class FileService {
    private static LinkRepository linkRepository;
    private static UserRepository userRepository;
    public FileService(LinkRepository linkRepository, UserRepository userRepository) {
        this.linkRepository = linkRepository;
        this.userRepository = userRepository;
    }
    public static void save(File file, Map<Long, Link> links) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            if (!file.exists()) {
                file.createNewFile();
            } else {
                file.delete();
                file.createNewFile();
            }
            oos.writeObject(links);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void load(File file) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Map<Long,Link> links = (Map<Long,Link>) ois.readObject();
            linkRepository.saveAll(links);
            userRepository.saveAll(links.values().stream().map(Link::getUser).collect(Collectors.toMap(User::getUuid, u -> u)));
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
