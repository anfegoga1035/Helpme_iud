package co.edu.iudigital.helpmeiud.configs;

import co.edu.iudigital.helpmeiud.utils.Constants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailConfig {

    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.port}")
    private int port;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;

    @Value("${spring.mail.properties.protocol}")
    private String protocol;

    @Value("${spring.mail.properties.auth}")
    private String auth;

    @Value("${spring.mail.properties.starttls.enabled}")
    private String starttls;

    @Bean
    public JavaMailSender sender() {

        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(host);
        javaMailSender.setPort(port);
        javaMailSender.setUsername(username);
        javaMailSender.setPassword(password);

        Properties properties = javaMailSender.getJavaMailProperties();
        properties.put(Constants.MAIL_TRANSPORT_PROTOCOL, protocol);
        properties.put(Constants.MAIL_SMTP_AUTH, auth);
        properties.put(Constants.MAIL_SMTP_STARTTLS_ENABLE, starttls);

        return javaMailSender;
    }
}
