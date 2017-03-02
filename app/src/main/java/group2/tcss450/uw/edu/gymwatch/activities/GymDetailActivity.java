package group2.tcss450.uw.edu.gymwatch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import group2.tcss450.uw.edu.gymwatch.R;
import group2.tcss450.uw.edu.gymwatch.data.GymItem;

public class GymDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym_detail2);
        Intent i = getIntent();
        GymItem gym = (GymItem) i.getParcelableExtra("Gym");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //OpenSource Github API for multiline titles.
        net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout collapsingToolbarLayout = (net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setTitle(gym.getGymName());

        TextView gymAddress = (TextView) findViewById(R.id.gym_address_textView);
        gymAddress.setText(gym.getGymAddress());
        TextView gymFill = (TextView) findViewById(R.id.gym_fill_textView);
        gymFill.setText(gym.getGymFill());

        TextView gymPhone = (TextView) findViewById(R.id.gym_phone_textView);
        gymPhone.setText("425-614-6666");

        RatingBar gymRating = (RatingBar) findViewById(R.id.gym_rating_detail_view);
        gymRating.setRating(Integer.parseInt(gym.getGymFill()));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
}
