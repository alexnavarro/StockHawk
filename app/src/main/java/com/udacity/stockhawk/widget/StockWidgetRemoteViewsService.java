package com.udacity.stockhawk.widget;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.os.Build;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.ui.StockQuoteDetailActivity;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import static com.udacity.stockhawk.data.Contract.Quote.POSITION_ID;

/**
 * Created by alexandrenavarro on 14/04/17.
 */

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class StockWidgetRemoteViewsService extends RemoteViewsService {

    private final DecimalFormat dollarFormat;
    private final DecimalFormat percentageFormat;

    public StockWidgetRemoteViewsService() {
        dollarFormat = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
        percentageFormat = (DecimalFormat) NumberFormat.getPercentInstance(Locale.getDefault());
        percentageFormat.setMaximumFractionDigits(2);
        percentageFormat.setMinimumFractionDigits(2);
        percentageFormat.setPositivePrefix("+");
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {
            private Cursor data = null;

            @Override
            public void onCreate() {
                // Nothing to do
            }

            @Override
            public void onDataSetChanged() {
                if (data != null) {
                    data.close();
                }

                final long identityToken = Binder.clearCallingIdentity();
                data = getContentResolver().query(Contract.Quote.URI,
                        Contract.Quote.QUOTE_COLUMNS.toArray(new String[]{}),
                        null, null, Contract.Quote.COLUMN_SYMBOL);
                Binder.restoreCallingIdentity(identityToken);
            }

            @Override
            public void onDestroy() {
                if (data != null) {
                    data.close();
                    data = null;
                }
            }

            @Override
            public int getCount() {
                return data == null ? 0 : data.getCount();
            }

            @Override
            public RemoteViews getViewAt(int position) {
                if (position == AdapterView.INVALID_POSITION ||
                        data == null || !data.moveToPosition(position)) {
                    return null;
                }
                RemoteViews views = new RemoteViews(getPackageName(),
                        R.layout.widget_stock_item);

                String stockSymbol = data.getString(Contract.Quote.POSITION_SYMBOL);
                String price = dollarFormat.format(data.getFloat(Contract.Quote.POSITION_PRICE));
                float rawAbsoluteChange = data.getFloat(Contract.Quote.POSITION_ABSOLUTE_CHANGE);
                float percentageChange = data.getFloat(Contract.Quote.POSITION_PERCENTAGE_CHANGE);
                String percentage = percentageFormat.format(percentageChange / 100);

                views.setTextViewText(R.id.symbol, stockSymbol);
                views.setTextViewText(R.id.price, price);
                views.setTextViewText(R.id.change, percentage);
                views.setInt(R.id.change, "setBackgroundResource", rawAbsoluteChange > 0
                        ? R.color.material_green_700 : R.color.material_red_700);

                final Intent fillInIntent = new Intent();
                fillInIntent.putExtra(StockQuoteDetailActivity.EXTRA_STOCK, stockSymbol);
                views.setOnClickFillInIntent(R.id.widget_list_item, fillInIntent);
                return views;
            }

            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.widget_stock_item);
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int position) {
                if (data.moveToPosition(position))
                    return data.getLong(POSITION_ID);
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
    }

//    private final DecimalFormat dollarFormat;
//    private final DecimalFormat percentageFormat;
//
//    public StockWidgetRemoteViewsService() {
//        super("StockWidgetRemoteViewsService");
//        dollarFormat = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
//        percentageFormat = (DecimalFormat) NumberFormat.getPercentInstance(Locale.getDefault());
//        percentageFormat.setMaximumFractionDigits(2);
//        percentageFormat.setMinimumFractionDigits(2);
//        percentageFormat.setPositivePrefix("+");
//    }
//
//    @Override
//    protected void onHandleIntent(@Nullable Intent intent) {
//        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
//        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this,
//                StockWidgetProvider.class));
//
//        Cursor data = getContentResolver().query(Contract.Quote.URI,
//                Contract.Quote.QUOTE_COLUMNS.toArray(new String[]{}),
//                null, null, Contract.Quote.COLUMN_SYMBOL);
//
//        if (data == null) {
//            return;
//        }
//        if (!data.moveToFirst()) {
//            data.close();
//            return;
//        }
//
//        String stockSymbol = data.getString(Contract.Quote.POSITION_SYMBOL);
//        String price = dollarFormat.format(data.getFloat(Contract.Quote.POSITION_PRICE));
//        float rawAbsoluteChange = data.getFloat(Contract.Quote.POSITION_ABSOLUTE_CHANGE);
//        float percentageChange = data.getFloat(Contract.Quote.POSITION_PERCENTAGE_CHANGE);
//        data.close();
//        String percentage = percentageFormat.format(percentageChange / 100);
//
//        for (int appWidgetId : appWidgetIds) {
//            RemoteViews views = new RemoteViews(getPackageName(), R.layout.widget_stock);
//            views.setTextViewText(R.id.symbol, stockSymbol);
//            views.setTextViewText(R.id.price, price);
//            views.setTextViewText(R.id.change, percentage);
//            views.setInt(R.id.change, "setBackgroundResource", rawAbsoluteChange > 0
//                    ? R.color.material_green_700 : R.color.material_red_700);
//
//            // Create an Intent to launch MainActivity
//            Intent launchIntent = new Intent(this, MainActivity.class);
//            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, launchIntent, 0);
//            views.setOnClickPendingIntent(R.id.widget, pendingIntent);
//
//            // Tell the AppWidgetManager to perform an update on the current app widget
//            appWidgetManager.updateAppWidget(appWidgetId, views);
//        }
//    }
//
//    private int getWidgetWidth(AppWidgetManager appWidgetManager, int appWidgetId) {
//        // Prior to Jelly Bean, widgets were always their default size
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
//            return getResources().getDimensionPixelSize(R.dimen.widget_stock_default_width);
//        }
//        // For Jelly Bean and higher devices, widgets can be resized - the current size can be
//        // retrieved from the newly added App Widget Options
//        return getWidgetWidthFromOptions(appWidgetManager, appWidgetId);
//    }
//
//    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
//    private int getWidgetWidthFromOptions(AppWidgetManager appWidgetManager, int appWidgetId) {
//        Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetId);
//        if (options.containsKey(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH)) {
//            int minWidthDp = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);
//            // The width returned is in dp, but we'll convert it to pixels to match the other widths
//            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
//            return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, minWidthDp,
//                    displayMetrics);
//        }
//        return  getResources().getDimensionPixelSize(R.dimen.widget_stock_default_width);
//    }
}
