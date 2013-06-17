package ru.ifmo.rain.tkachenko.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import ru.ifmo.rain.tkachenko.database.LinksDbAdapter;
import ru.ifmo.rain.tkachenko.rssreader.*;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.net.URL;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class RSSActivity extends Activity implements OnClickListener, Runnable {
    public static final String FEED_LINK = "rss.cnn.com/rss/edition_world.rss";

    public final String tag = "RSSReader";
    private RSSFeed feed = null;
    private LinksDbAdapter mDbHelper;
    public static int screenWidth = 0, screenHeight = 0;
    LinearLayout myRSS;
    Thread thread;
    AlertDialog alertDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_rss);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;
        mDbHelper = new LinksDbAdapter(this);
        myRSS = (LinearLayout) findViewById(R.id.myRSS);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_rss, menu);
        return true;
    }

    public void onClick(View v) {
        // TODO Auto-generated method stub
        Intent intent = new Intent(this, ShowDescription.class);
        intent.putExtra("description", ((PreviewView) v).getDescription());
        intent.putExtra("title", ((PreviewView) v).getTitle());

        startActivity(intent);
    }

    private RSSFeed getFeed(String urlToRssFeed) {
        try {
            URL url = new URL(urlToRssFeed);

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();

            XMLReader xmlreader = parser.getXMLReader();
            RSSHandler theRssHandler = new RSSHandler();
            xmlreader.setContentHandler(theRssHandler);
            InputSource is = new InputSource(url.openStream());
            xmlreader.parse(is);

            return theRssHandler.getFeed();
        } catch (Exception ee) {
            return null;
        }
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        thread = new Thread(this);
        thread.start();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        try {
            thread.join();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public void run() {
        // TODO Auto-generated method stub
        String curFeed = mDbHelper.getFeed(getIntent().getExtras().getString("title"));
        if (curFeed != null && curFeed.length() != 0) {
            feed = parseFeed(curFeed);
        } else {
            feed = getFeed("http://" + getIntent().getExtras().getString("query"));
        }
        rss = this;
        context = getApplicationContext();
        if (feed != null) {
            handler.sendEmptyMessage(1);
        } else {
            handler.sendEmptyMessage(2);
        }
    }

    private RSSFeed parseFeed(String curFeed) {
        StringTokenizer stok = new StringTokenizer(curFeed);
        String[] help = new String[3];
        RSSFeed ans = new RSSFeed();
        int pos = 0;
        String tmp = "";
        while (true) {
            String cur = stok.nextToken();
            if (cur.equals("###")) {
                break;
            }
            if (cur.equals("##")) {
                help[pos++] = tmp;
                tmp = "";
                pos = 0;
                ans.addItem(new RSSItem(help[0], help[1], help[2]));
                continue;
            }
            if (cur.equals("#")) {
                help[pos++] = tmp;
                tmp = "";
                continue;
            }
            tmp += cur + " ";
        }
        return ans;  //To change body of created methods use File | Settings | File Templates.
    }

    RSSActivity rss;
    Context context;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                myRSS.removeAllViews();
                feed.sortByDate();
                ArrayList<RSSItem> data = feed.getAllItems();
                String rssFeedString = "";
                for (int i = 0; i < data.size(); i++) {
                    rssFeedString += data.get(i).getTitle() + " # " + data.get(i).getDescription() + " # " + data.get(i).getPubDate() + " ## ";
                    RSSItem cur = data.get(i);
                    PreviewView pr = new PreviewView(getApplicationContext(),
                            cur);
                    pr.addTitle(cur.toString());
                    pr.addDate(cur.getPubDate());
                    pr.setOnClickListener(rss);
                    myRSS.addView(pr);
                }
                rssFeedString += "###";
                mDbHelper.updateFeed(getIntent().getExtras().getString("title"), rssFeedString);

            } else {
                myRSS.removeAllViews();
                TextView tv = new TextView(context);
                tv.setText("There is no RSS feed available.\n Please check your url.");
                tv.setTextSize(15);
                myRSS.addView(tv);
            }
        }

        ;
    };
}
