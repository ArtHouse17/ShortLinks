package art.core.service;

import art.core.exception.UserAlreadyExistException;
import art.core.model.User;
import art.core.port.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private Scanner scanner;

    private LoginService loginService;

    @BeforeEach
    void setUp() {
        loginService = new LoginService(userRepository);
    }

    @Test
    void testLogin_Successful() {

        String uuid = "test-uuid-123";
        User mockUser = mock(User.class);

        when(scanner.nextLine()).thenReturn(uuid);
        when(userRepository.exists(uuid)).thenReturn(true);
        when(userRepository.findById(uuid)).thenReturn(mockUser);


        boolean result = loginService.login(scanner);


        assertTrue(result);
        assertEquals(mockUser, loginService.getCurrentUser());
        verify(userRepository).exists(uuid);
        verify(userRepository).findById(uuid);
    }

    @Test
    void testLogin_UserNotFound() {

        String uuid = "non-existent-uuid";

        when(scanner.nextLine()).thenReturn(uuid);
        when(userRepository.exists(uuid)).thenReturn(false);


        boolean result = loginService.login(scanner);


        assertFalse(result);
        assertNull(loginService.getCurrentUser());
        verify(userRepository).exists(uuid);
        verify(userRepository, never()).findById(anyString());
    }

    @Test
    void testLogin_WithWhitespace() {

        String uuidWithSpaces = "  test-uuid  ";
        String trimmedUuid = "test-uuid";
        User mockUser = mock(User.class);

        when(scanner.nextLine()).thenReturn(uuidWithSpaces);
        when(userRepository.exists(trimmedUuid)).thenReturn(true);
        when(userRepository.findById(trimmedUuid)).thenReturn(mockUser);


        boolean result = loginService.login(scanner);


        assertTrue(result);
        verify(userRepository).exists(trimmedUuid);
    }

    @Test
    void testLogin_AlreadyLoggedIn() {

        User existingUser = mock(User.class);
        loginService.setCurrentUser(existingUser);


        boolean result = loginService.login(scanner);


        assertTrue(result);
        verify(scanner, never()).nextLine();
        verify(userRepository, never()).exists(anyString());
    }

    @Test
    void testRegister_Successful() {

        User result = loginService.register();


        assertNotNull(result);
        assertNotNull(result.getUuid());
        assertEquals(result, loginService.getCurrentUser());
        verify(userRepository).save(result);
    }

    @Test
    void testRegister_NewUserHasUUID() {

        User result = loginService.register();


        assertNotNull(result.getUuid());
        assertFalse(result.getUuid().isEmpty());
    }

    @Test
    void testUnlogin() {

        User mockUser = mock(User.class);
        loginService.setCurrentUser(mockUser);


        loginService.unlogin();


        assertNull(loginService.getCurrentUser());
    }

    @Test
    void testCheckLogin_WhenLoggedIn() {

        User mockUser = mock(User.class);
        loginService.setCurrentUser(mockUser);


        assertTrue(loginService.checkLogin());
    }

    @Test
    void testCheckLogin_WhenNotLoggedIn() {

        assertFalse(loginService.checkLogin());
    }

    @Test
    void testCheckLogin_AfterUnlogin() {

        User mockUser = mock(User.class);
        loginService.setCurrentUser(mockUser);


        loginService.unlogin();


        assertFalse(loginService.checkLogin());
    }

    @Test
    void testIntegration_RegisterThenLogin() {

        User registeredUser = loginService.register();
        String userUuid = registeredUser.getUuid();


        loginService.unlogin();


        when(scanner.nextLine()).thenReturn(userUuid);
        when(userRepository.exists(userUuid)).thenReturn(true);
        when(userRepository.findById(userUuid)).thenReturn(registeredUser);


        boolean loginResult = loginService.login(scanner);


        assertTrue(loginResult);
        assertEquals(registeredUser, loginService.getCurrentUser());
    }

    @Test
    void testConstructor_InjectsRepository() {

        assertEquals(userRepository, loginService.getUserRepository());
    }

    @Test
    void testLogin_EmptyUUID() {

        when(scanner.nextLine()).thenReturn("");
        when(userRepository.exists("")).thenReturn(false);


        boolean result = loginService.login(scanner);


        assertFalse(result);
        assertNull(loginService.getCurrentUser());
    }


    @Test
    void testMultipleLoginAttempts() {

        String firstUuid = "first-uuid";
        String secondUuid = "second-uuid";
        User mockUser = mock(User.class);

        when(scanner.nextLine())
                .thenReturn(firstUuid)
                .thenReturn(secondUuid);
        when(userRepository.exists(firstUuid)).thenReturn(false);
        when(userRepository.exists(secondUuid)).thenReturn(true);
        when(userRepository.findById(secondUuid)).thenReturn(mockUser);


        boolean firstResult = loginService.login(scanner);


        boolean secondResult = loginService.login(scanner);


        assertFalse(firstResult);
        assertTrue(secondResult);
        assertEquals(mockUser, loginService.getCurrentUser());
    }
}