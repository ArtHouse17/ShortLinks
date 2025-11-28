package art;

import art.core.model.User;
import art.core.service.HandleService;
import art.core.service.LinksService;
import art.core.service.LoginService;
import art.core.service.StartupService;
import art.infra.MemoryLinkRepository;
import art.infra.MemoryUserRepository;

import java.util.UUID;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        StartupService startupService = new StartupService(new MemoryUserRepository(), new MemoryLinkRepository());
        startupService.start();
    }
}
