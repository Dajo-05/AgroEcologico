package com.example.agroecologico;


import android.content.Context;
import android.os.StrictMode;
import android.widget.Toast;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;


public class ManejadorCorreo {
    private static final String DE = "dajo0518@gmail.com";
    private static final String PASSWORD = "omega0418";

    private String para;
    private String asunto;
    private String mensaje;
    private Context context;

    public ManejadorCorreo(String para, String asunto, String mensaje, Context context ){
        this.para=para;
        this.asunto=asunto;
        this.mensaje=mensaje;
        this.context=context;
    }

    public void enviarEmail() {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Properties properties = new Properties();
        properties.put("mail.smtp.auth","true");
        properties.put("mail.smtp.starttls.enable","true");
        properties.put("mail.smtp.host","smtp.gmail.com");
        properties.put("mail.smtp.port","587");

        Session session = Session.getDefaultInstance(properties,
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(DE, PASSWORD);
                    }
                });

        try{
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(DE));
            message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(para));
            message.setSubject(asunto);
            message.setText(mensaje);
            Transport.send(message);
            Toast.makeText(context, "email enviado!!", Toast.LENGTH_SHORT).show();
        }catch (MessagingException e){
            throw  new RuntimeException(e);
        }

    }
}
