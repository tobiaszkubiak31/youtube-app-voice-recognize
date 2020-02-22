package app.com.youtubeapiv3.speechtotext.YoutubePlayerActivity;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

import app.com.youtubeapiv3.acitivites.YoutubePlayerActivity;
import app.com.youtubeapiv3.speechtotext.Commands;

//Singleton
public class SpeechRecognition {

	private static final String TAG = SpeechRecognition.class.getSimpleName();
	private Commands commands;
	private SpeechRecognizer speechRecognizer;
	private SpeechRecognitionListener speechRecognitionListener;
	private Intent recognizerIntent;
	private AudioManager manager;
	private YoutubePlayerActivity context;
	private CommandService commandService;

	public SpeechRecognition(YoutubePlayerActivity context) {
	    this.context = context;

		speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
		speechRecognitionListener = new SpeechRecognitionListener();
		manager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		speechRecognizer.setRecognitionListener(speechRecognitionListener);
		//this.txvResult = (TextView) findViewById(R.id.txvResult);
		this.commands = new Commands();
		this.commandService = new CommandService(context);


		startListening();



	}

	void setObserverListener(){

	}


	private void startListening() {
		speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
		speechRecognizer.setRecognitionListener(speechRecognitionListener);
		recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "en");
		recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context.getPackageName());
		recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
		recognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 10000);
		recognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, 10000);
		recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);

		//if setting.SpeechEnable
//		manager.setStreamMute(AudioManager.STREAM_MUSIC, true);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
			manager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, 0);
		} else {
			manager.setStreamMute(AudioManager.STREAM_MUSIC, true);
		}
		speechRecognizer.startListening(recognizerIntent);
	}

	public void restart()
	{
		speechRecognizer.stopListening();
		speechRecognizer.cancel();
		speechRecognizer.destroy();
		speechRecognizer = null;
		startListening();
	}

	public void destroy() {
		speechRecognizer.stopListening();
		speechRecognizer.cancel();
		speechRecognizer.destroy();
		speechRecognizer = null;
	}

	class SpeechRecognitionListener implements RecognitionListener
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
			restart();
		}
		public void onResults(Bundle results)
		{
			ArrayList<String> data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
//			for (int i = 0; i < data.size(); i++)
//			{
//				txvResult.append("results: " + data.get(i) + "\n");
//				if(sentenceService(data.get(i)) == true)
//				{
//					break;
//				}
//			}

			commandService.checkCommand(data);


			Log.e(TAG, "onResults: got respond:" + data.get(0));
			restart();
		}
		public void onPartialResults(Bundle partialResults)
		{

		}
		public void onEvent(int eventType, Bundle params)
		{

		}
	}

}
