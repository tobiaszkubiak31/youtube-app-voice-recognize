package app.com.youtubeapiv3.fragments;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import app.com.youtubeapiv3.activites.DetailsActivity;
import app.com.youtubeapiv3.R;
import app.com.youtubeapiv3.adapters.VideoPostAdapter;
import app.com.youtubeapiv3.interfaces.OnItemClickListener;
import app.com.youtubeapiv3.models.YoutubeDataModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {
    private static String GOOGLE_YOUTUBE_API_KEY = "AIzaSyAdDix7i7a3an-gyXiquTV_14cIsr8-DZg";//here you should use your api key for testing purpose you can use this api also
    private static String CHANNEL_ID = "UCoMdktPbSTixAyNGwb-UYkQ"; //here you should use your channel id for testing purpose you can use this api also
    private static String CHANNLE_GET_URL = "https://www.googleapis.com/youtube/v3/search?part=snippet&order=date&channelId=" + CHANNEL_ID + "&maxResults=20&key=" + GOOGLE_YOUTUBE_API_KEY + "";


    private RecyclerView mList_videos = null;
    private VideoPostAdapter adapter = null;
    private ArrayList<YoutubeDataModel> mListData = new ArrayList<>();

    private ImageView searchButton = null;
    private TextView searchTextView = null;


    private static String SEARCH_URL = "https://www.googleapis.com/youtube/v3/search?part=snippet&order=date&q=" + "keyword" + "&maxResults=20&key=" + GOOGLE_YOUTUBE_API_KEY + "";
    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        mList_videos = (RecyclerView) view.findViewById(R.id.mList_videos);
        initList(mListData);
        this.searchButton = (ImageView)view.findViewById(R.id.imageViewSearch);
        this.searchTextView = (TextView)view.findViewById(R.id.searchView);
        new RequestYoutubeAPI().execute("https://www.googleapis.com/youtube/v3/search?part=snippet&order=date&q=" + "trends" + "&maxResults=25&key=" + SearchFragment.GOOGLE_YOUTUBE_API_KEY);

        initSearchToolbar();

        return view;
    }

    private void initList(ArrayList<YoutubeDataModel> mListData) {
        mList_videos.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new VideoPostAdapter(getActivity(), mListData, new OnItemClickListener() {
            @Override
            public void onItemClick(YoutubeDataModel item) {
                YoutubeDataModel youtubeDataModel = item;
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra(YoutubeDataModel.class.toString(), youtubeDataModel);
                startActivity(intent);
            }
        });
        mList_videos.setAdapter(adapter);

    }

    private void initSearchToolbar() {
        this.searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View var1) {
                String keywords = SearchFragment.this.searchTextView.getText().toString().replaceAll(" ", "%20");
                (new RequestYoutubeAPI()).execute("https://www.googleapis.com/youtube/v3/search?part=snippet&order=date&q=" + keywords + "&maxResults=25&key=" + SearchFragment.GOOGLE_YOUTUBE_API_KEY);
            }
        });
    }


    public void search(String keywords) {
        searchTextView.setText(keywords);
        searchButton.performClick();

    }


    //create an asynctask to get all the data from youtube
    private class RequestYoutubeAPI extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(params[0]);
            Log.e("URL", params[0]);
            try {
                HttpResponse response = httpClient.execute(httpGet);
                HttpEntity httpEntity = response.getEntity();
                String json = EntityUtils.toString(httpEntity);
                return json;
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            if (response != null) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.e("response", jsonObject.toString());
                    mListData = parseVideoListFromResponse(jsonObject);
                    initList(mListData);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public ArrayList<YoutubeDataModel> parseVideoListFromResponse(JSONObject jsonObject) {
        ArrayList<YoutubeDataModel> mList = new ArrayList<>();

        if (jsonObject.has("items")) {
            try {
                JSONArray jsonArray = jsonObject.getJSONArray("items");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json = jsonArray.getJSONObject(i);
                    if (json.has("id")) {
                        JSONObject jsonID = json.getJSONObject("id");
                        String video_id = "";
                        if (jsonID.has("videoId")) {
                            video_id = jsonID.getString("videoId");
                        }
                        if (jsonID.has("kind")) {
                            if (jsonID.getString("kind").equals("youtube#video")) {
                                YoutubeDataModel youtubeObject = new YoutubeDataModel();
                                JSONObject jsonSnippet = json.getJSONObject("snippet");
                                String title = jsonSnippet.getString("title");
                                String description = jsonSnippet.getString("description");
                                String publishedAt = jsonSnippet.getString("publishedAt");
                                String thumbnail = jsonSnippet.getJSONObject("thumbnails").getJSONObject("high").getString("url");

                                youtubeObject.setTitle(title);
                                youtubeObject.setDescription(description);
                                youtubeObject.setPublishedAt(publishedAt);
                                youtubeObject.setThumbnail(thumbnail);
                                youtubeObject.setVideo_id(video_id);
                                mList.add(youtubeObject);

                            }
                        }
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return mList;

    }

}
