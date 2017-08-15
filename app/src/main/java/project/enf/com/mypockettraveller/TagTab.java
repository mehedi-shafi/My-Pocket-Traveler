package project.enf.com.mypockettraveller;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import Adapters.TagListAdapter;
import Database.DatabaseOpenHelper;

/**
 * Created by shafi on 7/17/2017.
 */

public class TagTab extends ListFragment {

    private DatabaseOpenHelper dbs ;
    private TagListAdapter tagAdapter;
    ListView tagList ;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tag_tab, container, false);

        dbs = new DatabaseOpenHelper(container.getContext());
        ArrayList<String> tags = dbs.getAllTags();


        tagAdapter = new TagListAdapter(getActivity(), R.layout.tag_row, tags);
        setListAdapter(tagAdapter);

        tagAdapter.notifyDataSetChanged();

        return rootView;
    }
}
