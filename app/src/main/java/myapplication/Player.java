package myapplication;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;



import java.io.File;
import java.util.ArrayList;

import app.com.youtubeapiv3.R;

public class Player extends AppCompatActivity {

    static Button playBtn;
    Button nextBtn;
    Button prevBtn;
    Uri uri;
    static MediaPlayer mediaPlayer;
    ArrayList<File> songs;
    Thread timeUpdater;
    static int songTime=0;
    static int currentTime=0;
    int position;
    static boolean flag=false;


    SeekBar positionBar;
    TextView elapsedTime;
    TextView remainingTime;
    TextView songtitleView;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_layout);

        nextBtn=(Button) findViewById(R.id.nextBtn);
        prevBtn=(Button) findViewById(R.id.prevBtn);
        playBtn=(Button) findViewById(R.id.playBtn);


        positionBar = (SeekBar) findViewById(R.id.seekBar);
        elapsedTime = (TextView) findViewById(R.id.elapsedTimeLabel);
        remainingTime = (TextView) findViewById(R.id.remainingTimeLabel);
        songtitleView = (TextView) findViewById(R.id.songTitleView);

        //Context context = getApplicationContext();
        //timeUpdater.start();


        timeUpdater = new Thread(){
            @Override
            public void run(){


                while(true){
                      songTime = mediaPlayer.getDuration();
                      currentTime = 0;
                      positionBar.setMax(songTime);

                      while(currentTime<songTime){
                          String value = getIntent().getExtras().getString("msg");


                          Log.d("TAG", "run: tag bo toast nie działa ___ "+value);
                          if(value!=null){
                              if (value.toLowerCase().contains("next")){
                                  next();
                              }
                          }


                           try{
                               //MainActivity.
                                //songtitleView.setText(createTitle());
                                 sleep(300);
                                currentTime=mediaPlayer.getCurrentPosition();
                                songTime = mediaPlayer.getDuration();
                                Message msg = new Message();
                                msg.what = mediaPlayer.getCurrentPosition();
                                handler.sendMessage(msg);
                           }catch (Exception e){
                           }
                        }

                        mediaPlayer.stop();
                        //mediaPlayer.release();
                        currentTime=0;
                        if (flag){
                            position--;
                            flag=false;
                        }
                        else{
                            position++;
                        }
                        if (position>= songs.size()){
                            position=0;
                        }
                        if(position<0){
                            position=songs.size()-1;
                        }

                        uri = Uri.parse(songs.get(position).toString());
                        mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
                        songTime = mediaPlayer.getDuration();
                        Message message = new Message();
                        //message.what = createTitle();
                        tittleHandler.sendMessage(message);
                        //songtitleView.setText(createTitle());
                        mediaPlayer.start();

                    }
            }



        };

        //timeUpdater.start();

        if (mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        songs =(ArrayList) bundle.getParcelableArrayList("songs");
        position = bundle.getInt("pos",0);
        uri= Uri.parse(songs.get(position).toString());

        songtitleView.setText(createTitle());

        mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
        //mediaPlayer.start();
        timeUpdater.start();
        playBtn.setBackgroundResource(R.drawable.pause);
        mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
        mediaPlayer.seekTo(currentTime);
        mediaPlayer.start();

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer==null){
                    playBtn.setBackgroundResource(R.drawable.pause);
                    mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
                    mediaPlayer.seekTo(currentTime);
                    mediaPlayer.start();
                }else if (!mediaPlayer.isPlaying()){
                    playBtn.setBackgroundResource(R.drawable.pause);
                    mediaPlayer.seekTo(currentTime);
                    mediaPlayer.start();

                }
                else{
                    mediaPlayer.pause();
                    playBtn.setBackgroundResource(R.drawable.play_arrow);
                    currentTime=mediaPlayer.getCurrentPosition();
                }

            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next();
            }
        });





        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                currentTime=0;
                position--;
                if (position<0){
                    position= songs.size()-1;
                }
                songtitleView.setText(createTitle());
                uri = Uri.parse(songs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
                songTime = mediaPlayer.getDuration();

                mediaPlayer.start();
            }
        });

        positionBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if(fromUser)
                        {
                            mediaPlayer.seekTo(progress);
                            positionBar.setProgress(progress);
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                }
        );

    }



    public void next(){
        mediaPlayer.stop();
        mediaPlayer.release();
        currentTime=0;
        position++;
        if (position>= songs.size()){
            position=0;
        }
        uri = Uri.parse(songs.get(position).toString());
        mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
        songTime = mediaPlayer.getDuration();
        songtitleView.setText(createTitle());
        mediaPlayer.start();
    }

    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            int currentPosition = msg.what;
            positionBar.setProgress(currentPosition);

            String elapsedTimeString = createTimeLabel(currentPosition);
            elapsedTime.setText(elapsedTimeString);

            String remainingTimeString = createTimeLabel(mediaPlayer.getDuration()-currentPosition);
            remainingTime.setText(remainingTimeString);
        }

    };

    private Handler tittleHandler = new Handler(){
        @Override
        public  void handleMessage (Message msg){
            songtitleView.setText(createTitle());
        }
    };

    public String createTimeLabel(int time)
    {
        String timeLabel;
        int min = time/1000/60;
        int sec = time/1000%60;

        timeLabel = min + ":";
        if(sec<10)
            timeLabel = timeLabel + "0" + sec;
        else
            timeLabel = timeLabel + sec;

        return timeLabel;
    }

    public String createTitle()
    {
        String[] arr = songs.get(position).toString().split("/");
        String[] arr2 = arr[arr.length-1].split("\\.");
        return arr2[0];
        //return (arr[arr.length-1].split(""))[0];
    }

    public static MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }



    public static int getSongTime() {
        return Player.songTime;

    }



    public static int getCurrentTime() {
        return currentTime;
    }
}


