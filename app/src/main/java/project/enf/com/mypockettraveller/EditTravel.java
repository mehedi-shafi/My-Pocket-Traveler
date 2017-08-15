package project.enf.com.mypockettraveller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import Database.DatabaseOpenHelper;
import Models.Travel;
import Utilities.UtilityFunctions;

public class EditTravel extends AppCompatActivity {

    private EditText location, title, description, duration , tags;
    private RatingBar rating;
    private Button submit;
    private DatabaseOpenHelper dba;
    private Button placePickerButton;
    private static final int PLACE_PICKER_REQUEST = 1;
    private Travel travel;

    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(23.727677, 90.410553), new LatLng(23.727677, 90.410553));
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_travel);

        this.travel = (Travel) getIntent().getExtras().getSerializable("data");

        initialize(travel);
        submit.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
//
//                FirebaseHandler fb = new FirebaseHandler();
//                fb.addOnline(createTravel(), "test");
                Travel updatedTravel = createTravel();
               updateDatabase(updatedTravel);
                Toast.makeText(EditTravel.this, "updated  database successfully", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(EditTravel.this, ViewTravelDetails.class);

                Bundle mBundle = new Bundle();
                mBundle.putSerializable("data", updatedTravel);
                i.putExtras(mBundle);

                startActivity(i);
                EditTravel.this.finish();
            }
        });

        placePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    PlacePicker.IntentBuilder intentBuilder =  new PlacePicker.IntentBuilder();
                    intentBuilder.setLatLngBounds(BOUNDS_MOUNTAIN_VIEW);
                    startActivityForResult(intentBuilder.build(EditTravel.this), PLACE_PICKER_REQUEST);

                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void initialize(Travel travel){
        location = (EditText) findViewById(R.id.inputLocationInput);
        title = (EditText) findViewById(R.id.locationTitleInput);
        description = (EditText) findViewById(R.id.inputDescriptionDescription);
        duration = (EditText) findViewById(R.id.inputDurationDuration);
        tags = (EditText) findViewById(R.id.inputTagInput);
        rating = (RatingBar) findViewById(R.id.inputRatingRating);
        submit = (Button) findViewById(R.id.inputSubmitButton);
        placePickerButton = (Button) findViewById(R.id.inputPlacePickerOpenButton);


        //putting data on places.
        location.setText(travel.getLocation());
        title.setText(travel.getTitle());
        description.setText(travel.getDescription());
        duration.setText(String.valueOf(travel.getDuration()));
        tags.setText(new UtilityFunctions().getTagsFromList(travel.getTags()));
        rating.setNumStars(travel.getRating());
        tags.setEnabled(false);
    }

    public void updateDatabase(Travel tTravel){
        dba = new DatabaseOpenHelper(getApplicationContext());
        dba.updateTravel(tTravel);
    }

    public Travel createTravel(){
        UtilityFunctions utility = new UtilityFunctions();
        Travel travel = new Travel();
        travel.setDatabaseID(travel.getDatabaseID());
        travel.setTitle(title.getText().toString().replace("'", ""));
        int ratingInt = (int) rating.getRating();
        travel.setRating(ratingInt);
        travel.setDuration(utility.properRatingGet(duration.getText().toString().replace("'", "")));
        travel.setLocation(location.getText().toString().replace("'", ""));
        travel.setDescription(description.getText().toString().replace("'", ""));

        //creating current date well formatted string
        DateFormat formatDate = new SimpleDateFormat("dd/MM/yyy");
        Date today = new Date();
        String dateAdded = formatDate.format(today).toString();
        travel.setDateAdded(dateAdded);

        //get tags
        travel.setTags(utility.tagSplitter(tags.getText().toString()));

        return travel;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PLACE_PICKER_REQUEST && resultCode == RESULT_OK){
            final Place place = PlacePicker.getPlace(this, data);
            final String name = place.getName().toString();
            final String address = place.getAddress().toString();

            String attribution = (String) place.getAttributions();

            if (attribution == null){
                attribution = "";
            }
            if (address == null){
                Toast.makeText(this, "no address came.", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, address, Toast.LENGTH_SHORT).show();
            }
            location.setText(address);
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
