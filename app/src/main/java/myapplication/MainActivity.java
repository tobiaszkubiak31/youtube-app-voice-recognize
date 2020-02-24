package myapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Environment;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


import java.io.File;
import java.util.ArrayList;

import app.com.youtubeapiv3.R;


public class MainActivity extends AppCompatActivity {


    ListView listView;
    String[] items;
    private SpeechRecognizer speachRecognizer;
    private static SpeachRecognitionListener speachRecognitionListener;
    private Commands commands = new Commands();
    private Intent recognizerIntent;
    private AudioManager audioManager;
    final ArrayList<File> songs = findSong(Environment.getExternalStorageDirectory());
    private static final String tag = MainActivity.class.getSimpleName();
    Intent intent;
    public ArrayList<File> findSong(File file){

        ArrayList<File> arrayList = new ArrayList<>();

        File[] files = file.listFiles();
        for (File singeFile: files){
            if ( (singeFile.isDirectory()) && (!singeFile.isHidden()) ){
                arrayList.addAll(findSong(singeFile));
            }else{
                if( (singeFile.getName().endsWith(".mp3")) || (singeFile.getName().endsWith(".wav"))   ){
                    arrayList.add(singeFile);
                }
            }
        }

        arrayList.sort(new FileComparator());
        return arrayList;
    }

