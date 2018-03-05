package com.uoc.pra1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.uoc.datalevel.DataException;
import com.uoc.datalevel.DataObject;
import com.uoc.datalevel.DataQuery;
import com.uoc.datalevel.FindCallback;

import java.util.ArrayList;

public class ResultsActivity extends AppCompatActivity implements  ListView.OnItemClickListener {

    private View mProgressView;
    private ListView mListView;



    public ResultListAdapter m_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        String user_email = getIntent().getStringExtra("user_email");

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("PR1 :: Results");

        mListView = (ListView) findViewById(R.id.listView);
        mProgressView = findViewById(R.id.progress);


        mListView.setOnItemClickListener(this);

        showProgress(true);

        // ************************************************************************
        // UOC - BEGIN - CODE3
        //
        DataQuery query = DataQuery.get("item");
        query.findInBackground("", "", DataQuery.OPERATOR_ALL, new FindCallback<DataObject>() {
            @Override
            public void done(ArrayList<DataObject> dataObjects, DataException e) {
                if (e == null) {
                    if (dataObjects.size() != 0) {
                        m_adapter = new ResultListAdapter(ResultsActivity.this, null);

                        m_adapter.m_array = dataObjects;
                        m_adapter.mActivity = ResultsActivity.this;

                        showProgress(false);
                        mListView.setAdapter(m_adapter);
                    }
                } else {
                    // Error

                }
            }
        });
        // UOC - END - CODE3
        // ************************************************************************


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {


        // ************************************************************************
        // UOC - BEGIN - CODE5
        //
        DataObject object = (DataObject) m_adapter.m_array.get(position);

        // Send m_objectId to DetailACtivity



        // UOC - END - CODE5
        // ************************************************************************




    }

    private void showProgress(final boolean show) {

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mListView.setVisibility(show ? View.GONE : View.VISIBLE);

    }



}
