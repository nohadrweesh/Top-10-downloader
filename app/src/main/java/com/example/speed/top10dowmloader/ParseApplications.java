package com.example.speed.top10dowmloader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

/**
 * Created by Speed on 23/08/2017.
 */

public class ParseApplications {
    private static final String TAG = "ParseApplications";
    private ArrayList<FeedEntry>applications;

    public ParseApplications() {
        this.applications = new ArrayList<>();
    }

    public ArrayList<FeedEntry> getApplications() {
        return applications;
    }

    public  boolean parse(String xmlData){
        //going ell in parsing xml or not
        boolean status =true;
        FeedEntry currentRecord=null;
        //make sure we are in the right tafg (entry  tag)
        boolean inEntry=false;
        String textValue="";

        //for specific image size
        boolean gotImage=false;

        try {
            XmlPullParserFactory factory=XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);

            XmlPullParser xpp=factory.newPullParser();
            xpp.setInput(new StringReader(xmlData));

            int eventType=xpp.getEventType();

            while(eventType!=XmlPullParser.END_DOCUMENT){
                String tagName=xpp.getName();
                switch (eventType){
                    case XmlPullParser.START_TAG:
                        if("entry".equalsIgnoreCase(tagName)){
                            inEntry=true;
                            currentRecord=new FeedEntry();
                        }/*else if("image".equalsIgnoreCase(tagName)){
                            String imageResolution=xpp.getAttributeValue(null,"height");
                            if(imageResolution!=null){
                                gotImage="53".equalsIgnoreCase(imageResolution);
                            }
                        }*/
                        break;


                    case XmlPullParser.TEXT:
                        textValue=xpp.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        //Log.d(TAG, "parse: Ending tag  for "+tagName);
                        if(inEntry){
                            if("entry".equalsIgnoreCase(tagName)){
                                applications.add(currentRecord);
                                inEntry=false;
                            }else  if("name".equalsIgnoreCase(tagName)){
                                currentRecord.setName(textValue);
                            }else if("artist".equalsIgnoreCase(tagName)){
                                currentRecord.setArtist(textValue);
                            }else if("releaseDate".equalsIgnoreCase(tagName)){
                                currentRecord.setReleaseDate(textValue);
                            }else if("summary".equalsIgnoreCase(tagName)){
                                currentRecord.setSummary(textValue);
                            }else if("image".equalsIgnoreCase(tagName)){
                                //if(gotImage)
                                    currentRecord.setImageURL(textValue);
                            }
                        }
                        break;

                    default:
                        //nothing to do


                }
                eventType=xpp.next();

            }
//            for(FeedEntry app:applications){
//                Log.d(TAG, app.toString());
//            }

        }catch (Exception e){
            status=false;
            e.printStackTrace();
        }
        return  status;
    }
}