    void display(){
        final ArrayList<File> songs = findSong(Environment.getExternalStorageDirectory());
        items = new String[songs.size()];
        for (int i =0; i<songs.size();i++){
            items[i]=songs.get(i).getName().toString().replace(".mp3","");
            items[i]=items[i].replace(".wav","");
            commands.addSong(items[i]);
        }


        //Arrays.sort(items);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<>(this, R.layout.mytextview,items);

        listView.setAdapter(myAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent = new Intent(MainActivity.this,Player.class).putExtra("songs",songs).putExtra("songname",listView.getItemAtPosition(position).toString()).putExtra("pos",position);
                startActivity(intent);
            }
        });

    }


    void speachOnCreate(){
        speachRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speachRecognitionListener = new SpeachRecognitionListener();
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        speachRecognizer.setRecognitionListener(speachRecognitionListener);

        //this.commands = new Commands();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.mediaplayer_activity);
        listView = (ListView) findViewById(R.id.listview);

        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            } else {
                //SpeachRecognize -
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        }else{


            display();
            speachOnCreate();
            ////getSpeechInput(this.getView);

            startListening();



        }

    }


    public void getSpeechInput(View view) {

        startListening();
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch(requestCode){
            case 1:{
                if(grantResults.length>0&&grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(this,"Permission granted", Toast.LENGTH_SHORT).show();
                        display();

                    }

                }else{
                    Toast.makeText(this,"Permission denied", Toast.LENGTH_SHORT).show();
                    finish();
                }
                return;
            }

        }
    }

    class SpeachRecognitionListener implements RecognitionListener
    {
        public void onReadyForSpeech(Bundle params)
        {

            //txvResult.append("On ready");
        }
        public void onBeginningOfSpeech()
        {
            //txvResult.append("On beggining");
        }

        public void onRmsChanged(float rmsdB)
        {

        }
        public void onBufferReceived(byte[] buffer)
        {

        }
        public void onEndOfSpeech()
        {

            //txvResult.append("onEndOfSpeech");
        }
        public void onError(int error)
        {

            //txvResult.append("error " + error);
            restart();
        }
        public void onResults(Bundle results)
        {

            ArrayList<String> data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            for (int i = 0; i < data.size(); i++)
            {
                //Toast.makeText(MainActivity.this, "toast:  "+data.get(i), Toast.LENGTH_SHORT).show();
                //txvResult.append("results: " + data.get(i) + "\n");
                //tu odpalać
                boolean flag = false;
                int j;
                //String msg="";
                for (j=0;j<items.length;j++){

                    if ( (data.get(i)).toLowerCase().contains( "play "+(items[j]).toLowerCase()) ){
                        intent = new Intent(MainActivity.this,Player.class).putExtra("songs",songs).putExtra("songname",data.get(i).toString()).putExtra("pos",j);
                        startActivity(intent);
                        flag = true;
                        break;
                    }else if( ( (data.get(i)).toLowerCase().contains( "next") ) && (Player.getMediaPlayer()!=null) ){
                        Player.getMediaPlayer().seekTo(Player.getSongTime()+1);


                        break;


                    }else if( ( (data.get(i)).toLowerCase().contains( "previous") ) && (Player.getMediaPlayer()!=null) ){
                        Player.getMediaPlayer().seekTo(Player.getSongTime()+1);
                        Player.flag=true;


                        break;
                    }else if( ( (data.get(i)).toLowerCase().contains( "play") ) && (Player.getMediaPlayer()!=null) && (!Player.mediaPlayer.isPlaying() ) ){
                        Player.mediaPlayer.seekTo(Player.getCurrentTime());
                        Player.getMediaPlayer().start();
                        Player.playBtn.setBackgroundResource(R.drawable.pause);

                        break;
                    }else if( ( (data.get(i)).toLowerCase().contains( "pause") ) && (Player.getMediaPlayer()!=null) && (Player.mediaPlayer.isPlaying() ) ){
                        Player.mediaPlayer.seekTo(Player.getCurrentTime());
                        Player.getMediaPlayer().pause();
                        Player.playBtn.setBackgroundResource(R.drawable.play_arrow);

                        break;
                    }

                }


                if (flag){
                    break;
                }



            }
            restart();

        }
        public void onPartialResults(Bundle partialResults)
        {
        }
        public void onEvent(int eventType, Bundle params)
        {
        }
    }
    public void restart()
    {
        speachRecognizer.stopListening();

        //audioManager.setStreamMute(AudioManager.STREAM_NOTIFICATION, true);
        //audioManager.setStreamMute(AudioManager.STREAM_ALARM, true);
        //audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
        //audioManager.setStreamMute(AudioManager.STREAM_RING, true);
        //audioManager.setStreamMute(AudioManager.STREAM_SYSTEM, true);

        speachRecognizer.destroy();
        speachRecognizer = null;
        startListening();
    }

    private void startListening() {
        //Toast.makeText(this, "now listening", Toast.LENGTH_SHORT).show();
        speachRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speachRecognizer.setRecognitionListener(speachRecognitionListener);
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "en");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 10000);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, 10000);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);

        //if setting.SpeechEnable

        //audioManager.setStreamMute(AudioManager.STREAM_NOTIFICATION, false);
        //audioManager.setStreamMute(AudioManager.STREAM_ALARM, false);
        //audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
        //audioManager.setStreamMute(AudioManager.STREAM_RING, false);
        //audioManager.setStreamMute(AudioManager.STREAM_SYSTEM, false);


        speachRecognizer.startListening(recognizerIntent);
    }

    private boolean sentenceService(String sentence)
    {
        // locals
        boolean rV = false;
        String[] words = sentence.split(" ");

        for(int i = 0; i < words.length; i++)
        {
            commands.addWord(words[i]);
            if(commands.isValidExpression())
            {
                if(commands.isValidSong() == true)
                {
                    //this.txvResult.append("Recognized command: " + commands.getActualCommand() + "\n");
                    //this.txvResult.append("Found song: " + commands.getSong() + "\n");
                }
                else
                {
                    //this.txvResult.append("Recognized command " + commands.getActualCommand() + "\n");
                }
                rV = true;
            }
            else if(commands.unrecognizedCommand())
            {
                if (commands.isValidCommand() == true)
                {
                    //this.txvResult.append("Recognized command:\n");
                    //this.txvResult.append("Not found song: " + commands.getSong() + "\n");
                }
                else
                {
                    //this.txvResult.append("Unrecognized command\n");
                }
            }
        }
        commands.resetActualCommand();
        return rV;
    }

}
