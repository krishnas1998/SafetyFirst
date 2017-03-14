package com.vikas.dtu.safetyfirst2.mDiscussion;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vikas.dtu.safetyfirst2.BaseActivity;
import com.vikas.dtu.safetyfirst2.CategoryAdapter;
import com.vikas.dtu.safetyfirst2.NotificationService;
import com.vikas.dtu.safetyfirst2.R;
import com.vikas.dtu.safetyfirst2.mData.User;
import com.vikas.dtu.safetyfirst2.mSignUp.SignInActivity;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

import static com.vikas.dtu.safetyfirst2.mUtils.FirebaseUtil.getCurrentUserId;

public class DiscussionActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "DiscussionActivity";
    private ProgressBar progress;
    private TabItem tabItem1, tabItem2, tabItem3, tabItem4;
    private static final String SHOWCASE_ID = "sequence example";

    private  CategoryAdapter mAdapter;
    private static ViewPager mViewPager; // static so that it can be changed within fragments

    private final int[] tabIcons =  {

            R.drawable.disc_forum_1_ic_forum_white_24dp,
            R.drawable.disc_forum_2_ic_home_white_24dp,
            R.drawable.disc_forum_3_ic_account_circle_white_24dp,
            R.drawable.disc_forum_4_ic_edit_white_24dp};
/*
            R.drawable.ic_forum_white_24dp,
            R.drawable.ic_help_white_24dp,
            R.drawable.ic_account_circle_white_24dp,
            R.drawable.ic_border_color_black_24dp};*/

    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.discussion_view_pager);

        // starting notification service
        Intent serviceIntent = new Intent(this,NotificationService.class);
        startService(serviceIntent);
        tabItem1 = (TabItem)findViewById(R.id.discussions);
       // tabItem1.setOnClickListener(this);
        tabItem2 = (TabItem)findViewById(R.id.myposts);
        //tabItem2.setOnClickListener(this);
        tabItem3 = (TabItem)findViewById(R.id.bookmarked);
       // tabItem3.setOnClickListener(this);
        tabItem4 = (TabItem)findViewById(R.id.write_que);
       // tabItem4.setOnClickListener(this);





        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up round_blue_dark
        ab.setDisplayHomeAsUpEnabled(true);

        // Create the adapter that will return a fragment for each section
       /* mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            private final Fragment[] mFragments = new Fragment[]{
                    new RecentPostsFragment(),
                    new MyPostsFragment(),
            };
            private final String[] mFragmentNames = new String[]{
                    getString(R.string.heading_recent),
                    getString(R.string.heading_my_posts)
            };

            @Override
            public Fragment getItem(int position) {
                return mFragments[position];
            }

            @Override
            public int getCount() {
                return mFragments.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mFragmentNames[position];
            }
        };
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        // Button launches NewPostActivity
        findViewById(R.id.fab_new_post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DiscussionActivity.this, NewPostActivity.class));
            }
        }); */
        progress = (ProgressBar) findViewById(R.id.progressBar);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        DatabaseReference mUserRef= FirebaseDatabase.getInstance().getReference().child("users").child(getCurrentUserId());
        mUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                mAdapter = new CategoryAdapter(DiscussionActivity.this ,getSupportFragmentManager(), user);
                mViewPager.setAdapter(mAdapter);
                tabLayout = (TabLayout)findViewById(R.id.tabs);
                tabLayout.setOnClickListener(DiscussionActivity.this);
                tabLayout.setupWithViewPager(mViewPager);
                setupTabIcons();
                presentShowcaseView(1000); // one second delay
                progress.setVisibility(View.GONE);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(DiscussionActivity.this, "Error Loading User", Toast.LENGTH_LONG).show();
            }
        });
    }
    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        final MenuItem searchPost = menu.findItem(R.id.search_post);
        SearchView searchView = null;
        if (searchPost != null) {
            searchView = (SearchView) MenuItemCompat.getActionView(searchPost);
        }
        EditText searchPlate = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchPlate.setHint("Search Posts");
        View searchPlateView = searchView.findViewById(android.support.v7.appcompat.R.id.search_plate);
        searchPlateView.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent searchIntent = new Intent(getApplicationContext(),SearchActivity.class);
                searchIntent.putExtra("search_query",query);
                startActivity(searchIntent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // use this method for auto complete search process
                return false;
            }
        });

        //
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        switch (i) {
            case R.id.action_logout:
                FirebaseAuth.getInstance().signOut();
                View view = findViewById(R.id.action_logout);
                /*new MaterialShowcaseView.Builder(this)
                        .setTarget(view)
                        .setShapePadding(96)
                        .setDismissText("GOT IT")
                        .setContentText("Example of how to setup a MaterialShowcaseView for menu items in action bar.")
                        .setContentTextColor(getResources().getColor(R.color.green))
                        .setMaskColour(getResources().getColor(R.color.dark_blue))
                        .show();*/
                startActivity(new Intent(this, SignInActivity.class));
                finish();
                return true;
            case R.id.search_post:

                //  Toast.makeText(NewsActivity.this, "Just write important TAG/Word of your Question", Toast.LENGTH_LONG).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public static ViewPager getViewPager(){
        return mViewPager;
    }


    @Override
    public void onClick(View v) {

        presentShowcaseView(0);

    }
    private void presentShowcaseView(int withDelay) {

        new MaterialShowcaseView.Builder(this)
                .setTarget(tabLayout)
                .setContentText("This is Discussion Section \n1. Main Discussion Section\n2. Your Posts \n3. Profile \n4. Write new Post")
                .setFadeDuration(1000)
                .setDismissOnTouch(true)
                .setContentTextColor(getResources().getColor(R.color.white))
                .setMaskColour(getResources().getColor(R.color.dark_blue))
                .setDelay(withDelay) // optional but starting animations immediately in onCreate can make them choppy
                .singleUse(SHOWCASE_ID) // provide a unique ID used to ensure it is only shown once
                .show();
       /* ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(500); // half second between each showcase view

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, SHOWCASE_ID);

        sequence.setOnItemShownListener(new MaterialShowcaseSequence.OnSequenceItemShownListener() {
            @Override
            public void onShow(MaterialShowcaseView itemView, int position) {
                Toast.makeText(itemView.getContext(), "Item #" + position, Toast.LENGTH_SHORT).show();
            }
        });

        sequence.setConfig(config);

        sequence.addSequenceItem(tabItem1, "This is button one", "GOT IT");

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(tabItem2)
                        .setDismissText("GOT IT")
                        .setContentText("This is button two")
                        .withRectangleShape(true)
                        .build()
        );

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(tabItem3)
                        .setDismissText("GOT IT")
                        .setContentText("This is button three")
                        .withRectangleShape()
                        .build()
        );
        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(tabItem4)
                        .setDismissText("GOT IT")
                        .setContentText("This is button three")
                        .withRectangleShape()
                        .build()
        );
        sequence.start();*/

    }

}