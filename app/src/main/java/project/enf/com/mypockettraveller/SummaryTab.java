package project.enf.com.mypockettraveller;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import Adapters.LookBackAdapter;
import Database.DatabaseOpenHelper;

/**
 * Created by shafi on 7/17/2017.
 */

public class SummaryTab extends ListFragment {

    private TextView dateText, timeText, travelCountText;
    private LookBackAdapter lookkBackAdapter;
    private DatabaseOpenHelper dbs;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.summary_tab, container, false);
        dateText = (TextView) rootView.findViewById(R.id.summaryDateDateText);
        timeText = (TextView)rootView.findViewById(R.id.summaryDateTimeText);
        travelCountText = (TextView) rootView.findViewById(R.id.summaryPlaceVIsitedCountText);

        dbs = new DatabaseOpenHelper(getActivity().getApplicationContext());

        //setting up date
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date today = new Date();
        String date = String.valueOf(dateFormat.format(today));
        Log.v("date: ", String.valueOf(dateFormat.format(today)));
        if (dateText == null) Log.v("date Textview: " , "not initialized.");
        dateText.setText(date);

        //setting up time
        DateFormat timeFormat = new SimpleDateFormat("h:mm a");
        timeText.setText(String.valueOf(timeFormat.format(today)));
        if (dateText == null) Log.v("time Textview: " , "not initialized.");

        travelCountText.setText(String.valueOf(dbs.getTotalTravelsNumber()));

        lookkBackAdapter = new LookBackAdapter(getActivity(), R.layout.look_back_row, dbs.getLookbackStories());
        setListAdapter(lookkBackAdapter);

        lookkBackAdapter.notifyDataSetChanged();

        dbs.close();

        return rootView;
    }

}
