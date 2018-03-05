package com.uoc.datalevel;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Salva on 06/02/2016.
 */
public class DataQuery<Object> {

    public String m_class_name;
    public ArrayList<DataObject> m_result;
    public FindCallback<DataObject> m_callback = null;
    public GetCallback<DataObject> m_callback_get  = null;
    public Runnable m_runnable;

    public int m_delay;

    public static final int OPERATOR_ALL = 100;
    public static final int OPERATOR_OBJECT_ID = 101;
    public static final int OPERATOR_LESS = -1;
    public static final int OPERATOR_EQUAL = 0;
    public static final int OPERATOR_GREAT = 1;

    public String m_property;
    public Object m_value;
    public int m_operator;

    static public DataQuery<DataObject> get(String class_name)
    {
        DataQuery<DataObject> result = new DataQuery<DataObject>();

        result.m_callback = null;
        result.m_callback_get = null;

        result.m_class_name = class_name;
        result.m_delay = 3*1000;

        return result;
    }


    public void getInBackground(String objectId, GetCallback<DataObject> callback) {
        m_callback_get = callback;

        m_value = (Object)objectId;
        m_runnable = new Runnable() {
            public void run() {

                try {


                    Thread.sleep(m_delay);

                    DataLowLevel lowLevel = DataLowLevel.Get();
                    m_result = lowLevel.find("", "", m_value, DataQuery.OPERATOR_OBJECT_ID);
                    Message msg = m_handler.obtainMessage();
                    m_handler.sendMessage(msg);
                }
                catch(Exception err)
                {


                }


            }
        };

        Thread mythread = new Thread(m_runnable);
        mythread.start();


    }

    public void findInBackground(String property,Object value,int operator, FindCallback<DataObject> callback)
    {
        m_callback = callback;
        m_property = property;
        m_operator = operator;
        m_value = value;

        Log.d("UOC-DEBUG",  String.format("Start calculation thread id: %d",Thread.currentThread().getId()));

        m_runnable = new Runnable() {
            public void run() {

                try {

                    Log.d("UOC-DEBUG",  String.format("body calculation thread id: %d",Thread.currentThread().getId()));

                        Thread.sleep(m_delay);

                    DataLowLevel lowLevel = DataLowLevel.Get();
                    m_result = lowLevel.find(m_class_name, m_property, m_value, m_operator);
                    Message msg = m_handler.obtainMessage();
                    m_handler.sendMessage(msg);
                }
                catch(Exception err)
                {


                }


            }
        };

        Thread mythread = new Thread(m_runnable);
        mythread.start();

    }

    public Handler m_handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {


            if(m_callback!=null){
                (m_callback).done(m_result, null);
            }
            else if(m_callback_get!=null) {
                (m_callback_get).done(m_result.get(0), null);
            }

            m_callback = null;
            m_callback_get = null;
        }

    };



    public ArrayList<DataObject> find(String property,Object value,int operator)
    {
        try {
            Log.d("UOC-DEBUG",  String.format("Start calculation thread id: %d",Thread.currentThread().getId()));
            Thread.sleep(m_delay);
            Log.d("UOC-DEBUG", String.format("body calculation thread id: %d", Thread.currentThread().getId()));
            DataLowLevel lowLevel = DataLowLevel.Get();
            m_result = lowLevel.find(m_class_name, property, value, operator);
        }
        catch(Exception err)
        {


        }
        return m_result;
    }

}
