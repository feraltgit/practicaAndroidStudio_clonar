package com.uoc.datalevel;

import java.util.Hashtable;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Salva on 11/12/2015.
 */
public class DataObject {

    public String m_objectId;
    public String m_class_name;
    public Map<String,Object> m_properties;


    DataObject(String class_name)
    {
        m_class_name = class_name;
        m_properties = new Hashtable<String,Object>();

        m_objectId = UUID.randomUUID().toString();
    }


    // Properties

    public void put(String key,Object value)
    {

        m_properties.put(key,value);
    }

    public Object get(String key)
    {
        return m_properties.get(key);
    }

    // *******************************************************************
    // Save
    public void save()
    {
        DataLowLevel lowLevel = DataLowLevel.Get();
        lowLevel.save(this);

    }

    public void saveInBackground()
    {


    }

}
