package com.uoc.datalevel;

import java.util.ArrayList;

/**
 * Created by Salva on 06/02/2016.
 */
public interface FindCallback<DataObject> {
    public void done(ArrayList<DataObject> objects, DataException e);
}
