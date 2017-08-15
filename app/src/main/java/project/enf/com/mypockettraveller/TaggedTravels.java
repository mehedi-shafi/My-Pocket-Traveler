package project.enf.com.mypockettraveller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

import Adapters.TravelListAdapter;
import Database.DatabaseOpenHelper;
import Models.Travel;

public class TaggedTravels extends AppCompatActivity {

    private DatabaseOpenHelper dbs;
    private ListView visitList;
    private TravelListAdapter travelAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tagged_travels);

        dbs = new DatabaseOpenHelper(getApplicationContext());
        visitList = (ListView) findViewById(R.id.taggedList);

        ArrayList<Travel> data = dbs.getTaggedTravels(getIntent().getStringExtra("tag_tag"));
        System.out.println("Total tagged data fetched = " + data.size());
        travelAdapter = new TravelListAdapter(this, R.layout.history_row, data);

        visitList.setAdapter(travelAdapter);

        travelAdapter.notifyDataSetChanged();

    }
}
