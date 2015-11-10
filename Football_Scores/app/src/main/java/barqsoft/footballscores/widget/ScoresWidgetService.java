package barqsoft.footballscores.widget;

/**
 * Created by graingersoftware on 11/3/15.
 */

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import java.util.List;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.R;

public class ScoresWidgetService extends RemoteViewsService
{

    private static final String LOG_TAG = ScoresWidgetService.class.getSimpleName();

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent)
    {
        return new ScoresRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}

class ScoresRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory
{
    //private static final int mCount = 10;
    private int mCount;
    private List<WidgetItem> mWidgetItems = new ArrayList<WidgetItem>();
    private Context mContext;
    private int mAppWidgetId;
    private static final String LOG_TAG = ScoresRemoteViewsFactory.class.getSimpleName();

    public ScoresRemoteViewsFactory(Context context, Intent intent)
    {
        mContext = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    private final int COL_DATE = 0;
    private final int COL_TIME = 1;
    private final int COL_HOME = 2;
    private final int COL_AWAY = 3;
    private final int COL_HOME_GOALS = 4;
    private final int COL_AWAY_GOALS = 5;
    private final int COL_MATCH_= 6;

    public void onCreate()
    {
        // In onCreate() you setup any connections / cursors to your data source. Heavy lifting,
        // for example downloading or creating content etc, should be deferred to onDataSetChanged()
        // or getViewAt(). Taking more than 20 seconds in this call will result in an ANR.

        try
        {
            String[] projection = new String[]
                    {
                            DatabaseContract.scores_table.DATE_COL,
                            DatabaseContract.scores_table.TIME_COL,
                            DatabaseContract.scores_table.HOME_COL,
                            DatabaseContract.scores_table.AWAY_COL,
                            DatabaseContract.scores_table.HOME_GOALS_COL,
                            DatabaseContract.scores_table.AWAY_GOALS_COL,
                            DatabaseContract.scores_table.MATCH_DAY
                    };

            Cursor thisCursor = mContext.getContentResolver().query(DatabaseContract.scores_table.buildMostRecentUri(),
                    projection, null, null, DatabaseContract.scores_table._ID + " DESC");
            mCount = 0;
            while (thisCursor.moveToNext())
            {
                String homeTeam = thisCursor.getString(COL_HOME);
                String awayTeam = thisCursor.getString(COL_AWAY);
                String score = thisCursor.getString(COL_HOME_GOALS) + " - " + thisCursor.getString(COL_AWAY_GOALS);
                Log.i(LOG_TAG, "DATE: " + thisCursor.getString(0));
                Log.i(LOG_TAG, "TIME: " + thisCursor.getString(1));
                Log.i(LOG_TAG, "HOME GOALS: " + thisCursor.getString(4));
                mWidgetItems.add(new WidgetItem(homeTeam, awayTeam, score));
            }
        } catch (Exception ex)
        {
            Log.e(LOG_TAG, "EXCEPTION: " + ex);
        }

    }

    public void onDestroy()
    {
        // In onDestroy() you should tear down anything that was setup for your data source,
        // eg. cursors, connections, etc.
        mWidgetItems.clear();
    }

    public int getCount()
    {
        return mWidgetItems.size();
    }

    public RemoteViews getViewAt(int position)
    {
        // position will always range from 0 to getCount() - 1.
        // We construct a remote views item based on our widget item xml file, and set the
        // text based on the position.
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
        rv.setTextViewText(R.id.score, mWidgetItems.get(position).score);
        rv.setTextViewText(R.id.leftTeam, mWidgetItems.get(position).leftTeam);
        rv.setTextViewText(R.id.rightTeam, mWidgetItems.get(position).rightTeam);
        // Next, we set a fill-intent which will be used to fill-in the pending intent template
        // which is set on the collection view in StackWidgetProvider.
        Bundle extras = new Bundle();
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        rv.setOnClickFillInIntent(R.id.score, fillInIntent);
        // You can do heaving lifting in here, synchronously. For example, if you need to
        // process an image, fetch something from the network, etc., it is ok to do it here,
        // synchronously. A loading view will show up in lieu of the actual contents in the
        // interim.


        try
        {
            System.out.println("Loading view " + position);
            Thread.sleep(500);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        // Return the remote views object.
        return rv;
    }

    public RemoteViews getLoadingView()
    {
        // You can create a custom loading view (for instance when getViewAt() is slow.) If you
        // return null here, you will get the default loading view.
        return null;
    }

    public int getViewTypeCount()
    {
        return 1;
    }

    public long getItemId(int position)
    {
        return position;
    }

    public boolean hasStableIds()
    {
        return true;
    }

    public void onDataSetChanged()
    {
        // This is triggered when you call AppWidgetManager notifyAppWidgetViewDataChanged
        // on the collection view corresponding to this factory. You can do heaving lifting in
        // here, synchronously. For example, if you need to process an image, fetch something
        // from the network, etc., it is ok to do it here, synchronously. The widget will remain
        // in its current state while work is being done here, so you don't need to worry about
        // locking up the widget.
    }
}