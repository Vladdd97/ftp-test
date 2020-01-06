package com.faf.ftptest;

import org.apache.commons.net.ftp.FTPFile;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.file.FileNameGenerator;
import org.springframework.integration.file.remote.session.CachingSessionFactory;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.integration.ftp.outbound.FtpMessageHandler;
import org.springframework.integration.ftp.session.DefaultFtpSessionFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@SpringBootApplication
@IntegrationComponentScan
public class FtpTestApplication {

    public static void main(String[] args) throws IOException {


        SpringApplication.run(FtpTestApplication.class, args);
//
//		ConfigurableApplicationContext context =
//				new SpringApplicationBuilder(FtpTestApplication.class)
//						//.web(false)
//						.run(args);
//		MyGateway gateway = context.getBean(MyGateway.class);
//		gateway.sendToFtp(Files.readAllBytes(Paths.get("C:\\Users\\Vlad\\Desktop\\si_lab_7\\test123.txt")));
    }


    @Bean
    public SessionFactory<FTPFile> ftpSessionFactory() {
        DefaultFtpSessionFactory sf = new DefaultFtpSessionFactory();
        sf.setHost("localhost");
        sf.setPort(21);
        sf.setUsername("gabi");
        sf.setPassword("gabi");
        return new CachingSessionFactory<FTPFile>(sf);
    }

    @Bean
    @ServiceActivator(inputChannel = "ftpChannel")
    public MessageHandler handler() {
        FtpMessageHandler handler = new FtpMessageHandler(ftpSessionFactory());
        handler.setRemoteDirectoryExpressionString("headers['remote-target-dir']");
        //handler.setFileNameGenerator(message -> "handlerContent.txt");
        return handler;
    }

    @MessagingGateway
    public interface MyGateway {

        @Gateway(requestChannel = "ftpChannel")
        void sendToFtp(byte[] file);

    }


}
