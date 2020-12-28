package org.yokekhei.examples.xsdjava;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import iso.std.iso._20022.tech.xsd.head_001_001.AppHdr;

public class AppTest extends TestCase {
	private static Calendar calendar;
	private static SimpleDateFormat sdfDtTm;
	private static SimpleDateFormat sdfDt;
	
	private ClassLoader classLoader;
	
	public AppTest(String testName) {
        super(testName);
        calendar = Calendar.getInstance();
		calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
		sdfDtTm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sdfDtTm.setTimeZone(TimeZone.getTimeZone("UTC"));
		sdfDt = new SimpleDateFormat("yyyy-MM-dd");
		sdfDt.setTimeZone(TimeZone.getTimeZone("UTC"));
    }
	
    public static Test suite() {
    	return new TestSuite(AppTest.class);
    }
    
    public void testAppHdr() {
		AppHdr header = null;
    	
		try {
			classLoader = getClass().getClassLoader();
			JAXBContext jaxbContext = JAXBContext.newInstance(AppHdr.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			FileReader reader = new FileReader(classLoader.getResource("AppHdr.xml").getFile());
			header = (AppHdr) unmarshaller.unmarshal(reader);
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
        assertNotNull(header);
        assertEquals("BMSCMYK1", header.getFr().getFIId().getFinInstnId().getBICFI());
        assertEquals("Corporate Announcement Subscribers", header.getTo().getFIId().getFinInstnId().getNm());
        assertEquals("MY190911X0000018", header.getBizMsgIdr());
        assertEquals("seev.031.001.08", header.getMsgDefIdr());
        assertEquals("Corporate Announcements", header.getBizSvc());
        
        try {
        	Date dt = sdfDtTm.parse("2019-09-11 11:56:51");
			calendar.setTime(dt);
		} catch (ParseException e) {
			fail("Unexpected time format parse exception");
		}
        
        assertEquals(calendar.getTimeInMillis(), header.getCreDt().getTimeInMillis());
        assertEquals("DUPL", header.getCpyDplct());
        assertTrue(header.isPssblDplct());
    }
	
}
