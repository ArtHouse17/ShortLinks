package art.core.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class User {
    int uuid;
    String name;
    String password;
    public User(int uuid, String name, String password) {
        this.uuid = uuid;
        this.name = name;
        this.password = password;
    }
}
