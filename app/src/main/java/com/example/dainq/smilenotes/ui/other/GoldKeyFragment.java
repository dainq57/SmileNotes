package com.example.dainq.smilenotes.ui.other;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.dainq.smilenotes.common.Utility;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import nq.dai.smilenotes.R;

public class GoldKeyFragment extends Fragment implements OnChartValueSelectedListener, View.OnClickListener {
    private final int COUNT = 4;
    private Context mContext;
    private PieChart mChart;
    private ImageView mGoldkeyInside;

    ImageView[] mKey;

    public GoldKeyFragment(Context context) {
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_gold_key, container, false);

        initPie(view);
        return view;
    }

    private void initPie(View view) {
        mChart = (PieChart) view.findViewById(R.id.pie_chart_gold_key);
        mChart.getDescription().setEnabled(false);

        // radius of the center hole in percent of maximum radius
        mChart.setHoleRadius(35f);
        mChart.setTransparentCircleRadius(40f);

        mChart.getLegend().setEnabled(false);
        mChart.setData(generatePieData());
        mChart.setOnChartValueSelectedListener(this);

        mKey = new ImageView[COUNT];

        mKey[0] = (ImageView) view.findViewById(R.id.pie_key_34);
        mKey[0].setOnClickListener(this);

        mKey[1] = (ImageView) view.findViewById(R.id.pie_key_78);
        mKey[1].setOnClickListener(this);

        mKey[2] = (ImageView) view.findViewById(R.id.pie_key_56);
        mKey[2].setOnClickListener(this);

        mKey[3] = (ImageView) view.findViewById(R.id.pie_key_12);
        mKey[3].setOnClickListener(this);

        mGoldkeyInside = (ImageView) view.findViewById(R.id.pie_chart_inside);
        mGoldkeyInside.setOnClickListener(this);
    }

    protected PieData generatePieData() {

        ArrayList<PieEntry> entries = new ArrayList<>();

        entries.add(new PieEntry((float) (1), "Định vị"));
        entries.add(new PieEntry((float) (1), "Thành tích"));
        entries.add(new PieEntry((float) (1), "Tổ chức"));
        entries.add(new PieEntry((float) (1), "Kế hoạch"));

        PieDataSet ds1 = new PieDataSet(entries, "");
        ds1.setColors(Utility.MATERIAL_COLORS);
        ds1.setSliceSpace(2f);
        ds1.setValueTextColor(Color.WHITE);
        ds1.setValueTextSize(16f);

        return new PieData(ds1);
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        if (e == null)
            return;

        float index = h.getX();
        int ind = (int) index;

        for (int i = 0; i < COUNT; i++) {
            if (i != ind) {
                mKey[i].setVisibility(View.INVISIBLE);
            }
        }
        mKey[ind].setVisibility(View.VISIBLE);

        Log.i("VAL SELECTED", "Value: " + e.getY() + ", index: " + h.getX() + ", DataSet index: " + h.getDataSetIndex());
    }

    @Override
    public void onNothingSelected() {
        for (int i = 0; i < COUNT; i++) {
            mKey[i].setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pie_key_12:
                openInformation();
                break;

            case R.id.pie_key_34:
                openInformation();
                break;

            case R.id.pie_key_56:
                openInformation();
                break;

            case R.id.pie_key_78:
                openInformation();
                break;

            case R.id.pie_chart_inside:
                openInformation();
                Toast.makeText(mContext, "center clicked ", Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }
    }

    private void openInformation() {
        Intent intent = new Intent(mContext, GoldKeyInfoActivity.class);
        startActivity(intent);
    }
}
