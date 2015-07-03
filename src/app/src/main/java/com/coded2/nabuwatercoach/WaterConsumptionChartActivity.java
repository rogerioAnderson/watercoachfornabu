package com.coded2.nabuwatercoach;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;


public class WaterConsumptionChartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_consumption_chart);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
        }
    }

    public static class PlaceholderFragment extends Fragment {

        static final String days []={"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"};
        List<WaterDailyRecord>data;

        private LineChartView chartTop; //days
        private ColumnChartView chartBottom; //months

        private LineChartData lineData;
        private ColumnChartData columnData;



        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_dependency_chart, container, false);

            data = WaterDailyRecord.statistics(getActivity());

            chartTop = (LineChartView) rootView.findViewById(R.id.chart_top);

            generateInitialLineData();

            chartBottom = (ColumnChartView) rootView.findViewById(R.id.chart_bottom);
            generateColumnData();
            return rootView;
        }



        private void generateColumnData() {

            int numSubcolumns = 1;
            int numColumns = data.size();

            List<AxisValue> axisValues = new ArrayList<AxisValue>();
            List<Column> columns = new ArrayList<Column>();
            List<SubcolumnValue> values;
            for (int i = 0; i < numColumns; ++i) {

                values = new ArrayList<SubcolumnValue>();
                for (int j = 0; j < numSubcolumns; ++j) {
                    values.add(new SubcolumnValue(data.get(i).ml, ChartUtils.pickColor()));
                }

                axisValues.add(new AxisValue(i).setLabel(data.get(i).date));

                columns.add(new Column(values).setHasLabelsOnlyForSelected(true));
            }

            columnData = new ColumnChartData(columns);

            columnData.setAxisXBottom(new Axis(axisValues).setHasLines(true));
            columnData.setAxisYLeft(new Axis().setHasLines(true).setMaxLabelChars(6).setName("ML"));

            columnData.setAxisYRight(new Axis().setHasLines(true).setMaxLabelChars(0));


            chartBottom.setColumnChartData(columnData);

            // Set value touch listener that will trigger changes for chartTop.
            chartBottom.setOnValueTouchListener(new ValueTouchListener());

            // Set selection mode to keep selected month column highlighted.
            chartBottom.setValueSelectionEnabled(true);

            chartBottom.setZoomType(ZoomType.HORIZONTAL);
        }

        private void generateInitialLineData() {
            int numValues = days.length;

            List<AxisValue> axisValues = new ArrayList<AxisValue>();
            List<PointValue> values = new ArrayList<PointValue>();
            for (int i = 0; i < numValues; ++i) {
                values.add(new PointValue(i, 0));
                axisValues.add(new AxisValue(i).setLabel(days[i]));
            }

            Line line = new Line(values);
            line.setColor(ChartUtils.COLOR_GREEN).setCubic(true);

            List<Line> lines = new ArrayList<Line>();
            lines.add(line);

            lineData = new LineChartData(lines);
            lineData.setAxisXBottom(new Axis(axisValues).setHasLines(true).setMaxLabelChars(2));
            lineData.setAxisYLeft(new Axis().setHasLines(true).setMaxLabelChars(4).setName("ML/Day"));

            chartTop.setLineChartData(lineData);

            // For build-up animation you have to disable viewport recalculation.
            chartTop.setViewportCalculationEnabled(false);

            // And set initial max viewport and current viewport- remember to set viewports after data.
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

            String goal = preferences.getString(getString(R.string.pref_key_goal),"2000");


            Viewport v = new Viewport(0, Integer.parseInt(goal), days.length, 0);
            chartTop.setMaximumViewport(v);
            chartTop.setCurrentViewport(v);

            chartTop.setZoomType(ZoomType.HORIZONTAL);
        }


        private class ValueTouchListener implements ColumnChartOnValueSelectListener {

            @Override
            public void onValueSelected(int columnIndex, int subcolumnIndex, SubcolumnValue value) {
                generateLineData(value.getColor(), columnIndex);
            }

            @Override
            public void onValueDeselected() {

                generateInitialLineData();

            }
        }


        private void generateLineData(int color, int index) {

            chartTop.cancelDataAnimation();

            List<WaterDailyRecord> dailyTotals = WaterDailyRecord.statisticsOfMonth(getActivity(), data.get(index).date);

            Line line = lineData.getLines().get(0);// For this example there is always only one line.
            line.setColor(color);

            for (PointValue value : line.getValues()) {
                // Change target only for Y value.
                value.setTarget(value.getX(), 0);
            }

            for(WaterDailyRecord dailyRecord: dailyTotals){
                PointValue value = line.getValues().get(Integer.parseInt(dailyRecord.date));
                value.setTarget(value.getX(),dailyRecord.ml);
            }

            // Start new data animation with 300ms duration;
            chartTop.startDataAnimation(300);
        }

    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_water_consumption_chart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
