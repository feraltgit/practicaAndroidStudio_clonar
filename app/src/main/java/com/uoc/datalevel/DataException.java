package com.uoc.datalevel;

/**
 * Created by Salva on 06/02/2016.
 */
public class DataException extends Exception
{
    public DataException() { super(); }
    public DataException(String message) { super(message); }
    public DataException(String message, Throwable cause) { super(message, cause); }
    public DataException(Throwable cause) { super(cause); }
}
