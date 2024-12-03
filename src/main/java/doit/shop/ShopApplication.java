package doit.shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Arrays;

@SpringBootApplication
public class ShopApplication {

    public static void main(String[] args) {

        try {
            ApplicationContext context = SpringApplication.run(ShopApplication.class, args);
            System.out.println("Loaded beans: " + Arrays.toString(context.getBeanDefinitionNames()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
