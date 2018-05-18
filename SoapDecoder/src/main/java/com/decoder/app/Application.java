package com.decoder.app;

import com.decoder.app.connector.SOAPConnector;
import com.decoder.app.connector.SoapConfiguration;
import com.decoder.app.factory.ContactFactory;
import com.decoder.app.factory.MessageFactory;
import com.decoder.wsdl.TickstarInputMessage;
import com.decoder.wsdl.TickstarOutputMessage;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    CommandLineRunner lookup(SOAPConnector soapConnector) {
        return args -> {
            TickstarInputMessage message = MessageFactory
                  .from("MessageToDecode.txt", ContactFactory.createDefaultContact());

            TickstarOutputMessage response = (TickstarOutputMessage)
                  soapConnector.callWebService(SoapConfiguration.SOAP_ENDPOINT, message);

            System.out.println(response.getOutput());
        };
    }
}
