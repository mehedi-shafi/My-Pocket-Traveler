package Database;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Models.Post;
import Models.Travel;

/**
 * Created by shafi on 7/21/2017.
 */

public class FirebaseHandler {

    private DatabaseReference mdb = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = mAuth.getCurrentUser();
    private DatabaseOpenHelper dbh;

    public FirebaseHandler(){

    }

    public FirebaseHandler(DatabaseOpenHelper dbh){
        this.dbh = dbh;
        Query users = mdb.child("users");
        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    syncho(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void addOnline (Travel travel){
        if (currentUser != null) {
            try {
                mdb.child("users").child(currentUser.getUid().toString()).child(String.valueOf(travel.getDatabaseID())).setValue(travel);
                String key = mdb.child("posts").push().getKey();
                Post post = new Post(FirebaseAuth.getInstance().getCurrentUser().getUid(), FirebaseAuth.getInstance().getCurrentUser().getDisplayName(), travel.getTitle(), travel.getDescription(), travel.getRating(), travel.getLocation());
                mdb.child("posts").child(key).setValue(post);
            } catch (Exception E) {
                Log.v("exception. ", "Firebase handler");
            }
        }
    }

    public ArrayList<Travel> syncho(DataSnapshot dataSnapshot){
        ArrayList<Travel> fireBaseList = new ArrayList<>();
            System.out.println(dataSnapshot.getChildrenCount());
            DataSnapshot thisUser = dataSnapshot.child(currentUser.toString());
            for (DataSnapshot thisUserData : thisUser.getChildren()){
                Travel travel = thisUserData.getValue(Travel.class);
                fireBaseList.add(travel);
            }
//
        return fireBaseList;
    }

    public void getAllTravels(){

    }


}
