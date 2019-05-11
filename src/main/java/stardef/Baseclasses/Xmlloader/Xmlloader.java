package stardef.Baseclasses.Xmlloader;


import stardef.Baseclasses.Empire;
import stardef.Baseclasses.Empires;
import stardef.Baseclasses.Spaceship;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.JAXBException;

        import java.io.StringWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.File;

public class Xmlloader
{
    public static String jaxbObjectToXMLsingle(Empire OBJ) {
        String xmlString = "";
        try {
            JAXBContext context = JAXBContext.newInstance(OBJ.getClass());
            Marshaller m = context.createMarshaller();
    
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE); // To format XML
            
            StringWriter sw = new StringWriter();
            m.marshal(OBJ, sw);
            xmlString = sw.toString();
    
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    
        return xmlString;
    }
    public static String jaxbObjectToXML(Object OBJ) {
        String xmlString = "";
        try {
            JAXBContext context = JAXBContext.newInstance(OBJ.getClass());
            Marshaller m = context.createMarshaller();
    
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE); // To format XML
            
            StringWriter sw = new StringWriter();
            m.marshal(OBJ, sw);
            xmlString = sw.toString();
    
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    
        return xmlString;
    }

    public static void jaxbObjectToXMLfile(Object obj,String path)
    {
    BufferedWriter writer = null;
    String str=Xmlloader.jaxbObjectToXML(obj);
    String xmlpath="src/main/resources/Xmlresources/";
    try
    {
        writer = new BufferedWriter( new FileWriter(xmlpath+path));
        writer.write( str);
    }
    catch ( IOException exp) {   }
    finally
    {
        try
        {
            if ( writer != null)
            writer.close( );
        }
        catch ( IOException exp) {   }
    }
    }

    public static Empires XMLfileToEmpires(String path)
    {
        String xmlpath="src/main/resources/Xmlresources/";
        Empires emp=new Empires();
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Empires.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            emp = (Empires) jaxbUnmarshaller.unmarshal(new File(xmlpath+path));
            }
        catch(JAXBException e){e.printStackTrace();}
        return emp;
    }
    public static Spaceship XMLfileToShipdata(String path)
    {
        String xmlpath="src/main/resources/Xmlresources/";
        Spaceship ss=new Spaceship();
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Spaceship.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            ss = (Spaceship) jaxbUnmarshaller.unmarshal(new File(xmlpath+path));
        }
        catch(JAXBException e){e.printStackTrace();}
        return ss;
    }


}