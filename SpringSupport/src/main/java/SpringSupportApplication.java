import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@PropertySource(value = "classpath:/*.yml", ignoreResourceNotFound = true)
@EnableAutoConfiguration
@Configuration
@ComponentScan({"org.test"})
@SpringBootApplication
public class SpringSupportApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringSupportApplication.class, args);
    }
}
