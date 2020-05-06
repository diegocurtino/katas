package com.decoder.app.factory;

import com.decoder.wsdl.Contact;
import java.util.GregorianCalendar;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

public class ContactFactory {

    private ContactFactory() {
    }

    public static Contact createDefaultContact() throws DatatypeConfigurationException {
        Contact contact = new Contact();
        contact.setFirstName("John");
        contact.setLastName("Doe");
        contact.setEmailAddress("xxxxxxxx@xxxx.com");
        contact.setBirthDate(getBirthDate());
        return contact;
    }


    private static XMLGregorianCalendar getBirthDate() throws DatatypeConfigurationException {
        GregorianCalendar c = new GregorianCalendar();
        c.set(1900, 1, 1);
        return DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
    }
}