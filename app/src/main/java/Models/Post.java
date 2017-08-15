package Models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

// [START post_class]
@IgnoreExtraProperties
public class Post implements Serializable {

    public String uid;
    public String author;
    public String title;
    public String body;
    public String  starCount = "0";
    public String location;

    public Post() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Post(HashMap<String, Object> data){
        this.starCount = data.get("starCount").toString();
        this.author =  data.get("author").toString();
        this.body = data.get("body").toString();
        this.uid = data.get("uid").toString();
        this.title = data.get("title").toString();
        this.location = data.get("location").toString();
    }

    public Post(String uid, String author, String title, String body, int StarCount, String location) {
        this.uid = uid;
        this.author = author;
        this.title = title;
        this.starCount = String.valueOf(StarCount);
        this.body = body;
        this.location = location;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getStarCount() {
        return starCount;
    }

    public void setStarCount(String starCount) {
        this.starCount = starCount;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
// [END post_class]
