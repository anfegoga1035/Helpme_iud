package co.edu.iudigital.helpmeiud.services;

public interface IEmailService {

    // enviar email
    boolean sendMail(String mensaje, String email, String asunto);
}
