package app.com.youtubeapiv3.activites;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import app.com.youtubeapiv3.R;
import app.com.youtubeapiv3.speechtotext.YoutubePlayerActivity.MainActivityService.SpeechRecognition;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout = null;
    private ViewPager viewPager = null;
    private SpeechRecognition speechRecognition;
    private app.com.youtubeapiv3.adapters.PagerAdapter pagerAdapter;
    private final int MY_PERMISSIONS_RECORD_AUDIO = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        //setting the tabs title
        tabLayout.addTab(tabLayout.newTab().setText("Search"));
        tabLayout.addTab(tabLayout.newTab().setText("MediaPlayer"));
        tabLayout.addTab(tabLayout.newTab().setText("Live"));

        this.tabLayout.getTabAt(0).setIcon(R.drawable.ic_search_red_24dp);
        this.tabLayout.getTabAt(1).setIcon(R.drawable.ic_playlist_play_black_24dp);
        this.tabLayout.getTabAt(2).setIcon(R.drawable.ic_live_tv_black_24dp);

        //setup the view pager
        pagerAdapter = new app.com.youtubeapiv3.adapters.PagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    MY_PERMISSIONS_RECORD_AUDIO);

        }

        this.speechRecognition = new SpeechRecognition(this);




    }

    public void searchModeDetected(String var1) {
        this.viewPager.setCurrentItem(0);
        pagerAdapter.getSearchFragment().search(var1);
    }


    protected void onPause() {
        this.speechRecognition.destroy();
        super.onPause();
    }

    protected void onResume() {
        this.speechRecognition = new SpeechRecognition(this);
        super.onResume();

    }


    public void changeTabDetected(String var1) {
        if (var1.equals("next") && this.viewPager.getCurrentItem() < 4) {
            this.viewPager.setCurrentItem(this.viewPager.getCurrentItem() + 1);
        }

        if (var1.equals("previous") && this.viewPager.getCurrentItem() > 0) {
            this.viewPager.setCurrentItem(this.viewPager.getCurrentItem() - 1);
        }

        if (var1.equals("playlist")) {
            this.viewPager.setCurrentItem(1);
        }

    }




}
