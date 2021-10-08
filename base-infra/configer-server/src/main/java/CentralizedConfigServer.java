import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * @author : thucnh
 * @mailto : thucnh96.dev@gmail.com
 * @created :07/10/2021 - 3:56 PM
 */
@SpringBootApplication
@EnableConfigServer
public class CentralizedConfigServer {
    public static void main(String[] args) {
        SpringApplication.run(CentralizedConfigServer.class, args);
    }
}
