package com.decoder.app.factory;

import com.decoder.wsdl.Contact;
import com.decoder.wsdl.TickstarInputMessage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MessageFactory {

    private MessageFactory() {
    }

    private static String readMessageToDecode(String payloadFileName) throws IOException {
        Path path = Paths.get("src/main/resources/" + payloadFileName);
        return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
    }

    public static TickstarInputMessage from(String payloadFilename, Contact contact) throws IOException {
        TickstarInputMessage message = new TickstarInputMessage();
        message.setInput(readMessageToDecode(payloadFilename));
        message.setContact(contact);
        return message;
    }
}
