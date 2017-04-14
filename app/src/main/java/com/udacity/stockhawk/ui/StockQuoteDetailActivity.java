package com.udacity.stockhawk.ui;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by alexandrenavarro on 09/04/17.
 */

public class StockQuoteDetailActivity extends AppCompatActivity {

    public static final String EXTRA_STOCK = "extra_stock_name";

    @BindView(R.id.chart)LineChart chart;
    private String mStockName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_quote_detail);
        ButterKnife.bind(this);

        mStockName = getIntent().getStringExtra(EXTRA_STOCK);

        Cursor cursor = getContentResolver().query(Contract.Quote.makeUriForStock(mStockName), null, null, null, null);
        cursor.moveToNext();
        String history = cursor.getString(Contract.Quote.POSITION_HISTORY);
        cursor.close();
        String[] lines = history.split("\n");
        List<Entry> entries = new ArrayList<>();

        for(int i = lines.length -1; i > -1 ; i--){
            String[] row = lines[i].split(",");
            Float date = Float.valueOf(row[0]);
            Float price = Float.valueOf(row[1]);
            Entry entry = new Entry(date, price);
            entries.add(entry);
        }

        chart.getDescription().setEnabled(false);
        chart.setDragDecelerationFrictionCoef(0.9f);

        // enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setDrawGridBackground(false);
        chart.setHighlightPerDragEnabled(true);

        chart.setBackgroundColor(Color.LTGRAY);

        LineDataSet dataSet = new LineDataSet(entries, mStockName);
        dataSet.setColor(ColorTemplate.getHoloBlue());
        dataSet.setCircleColor(Color.YELLOW);
        dataSet.setLineWidth(2f);
        dataSet.setCircleRadius(3f);
        dataSet.setFillAlpha(65);
        dataSet.setFillColor(ColorTemplate.getHoloBlue());
        dataSet.setHighLightColor(Color.rgb(244, 117, 117));
        dataSet.setDrawCircleHole(false);


        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        XAxis xAxis = chart.getXAxis();
        xAxis.setTextSize(11f);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis((long) value);
                int style = DateFormat.SHORT;
                DateFormat df = DateFormat.getDateInstance(style, Locale.getDefault());
                return df.format(calendar.getTime());
            }
        });

        // get the legend (only possible after setting data)
        Legend l = chart.getLegend();
        l.setForm(Legend.LegendForm.LINE);
        l.setTextSize(11f);
        l.setTextColor(Color.WHITE);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);

        chart.invalidate();

    }
}
