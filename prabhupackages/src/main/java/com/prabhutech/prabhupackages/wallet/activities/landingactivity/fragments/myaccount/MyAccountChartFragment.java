package com.prabhutech.prabhupackages.wallet.activities.landingactivity.fragments.myaccount;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.EntryXComparator;
import com.prabhutech.prabhupackages.R;
import com.prabhutech.prabhupackages.databinding.FragmentMyAccountChartBinding;
import com.prabhutech.prabhupackages.wallet.activities.landingactivity.fragments.myaccount.model.LastThirtyBalance;
import com.prabhutech.prabhupackages.wallet.core.prefs.UserPref;
import com.prabhutech.prabhupackages.wallet.utils.DebugMode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MyAccountChartFragment extends Fragment {
    private static final String TAG = "MyAccountChartFragment";

    private FragmentMyAccountChartBinding binding;

    private List<LastThirtyBalance> lastThirtyBalances;
    private ArrayList<Float> balances = new ArrayList<>();
    private ArrayList<String> dates = new ArrayList<>();

    private final List<Entry> entries1 = new ArrayList<>();

    public static ArrayList<String> markerDates = new ArrayList<>();

    private DebugMode debugMode;

    public MyAccountChartFragment() {
    }

    public MyAccountChartFragment(List<LastThirtyBalance> lastThirtyBalances) {
        this.lastThirtyBalances = lastThirtyBalances;
        debugMode = new DebugMode();
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMyAccountChartBinding.inflate(inflater, container, false);

        //Shared pref for account detail
        HashMap<String, String> accountDetail = UserPref.getAccountDetail(requireContext());
        String savedBalance = accountDetail.get(UserPref.ACCOUNT_BALANCE_KEY);

        //Null handle
        if (savedBalance != null) {
            if (savedBalance.isEmpty()) {
                binding.textViewOpeningBalanceAmount.setText("0.00");
                binding.textViewClosingBalanceAmount.setText("0.00");
            } else {
                binding.textViewOpeningBalanceAmount.setText(savedBalance);
                binding.textViewClosingBalanceAmount.setText(savedBalance);
            }
        }

        String day;
        binding.lineChart.resetViewPortOffsets();
        balances.clear();
        dates.clear();
        entries1.clear();
        markerDates.clear();

        if (lastThirtyBalances.size() != 0) {
            for (int i = 0; i < lastThirtyBalances.size(); i++) {
                float balance = Float.parseFloat(lastThirtyBalances.get(i).getBalance());
                balances.add(balance);

                String date = lastThirtyBalances.get(i).getDate();
                dates.add(date);
                /**
                 * Changing date format to date only
                 */
                {
                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                    try {
                        Date daters = sdf.parse(date);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd");
                        day = dateFormat.format(daters);
                        float xAxisDate = Float.parseFloat(day);
//                        Log.e(TAG, "MyChartCount: " + i + " Date: " + day);
                        debugMode.showLog(TAG, "MyChartCount: " + i + " Date: " + day);
//                        entries1.add(new Entry(xAxisDate, balance));
                        markerDates.add(date);
                        entries1.add(new Entry(i + 1, balance));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }

            // Add entries
            LineDataSet dataSet = new LineDataSet(entries1, "Balances");
            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(dataSet);
            LineData data = new LineData(dataSet);

            setupLineChart(binding.lineChart, data);

            XAxis xAxis = binding.lineChart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setLabelCount(dates.size(), true);

            YAxis yAxis = binding.lineChart.getAxisLeft();
            yAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
            yAxis.setValueFormatter(new LargeValueFormatter());

            dataSet.setDrawCircles(true);
            dataSet.setLineWidth(1.2f);
            dataSet.setCircleRadius(2f);
            dataSet.setCircleHoleRadius(2f);
            dataSet.setFillColor(getResources().getColor(R.color.colorPrimary));
            dataSet.setDrawValues(true);
            dataSet.setValueTextSize(0f);
            dataSet.setColor(getResources().getColor(R.color.colorPrimary));
            dataSet.setCircleColor(getResources().getColor(R.color.colorPrimary));
            dataSet.setHighLightColor(getResources().getColor(R.color.colorPrimary));

            // Create marker to display box when values are selected
            ChartMarkerView chartMarkerView = new ChartMarkerView(requireContext(), R.layout.line_chart_marker_view);
            chartMarkerView.setChartView(binding.lineChart);
            binding.lineChart.setMarker(chartMarkerView);

            Collections.sort(entries1, new EntryXComparator());
        } else {
            binding.lineChart.setData(null);
        }

        return binding.getRoot();
    }

    private void setupLineChart(LineChart lineChart, LineData data) {
        lineChart.setData(data);

        lineChart.setDrawGridBackground(false);

        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(false);
        lineChart.setScaleEnabled(false);

        lineChart.setBackgroundColor(getResources().getColor(android.R.color.transparent));

        lineChart.setDescription(null);

        // To show color label
        Legend l = lineChart.getLegend();
        l.setEnabled(false);

        // X and Y axis grid guild lines
        lineChart.getAxisLeft().setEnabled(true);
        lineChart.getAxisLeft().setDrawGridLines(true);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getAxisRight().setDrawGridLines(false);
        lineChart.getXAxis().setDrawGridLines(true);

        lineChart.animateX(2500);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}