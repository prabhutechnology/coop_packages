package com.prabhutech.prabhupackages.wallet.activities.landingactivity.fragments.myaccount;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;


import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;
import com.prabhutech.prabhupackages.R;
import com.prabhutech.prabhupackages.wallet.utils.DebugMode;

import java.text.DecimalFormat;

public class ChartMarkerView extends MarkerView {
    private static final String TAG = "ChartMarkerView";
    private final TextView tvContent;

    private final DebugMode debugMode;

    public ChartMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        tvContent = findViewById(R.id.tvContent);
        debugMode = new DebugMode();
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        super.refreshContent(e, highlight);
        if (e instanceof CandleEntry) {
            CandleEntry ce = (CandleEntry) e;
            tvContent.setText(Utils.formatNumber(ce.getHigh(), 0, true));
//            Log.e(TAG, "refreshContent: CandleEntry" + ce.getHigh());
            debugMode.showLog(TAG, "refreshContent: CandleEntry" + ce.getHigh());
        } else {
            String setYValue = prettyCount((double) e.getY());
            String setXValue = "";
            int pointerIndex = (int) (e.getX() - 1);
            try {
                setXValue = com.prabhutech.prabhupackages.wallet.activities.landingactivity.fragments.myaccount.MyAccountChartFragment.markerDates.get(pointerIndex);
                tvContent.setText(String.format("%s , %s", setYValue, setXValue));
            } catch (Exception ess) {
                tvContent.setText(setYValue);
//                Log.e(TAG, "refreshContent: " + ess.getMessage());
                debugMode.showLog(TAG, "refreshContent: " + ess.getMessage());
            }
           /* if (pointerIndex < 3) {
                xOffsetMultiplier = 1;
                markerView.setBackground(getResources().getDrawable(R.drawable.ic_start_pointer));
            } else if (pointerIndex > 26) {
                xOffsetMultiplier = 1;
                markerView.setBackground(getResources().getDrawable(R.drawable.ic_end_pointer));
            } else {
                xOffsetMultiplier = 3;
                markerView.setBackground(getResources().getDrawable(R.drawable.pointer));
            }*/

          /*  String setXValue = String.valueOf(e.getX());
            tvContent.setText(setYValue + " , " + setXValue);*/

            debugMode.showLog(TAG, "refreshContent: " + setYValue + " Original " + e.getY());
            debugMode.showLog(TAG, "refreshContent: " + e);
            debugMode.showLog(TAG, "refreshContent: " + setXValue);

            /*Log.e(TAG, "refreshContent: " + setYValue + " Original " + e.getY());
            Log.e(TAG, "refreshContent: " + e);
            Log.e(TAG, "refreshContent: " + setXValue);*/
        }
    }

    public static String prettyCount(Double number) {
        DecimalFormat df = new DecimalFormat("#.#");
        String numberString = "";
        if (Math.abs(number / 1000000) >= 1) {
            numberString = df.format(number / 1000000.0) + "m";
        } else if (Math.abs(number / 1000.0) >= 1) {
            numberString = df.format(number / 1000.0) + "k";
        } else {
            numberString = number.toString();
        }
        return numberString;
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}
