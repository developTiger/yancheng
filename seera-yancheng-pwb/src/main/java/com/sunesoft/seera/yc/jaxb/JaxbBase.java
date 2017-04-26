package com.sunesoft.seera.yc.jaxb;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;

/**
 * Created by bing on 16/8/4.
 */
public abstract class JaxbBase implements Serializable {

    protected Class<?> objectClass;

    public JaxbBase(Class<?> objectClass) {
        this.objectClass = objectClass;
    }

    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public String toXml() {
        try {
            JAXBContext context = JAXBContext.newInstance(objectClass);

            Marshaller marshaller = context.createMarshaller();
//            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");// //编码格式
//            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);// 是否格式化生成的xml串
//            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, false);// 是否省略xm头声明信息
            StringWriter writer = new StringWriter();
            marshaller.marshal(this, writer);
            return writer.toString();

        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public String toString() {
        return toXml();
    }

    public static <T> T toObject(String xml, Class<T> jaxbBaseClass) throws JAXBException {
        if (null == xml || "".equals(xml)) return null;
        JAXBContext jaxbContext = JAXBContext.newInstance(jaxbBaseClass);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
//        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xml.getBytes());
        StringReader sr = new StringReader(xml);
        return  (T) unmarshaller.unmarshal(sr);
    }

    public static String getUTF8XMLString(String xml) {
        // A StringBuffer Object
        StringBuffer sb = new StringBuffer();
        sb.append(xml);
        String xmString = "";
        String xmlUTF8="";
        try {
                xmString = new String(sb.toString().getBytes("UTF-8"));
            xmlUTF8 = URLEncoder.encode(xmString, "UTF-8");
            System.out.println("utf-8 编码：" + xmlUTF8) ;
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // return to String Formed
        return xmlUTF8;
    }
}
