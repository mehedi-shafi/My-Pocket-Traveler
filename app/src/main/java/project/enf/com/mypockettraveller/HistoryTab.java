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

import java.util.ArrayList;

import Adapters.TravelListAdapter;
import Database.DatabaseOpenHelper;
import Models.Travel;

/**
 * Created by shafi on 7/17/2017.
 */

public class HistoryTab extends ListFragment {

    private DatabaseOpenHelper dbs;
    private ListView visitList;
    private TravelListAdapter travelAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.history_tab, container, false);

        dbs = new DatabaseOpenHelper(container.getContext());
        ArrayList<Travel> travels = dbs.getAllTravels();
        dbs.close();

        travelAdapter = new TravelListAdapter(getActivity(), R.layout.history_row, travels);

        setListAdapter(travelAdapter);

        travelAdapter.notifyDataSetChanged();
        return rootView;
    }

}
