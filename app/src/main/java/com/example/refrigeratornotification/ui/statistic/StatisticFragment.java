package com.example.refrigeratornotification.ui.statistic;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.refrigeratornotification.R;
import com.example.refrigeratornotification.ui.setting.SettingFragment;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StatisticFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatisticFragment extends Fragment implements OnChartValueSelectedListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StatisticFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StatisticFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StatisticFragment newInstance(String param1, String param2) {
        StatisticFragment fragment = new StatisticFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_statistic, container, false);
    }

    private BarChart chart;
    private Random random;
    private AlertDialog dialog;
    SharedPreferences sharedPreferences;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        random = new Random();
        chart = getView().findViewById(R.id.example_bar_chart);
        chart.setNoDataText("No data availible");
        drawBarChart();

    }

    private int getColor(String colorStr) {
        switch (colorStr) {
            case "Red":
                return Color.RED;
            case "Green":
                return Color.GREEN;
            default:
                return Color.BLUE;
        }
    }


    public void drawBarChart() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String colorStr = sharedPreferences.getString("chart_color_setting", "");
        boolean showValue = sharedPreferences.getBoolean("check_show_value_chart", true);

        ArrayList<BarEntry> times = new ArrayList<>();
        times.add(new BarEntry(1, random.nextInt(301)));
        times.add(new BarEntry(2, random.nextInt(301)));
        times.add(new BarEntry(3, random.nextInt(301)));
        times.add(new BarEntry(4, random.nextInt(301)));
        times.add(new BarEntry(5, random.nextInt(301)));
        times.add(new BarEntry(6, random.nextInt(301)));
        times.add(new BarEntry(7, random.nextInt(301)));
        times.add(new BarEntry(8, random.nextInt(301)));
        times.add(new BarEntry(9, random.nextInt(301)));
        times.add(new BarEntry(10, random.nextInt(301)));
        times.add(new BarEntry(11, random.nextInt(301)));
        times.add(new BarEntry(12, random.nextInt(301)));

        BarDataSet barDataSet = new BarDataSet(times, "Time");
        barDataSet.setColor(getColor(colorStr));
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);
        if (!showValue)
            barDataSet.setDrawValues(false);

        BarData barData = new BarData(barDataSet);

        String[] lebels = {"", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

        chart.setFitBars(true);
        chart.setData(barData);
        XAxis xA = chart.getXAxis();
        xA.setValueFormatter(new IndexAxisValueFormatter(lebels));
        xA.setPosition(XAxis.XAxisPosition.BOTTOM);
        xA.setLabelCount(lebels.length);
        xA.setDrawGridLines(false);

        YAxis yAl = chart.getAxisLeft();
        yAl.setAxisMinimum(0);
        yAl.setDrawGridLines(false);

        YAxis yAr = chart.getAxisRight();
        yAr.setAxisMinimum(0);
        yAr.setDrawLabels(false);
        yAr.setDrawGridLines(false);

        chart.getDescription().setEnabled(false);
        chart.animateXY(500, 500);
        chart.setDoubleTapToZoomEnabled(false);
        chart.setDragXEnabled(false);
        chart.getLegend().setEnabled(false);
        chart.setOnChartValueSelectedListener(this);
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        String msg = String.valueOf(e.getX()) + " " + String.valueOf(e.getY());


        dialog = new AlertDialog.Builder(getContext())
                .setTitle("Information")
                .setMessage(msg)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialog.dismiss();
                    }
                }).show();
    }

    @Override
    public void onNothingSelected() {

    }
}