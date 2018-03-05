package com.uoc.pra1;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.uoc.datalevel.DataObject;

import java.util.ArrayList;


/**
 * Created by Salva on 12/12/2015.
 */
public class ResultListAdapter extends BaseAdapter {

    private Context mContext;
    Cursor cursor;
    public Activity mActivity;
   // public ArrayList<ModelTool> m_array;
   public ArrayList<DataObject> m_array;


    public ResultListAdapter(Context context,Cursor cur)
    {
        super();
        mContext=context;
        cursor=cur;

    }

    public int getCount()
    {
        // return the number of records in cursor
        // return cursor.getCount();

        return  m_array.size();
    }

    public View getView(int position,  View view, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        RelativeLayout vp = (RelativeLayout)inflater.inflate(R.layout.activity_results_row, null);


        DataObject object = m_array.get(position);

        TextView title = (TextView)vp.findViewById(R.id.title);
        title.setText((String) object.get("name"));


        ImageView  thumbnail=  (ImageView)vp.findViewById(R.id.thumbnail);
        thumbnail.setImageBitmap((Bitmap) object.get("image"));


        // ************************************************************************
        // UOC - BEGIN - CODE4
        //



        // UOC - END - CODE4
        // ************************************************************************


        view = vp;


        return view;
    }

    public Object getItem(int position) {

        return position;
    }

    public long getItemId(int position) {

        return position;
    }
}
