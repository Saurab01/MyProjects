package com.personal.common.utilities;

/**
 * Created by saurabhagrawal on 21/06/16.
 */
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class EmailNotification {
    public static void mailSendSimple(String host,String mail_subject,String mail_text){
        Properties props = new Properties();   //to fill all information like hostname
        props.put("mail.host", host);

        Session mailSession = Session.getInstance(props, null);  //returns new Instance
        //javax.mail.Session object stores all the information of host like host name, username, password etc.
        Message msg = new MimeMessage(mailSession);  //session object passed

      //  Address a = new InternetAddress("a@a.com", "A a");
       // Address b = new InternetAddress("fake@java2s.com");

        try {
            msg.setContent("Mail contect", "text/plain");
           // msg.setFrom(a);
            //msg.setRecipient(Message.RecipientType.TO, b); //cc //bcc
            msg.setSubject(mail_subject);
            msg.setText(mail_text);

            Transport.send(msg);  //to send the message
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }
    public static void mailSendAttachment(String host,String mail_subject,String mail_text){

        Properties props = new Properties();
        props.put("mail.host", host);
        Session mailSession = Session.getInstance(props, null);

        //compose message
        try{
            Message message = new MimeMessage(mailSession);
           // message.setFrom(new InternetAddress(user));
            //message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
            message.setSubject(mail_subject);

            //3) create MimeBodyPart object and set your message text
            BodyPart messageBodyPart1 = new MimeBodyPart();
            messageBodyPart1.setText(mail_text);

            //4) create new MimeBodyPart object and set DataHandler object to this object
            MimeBodyPart messageBodyPart2 = new MimeBodyPart();

            String filename = "SendAttachment.java";//change accordingly
            DataSource source = new FileDataSource(filename);
            messageBodyPart2.setDataHandler(new DataHandler(source));
            messageBodyPart2.setFileName(filename);


            //5) create Multipart object and add MimeBodyPart objects to this object
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart1);
            multipart.addBodyPart(messageBodyPart2);

            //6) set the multiplart object to the message object
            message.setContent(multipart );

            //7) send message
            Transport.send(message);

            System.out.println("message sent....");
        }catch (MessagingException ex) {ex.printStackTrace();}
    }
}



