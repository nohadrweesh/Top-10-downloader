package com.example.speed.top10dowmloader;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ListView listApps;
    private  String feedUrl="http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=%d/xml";
    private  int feedLimit=10;

    private  String feedCachedUrl="INVALIDATED";
    private  static final String STATE_FEED_URL="feedUrl";
    private  static final String STATE_FEED_LIMIT="feedLimit";

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(STATE_FEED_URL,feedUrl);
        outState.putInt(STATE_FEED_LIMIT,feedLimit);
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState: called with  url : "+feedUrl+" and limit : "+feedLimit);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
if(savedInstanceState!=null) {
    feedUrl = savedInstanceState.getString(STATE_FEED_URL);

    feedLimit = savedInstanceState.getInt(STATE_FEED_LIMIT);
}


        listApps=(ListView)findViewById(R.id.xmlListView);


        Log.d(TAG, "onCreate: starting AsyncTask");
        //AsyscTask instantiation
        downloadUrl(String.format(feedUrl,feedLimit));

//        DownloadData downloadData=new DownloadData();
//        downloadData.execute(url);


        Log.d(TAG, "onCreate: done");
    }


    private class   DownloadData extends AsyncTask<String,Void,String>{
        private static final String TAG = "DownloadData";
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
           // Log.d(TAG, "onPostExecute: parameter is "+s);
            ParseApplications parseApplications=new ParseApplications();
            parseApplications.parse(s);

           // ArrayAdapter<FeedEntry> arrayAdapter=new ArrayAdapter<FeedEntry>(MainActivity.this,R.layout.list_item,parseApplications.getApplications());
           // listApps.setAdapter(arrayAdapter);
            FeedAdapter feedAdapter=new FeedAdapter(MainActivity.this,R.layout.list_record,parseApplications.getApplications());
            listApps.setAdapter(feedAdapter);

        }

        @Override
        protected String doInBackground(String... params) {
           // Log.d(TAG, "doInBackground: starts with "+params[0]);
            String rssFeed=downloadXML(params[0]);
            if(rssFeed==null){
                Log.e(TAG, "doInBackground: Error downloading" );
            }
            return rssFeed;
            //return params[0];
        }

        private String downloadXML(String urlPath){
            StringBuilder xmlResult=new StringBuilder();
            try{
                URL url=new URL(urlPath);
                HttpURLConnection connection =(HttpURLConnection)url.openConnection();
                int response=connection.getResponseCode();
                //Log.d(TAG, "downloadXML: The response Code is "+response);
                //begin to readdata
//                InputStream inputStream=connection.getInputStream();
//                InputStreamReader inputStreamReader=new InputStreamReader(inputStream);
//                BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
//
                BufferedReader reader=new BufferedReader(new InputStreamReader(connection.getInputStream()));

                int charsRead;
                char[] inputBuffer=new char[500];
                while(true){
                    charsRead=reader.read(inputBuffer);
                    if(charsRead<0){
                        break;
                    }
                    if(charsRead>0){
                        xmlResult.append(String.copyValueOf(inputBuffer,0,charsRead));
                    }
                }

                reader.close();
                return xmlResult.toString();


            }catch(MalformedURLException e){
                Log.e(TAG, "downloadXML: Invalid url "+e.getMessage() );

            }catch (IOException e){
                Log.e(TAG, "downloadXML: IOECeptionreading data "+e.getMessage() );
            }catch (SecurityException e){
                Log.e(TAG, "downloadXML: Security Exception "+e.getMessage() );
                //e.printStackTrace();
            }
            return  null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.feeds_menu,menu);
        if(feedLimit==10){
            menu.findItem(R.id.mnu10).setChecked(true);
        }else {
            menu.findItem(R.id.mnu10).setChecked(true);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();

        switch (id){
            case R.id.mnuFree:
                feedUrl="http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=%d/xml";
                break;
            case R.id.mnuPaid:
                feedUrl="http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/toppaidapplications/limit=%d/xml";
                break;
            case R.id.mnuSongs:
                feedUrl="http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topsongs/limit=%d/xml";
                break;
            case R.id.mnu10:
            case R.id.mnu25:
                if(!item.isChecked()){
                    item.setChecked(true);
                    feedLimit=35-feedLimit;

                    Log.d(TAG, "onOptionsItemSelected: "+item.getTitle()+" settinglimit to "+feedLimit);
                }else{
                    //do nothing
                    Log.d(TAG, "onOptionsItemSelected: "+item.getTitle()+" feedLimit doesn't change");
                }
                break;
            case R.id.mnuRefresh:
                feedCachedUrl="INVALIDATED";


                break;

            /*case R.id.mnu25:
                if(!item.isChecked())
                    item.setChecked(true);
                feedLimit=25;
                break;
            case R.id.mnu10:
                if(!item.isChecked())
                    item.setChecked(true);
                feedLimit=10;
                break;*/
            default:
                return super.onOptionsItemSelected(item);

        }
       // Log.d(TAG, "onOptionsItemSelected: option : "+item.getTitle()+" limit is: "+item.getTitle());
        downloadUrl(feedUrl);
        return true;

    }

    private  void downloadUrl(String feedUrl){
            if(!feedUrl.equalsIgnoreCase(feedCachedUrl)){
                Log.d(TAG, "downloadUrl: starting AsyncTask");
                //AsyscTask instantiation
                DownloadData downloadData=new DownloadData();
                downloadData.execute(String.format(feedUrl,feedLimit));
                feedCachedUrl=feedUrl;

                Log.d(TAG, "downloadUrl: url is "+String.format(feedUrl,feedLimit));
                Log.d(TAG, "downloadUrl: done");
            }else{
                Log.d(TAG, "downloadUrl: not a valid url");
            }

    }


}


