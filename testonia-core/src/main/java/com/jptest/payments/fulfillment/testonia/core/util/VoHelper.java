package com.jptest.payments.fulfillment.testonia.core.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.jptest.payments.fulfillment.testonia.core.util.xml.XMLHelper;
import com.jptest.vo.ValueObject;
import com.jptest.vo.serialization.Formats;
import com.jptest.vo.serialization.UniversalDeserializer;
import com.jptest.vo.serialization.UniversalSerializer;

/**
 *
 * @JP Inc.
 *
 *         VoHelper facilitates loading ValueObject objects from resources for
 *         testing and etc.
 */
public class VoHelper {

	private static final Logger LOGGER = LoggerFactory.getLogger(VoHelper.class);

	private static UniversalDeserializer deserializer = new UniversalDeserializer();

	@SuppressWarnings("unchecked")
	public static <T> T loadVO(Class<T> type, String resource) throws IOException {
		Validate.notNull(type, "Class type was not provided");
		Validate.notEmpty(resource, "resource name must not be empty");
		InputStream is = getInputStreamForResource(resource);
		return (T) deserializer.deserialize(is);

	}

	protected static InputStream getInputStreamForResource(String resourceName) throws IOException {
		InputStream is = ClassLoader.class.getResourceAsStream(resourceName);
		if (is == null) {
			throw new FileNotFoundException(resourceName + " is not loadable as a stream");
		}
		return is;
	}

	public static ValueObject deserializeByteArray2VO(byte[] message) throws IOException {
		UniversalDeserializer deserializer = new UniversalDeserializer();
		ByteArrayInputStream inputStream = new ByteArrayInputStream(message);
		return deserializer.deserialize(inputStream);
	}

	@SuppressWarnings("unchecked")
	public static <T> T deserializeByteArray2VO(Class<T> type, byte[] voByteArray) throws IOException {
		UniversalDeserializer deserializer = new UniversalDeserializer();
		ByteArrayInputStream inputStream = new ByteArrayInputStream(voByteArray);
		return (T) deserializer.deserialize(inputStream);
	}

	/**
	 * Method to serialize a jptest {@link com.jptest.vo.ValueObject} to a XML
	 * String.
	 *
	 * @param obj
	 *            - {@link com.jptest.vo.ValueObject} which needs to be
	 *            transformed into a XML String.
	 * @return the XML String of the {@link com.jptest.vo.ValueObject}
	 * @throws IOException
	 */
	public static final String printValueObject(ValueObject obj) {
		String xmlString = null;
		UniversalSerializer serlializer = new UniversalSerializer(Formats.XML, false, false, false);
		OutputStream out = new ByteArrayOutputStream();
		try {
			serlializer.serialize(obj, out);
			xmlString = out.toString();
			out.close();
		} catch (IOException e) {
			LOGGER.error("Error occurred printing VO => ", e);
		}

		return xmlString;
	}

	/**
	 * Sample input can be
	 * "/voMocks/riskholds/holdsandrelease/CreateS2FHoldTest/PurchaseContext.vo"
	 * 
	 * @param fileLocation
	 * @return
	 */
	public static final String printValueObjectFromFile(String fileLocation) {
		String output = null;
		try {
			ValueObject vo = VoHelper.loadVO(ValueObject.class, fileLocation);
			String xmlContent = VoHelper.printValueObject(vo);
			Document doc = new XMLHelper().convertToXmlDocument(xmlContent);
			output = new XMLHelper().getPrettyXml(doc);
		} catch (IOException | SAXException | ParserConfigurationException expt) {
			LOGGER.error("Error occurred printing VO from location {}", fileLocation, expt);
		}
		return output;
	}

	public static byte[] serializeValueObject(ValueObject vo, Formats format, boolean compress) throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		new UniversalSerializer(format, false, compress, false).serialize(vo, os);
		return os.toByteArray();
	}

}
