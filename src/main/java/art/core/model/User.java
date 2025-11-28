package art.core.model;

import lombok.Data;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    String uuid;
    final int SHORT_ID_LENGHT = 8;
    public User() {
        this.uuid = RandomStringUtils.randomAlphanumeric(SHORT_ID_LENGHT);
    }
}
