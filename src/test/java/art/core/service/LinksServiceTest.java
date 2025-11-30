package art.core.service;

import art.core.model.Link;
import art.core.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class LinksServiceTest {

    @Test
    void encode() {
        String encoded = LinksService.encode(200L);
        assertEquals(encoded, "3e");
    }

    @Test
    void decode() {
        Long decoded = LinksService.decode("3e");
        assertEquals(decoded, 200L);

    }

    @Test
    void isValid() {
        boolean isValid = LinksService.isValid("https://www.google.com");
        assertTrue(isValid);
    }

    @Test
    void isExpired() {
        Link newLink = new Link("https://www.google.com", new User(), "2", LocalDateTime.now().plusMinutes(29));
        boolean isExpired = LinksService.isExpired(newLink);

        assertFalse(isExpired);
    }

    @Test
    void isReachedLimit() {
        Link newLink = new Link("https://www.google.com", new User(), "1", LocalDateTime.now().plusMinutes(29));
        newLink.setForwaredTimes(10L);
        boolean isReachedLimit = LinksService.isReachedLimit(newLink);
        assertTrue(isReachedLimit);
    }

    @Test
    void canCreateLink() {
        boolean canCreateLink = LinksService.canCreateLink(LocalDateTime.now().plusMinutes(10), 3);
        assertTrue(canCreateLink);
    }


}