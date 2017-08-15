package project.enf.com.mypockettraveller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

import Models.Post;

public class ActivityPostDetail extends AppCompatActivity {

    TextView title, location , description, rating, poster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);


        title = (TextView) findViewById(R.id.detail_post_title);
        poster = (TextView) findViewById(R.id.detail_poster);
        location = (TextView) findViewById(R.id.location_detail);
        rating = (TextView) findViewById(R.id.rating_detail);
        description = (TextView) findViewById(R.id.detail_desription);


        Post post = (Post) getIntent().getExtras().getSerializable("data");

        title.setText(post.getTitle());
        description.setText(post.getBody());
        rating.setText(R.string.user_rated_text + " "  + post.getStarCount() + " stars");
        location.setText(post.getLocation());
        poster.setText(post.getAuthor());

    }
}
