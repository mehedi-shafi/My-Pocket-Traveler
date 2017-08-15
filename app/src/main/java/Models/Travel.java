package Models;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by shafi on 7/17/2017.
 */

public class Travel implements Serializable {

    private String title;
    private String location;
    private int duration;
    private String description;
    private ArrayList<String> tags;
    private String dateAdded;
    private int rating;
    private int databaseID;

    public Travel(Travel travel){
        this.title = travel.getTitle();
        this.location = travel.getLocation();
        this.duration = travel.getDuration();
        this.description = travel.getDescription();
        this.tags = travel.getTags();
        this.rating = travel.getRating();
        this.databaseID = travel.getDatabaseID();
        this.dateAdded = travel.getDateAdded();
    }

    public Travel (HashMap<String, Object> travel){
        this.title = travel.get("title").toString();
        this.location = travel.get("location").toString();
        this.duration = Integer.parseInt(travel.get("duration").toString());
        this.tags = (ArrayList<String>) travel.get("tags");
        this.description = travel.get("description").toString();
        this.rating = Integer.parseInt(travel.get("rating").toString());
        this.dateAdded = travel.get("dateAdded").toString();
    }

    public ArrayList<String> getTagsHash(HashMap<Integer, String> hash){
        ArrayList<String> ret = new ArrayList<>();
        for (Map.Entry<Integer, String> entry : hash.entrySet()){
            ret.add(entry.getValue());
        }
        return ret;
    }

    public Travel(){
        tags = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getDatabaseID() {
        return databaseID;
    }

    public void setDatabaseID(int databaseID) {
        this.databaseID = databaseID;
    }
}
