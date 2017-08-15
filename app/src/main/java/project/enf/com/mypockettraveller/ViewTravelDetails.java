package project.enf.com.mypockettraveller;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import Database.DatabaseOpenHelper;
import Models.Travel;
import Utilities.UtilityFunctions;

public class ViewTravelDetails extends AppCompatActivity {

    private  TextView travelTitle, travelDate, travelTags, travelDetails, travelLocation, travelDuration, travelRatings;
    private UtilityFunctions uf = new UtilityFunctions();
    private Button deleteButton;
    private FloatingActionButton editButton;
    private Travel travelData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_travel_details);

        initiate();

        final Travel travel = (Travel) getIntent().getExtras().getSerializable("data");
        travelData = travel;

        travelTitle.setText(travel.getTitle());
        travelDate.setText(travel.getDateAdded());
        travelLocation.setText(travel.getLocation());
        travelTags.setText(uf.getTagsFromList(travel.getTags()));
        travelDuration.setText(String.valueOf(travel.getDuration()));
        travelDetails.setText(travel.getDescription());
        travelRatings.setText(uf.getStarBack(travel.getRating()));

        editButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent i = new Intent(ViewTravelDetails.this, EditTravel.class);

                Bundle mBundle = new Bundle();
                mBundle.putSerializable("data", travel);

                i.putExtras(mBundle);

               startActivity(i);

                ViewTravelDetails.this.finish();
            }
        });


    }

    public void initiate(){
        travelTitle = (TextView) findViewById(R.id.travelDetailsTitle);
        travelDate = (TextView) findViewById(R.id.travelDetailsDate);
        travelLocation = (TextView) findViewById(R.id.travelDetailsLocation);
        travelTags = (TextView) findViewById(R.id.travelDetailsTags);
        travelDetails = (TextView) findViewById(R.id.travelDetailsDescription);
        travelDuration = (TextView) findViewById(R.id.travelDetailsDuration);
        travelRatings = (TextView) findViewById(R.id.travelDetailsRating);
        editButton = (FloatingActionButton) findViewById(R.id.travelDetailsEditButton);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.deleteMenu) {
            DatabaseOpenHelper dbh= new DatabaseOpenHelper(getApplicationContext());
            dbh.deleteTravel(travelData);
            Intent i = new Intent(ViewTravelDetails.this, MainActivity.class);
            Toast.makeText(this, "Travel has been removed", Toast.LENGTH_SHORT).show();
            startActivity(i);
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
