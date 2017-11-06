package com.jptest.payments.fulfillment.testonia.model.util.jaxb;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;

public class JAXBHelper {

    public static <T> Document convertJaxbToDocument(T instance) throws JAXBException, ParserConfigurationException {
        JAXBContext jc = JAXBContext.newInstance(instance.getClass());
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

        Marshaller marshaller = jc.createMarshaller();
        marshaller.marshal(instance, document);
        return document;
    }
}
