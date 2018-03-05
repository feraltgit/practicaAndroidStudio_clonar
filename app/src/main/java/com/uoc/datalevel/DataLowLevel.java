package com.uoc.datalevel;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

/**
 * Created by Salva on 11/12/2015.
 */
public class DataLowLevel {

    private static DataLowLevel instance = null;

    public ReentrantLock m_lock;
    public String mBasePath;
    public AssetManager m_assetManager;

    DataLowLevel()
    {

        m_lock = new ReentrantLock();

    }

    static public DataLowLevel Get()
    {
        return instance;
    }

    static public DataLowLevel Open(Context context)
    {
        if(instance==null){
            instance = new DataLowLevel();



            instance.mBasePath =  "" + context.getFilesDir();
            instance.m_assetManager = context.getAssets();

            if(!instance.Exists()){
                instance.Create();
            }

        }

        return instance;
    }

    public boolean Exists()
    {
        return ExistsFile(mBasePath + "/database.xml");
    }

    public void Create()
    {
        // Move first xml with your images
        CopyAssetDirToExternal(m_assetManager);
    }

    static public void Close()
    {


    }

    public void save(DataObject data)
    {

            if (m_lock.tryLock()) {
                try {
                    // manipulate protected state
                }
                catch(Exception e)
                {

                }
                finally {
                    m_lock.unlock();
                }
            }
            else {
                // perform alternative actions
            }

    }

    public String getStringXpath(String class_name, String property,Object value,int operator)
    {
        String str_xpath = "";

        if(operator == DataQuery.OPERATOR_EQUAL) {
            str_xpath = String.format("data/class[@name = '%s']/object/properties/property[@name='%s' and text()='%s']/../..", class_name, property, (String) value);
        }
        else if (operator == DataQuery.OPERATOR_ALL) {
            str_xpath = String.format("data/class[@name = '%s']/object", class_name);

        }
        else if (operator == DataQuery.OPERATOR_OBJECT_ID) {
            str_xpath = String.format("//object[@Id = '%s']", value);
        }



        return str_xpath;
    }

    public ArrayList<DataObject> find(String class_name, String property,Object value,int operator)
    {
        ArrayList<DataObject> result = new ArrayList<DataObject>();

        if (m_lock.tryLock()) {
            try {
                // manipulate protected state

                String xmlData = ReadAllText("database.xml");
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                try {
                    DocumentBuilder builder = factory.newDocumentBuilder();
                    Document doc = builder.parse(new InputSource(new StringReader(xmlData)));

                    XPathFactory factory_xpath=XPathFactory.newInstance();
                    XPath xPath=factory_xpath.newXPath();

                    XPathExpression xpath_xpression = xPath.compile(getStringXpath(class_name,property, value, operator));

                    NodeList list = (NodeList) xpath_xpression.evaluate(doc, XPathConstants.NODESET);
                    for(int index = 0; index < list.getLength(); index ++) {
                        Node node = list.item(index);
                        //String name = node.getNodeValue();
                        DataObject obj  = new DataObject("");
                        obj.m_objectId = node.getAttributes().getNamedItem("Id").getNodeValue();




                        XPathExpression xpath_xpression2 =  xPath.compile("./properties/property");

                        NodeList properties_list = (NodeList) xpath_xpression2.evaluate(node, XPathConstants.NODESET);


                        for(int i=0;i<properties_list.getLength();i++) {
                            Node pro_node = properties_list.item(i);
                            String pro_name =  pro_node.getAttributes().getNamedItem("name").getNodeValue();



                            if(pro_name.equals("image")){
                                String pro_value =  pro_node.getFirstChild().getNodeValue();

                                String pathName = mBasePath + "/images/" + pro_value;

                                Bitmap bitmap = BitmapFactory.decodeFile(pathName);


                                obj.put(pro_name,bitmap);

                            }
                            else{
                                String pro_value =  pro_node.getFirstChild().getNodeValue();
                                obj.put(pro_name,pro_value);
                            }



                        }

                        result.add(obj);
                    }

                }
                catch (Exception err)
                {


                }




            }
            catch(Exception e)
            {

            }
            finally {
                m_lock.unlock();
            }
        }
        else {
            // perform alternative actions
        }

        return result;
    }


    // ********************************************************************
    // File System

    public boolean ExistsFile(String path)
    {
        File tFile = new File(path);
        return tFile.exists();
    }


    public void copyFile(InputStream in, OutputStream out) throws IOException {
        //  byte[] buffer = new byte[1024];
        byte[] buffer = new byte[65535];
        int read;
        while((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    public void copyFileName(String in1, String out1,AssetManager assetManager){

        InputStream in = null;
        OutputStream out = null;

        try {
            in = assetManager.open(in1);
            out = new FileOutputStream(mBasePath + "/" + out1);
            copyFile(in, out);
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
        } catch(IOException e) {
            Log.e("ERROR", "Failed to copy asset file: " + in1, e);
        }


    }

    public  void CopyAssetDirToExternal(AssetManager assetManager) {

        String the_path = "";


        copyFileName("database.xml", the_path + "/" + "database.xml",assetManager);

        File folder = new File( mBasePath + "/images");
        folder.mkdir();



        copyFileName("images/" + "067e6162-3b6f-4ae2-a171-2470b63dff10.jpg", the_path + "/images/" + "067e6162-3b6f-4ae2-a171-2470b63dff10.jpg",assetManager);
        copyFileName("images/" + "067e6162-3b6f-4ae2-a171-2470b63dff11.jpg", the_path + "/images/" + "067e6162-3b6f-4ae2-a171-2470b63dff11.jpg",assetManager);
        copyFileName( "images/" + "067e6162-3b6f-4ae2-a171-2470b63dff12.jpg", the_path + "/images/" + "067e6162-3b6f-4ae2-a171-2470b63dff12.jpg",assetManager);


    }

    public  String ReadAllText(String filename)
    {
        String resul = "";

        File file = new File(mBasePath + "/" + filename);

        int length = (int) file.length();

        byte[] bytes = new byte[length];
        StringBuffer buffer = new StringBuffer();

        try {
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis, "UTF8");
            Reader in = new BufferedReader(isr);
            int ch;
            while ((ch = in.read()) > -1) {
                buffer.append((char)ch);
            }
            in.close();
            return buffer.toString();
        }
        catch(Exception err) {
            resul = "";
        }


        return resul;
    }

}
