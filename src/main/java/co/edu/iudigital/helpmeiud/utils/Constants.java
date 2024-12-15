package co.edu.iudigital.helpmeiud.utils;

import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;

public interface Constants {

    String ROLE_USER = "ROLE_USER";

    String MAIL_TRANSPORT_PROTOCOL = "mail.transport.protocol";

    String MAIL_SMTP_AUTH = "mail.smtp.auth";

    String MAIL_SMTP_STARTTLS_ENABLE = "mail.smtp.starttls.enable";

    // security

    SecretKey SECRET_KEY = Jwts.SIG.HS256.key().build();

    String TOKEN = "token";

    String USERNAME = "username";

    String MESSAGE = "message";

    String BEARER = "Bearer";

    String SESSION_EXITOSO = "Inicio de sesión exitoso: ";

    String AUTHORITIES = "authorities";

    String SESSION_ERROR = "Error al intentar autenticarse";

    String ERROR = "Error";
}
