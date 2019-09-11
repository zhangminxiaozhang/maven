package com.example.huiming10;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class WeightSignActivity2  extends AppCompatActivity {
    private static String TAG = "WeightsignActivity";
    private Toolbar toolbar;
    private LineChart lineChart;
    private XAxis xAxis;                //X轴
    private YAxis leftYAxis;            //左侧Y轴
    private YAxis rightYaxis;           //右侧Y轴
    private Legend legend;              //图例
    private LimitLine limitLine;        //限制线
    private static final int UPDATE_TEXT=1;
    // private MyMarkerView markerView;    //标记视图 即点击xy轴交点时弹出展示信息的View 需自定义
    private Handler handler=new Handler(){
        public void handleMessage(Message msg){
        switch (msg.what){
            case UPDATE_TEXT:
                initChart(lineChart);
                showLineChart(7,"蓝色:心率", Color.BLUE);
                addLineChart(7,"绿色：呼吸率",Color.GREEN);
                break;
                default:
                    break;
        }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weightsignactivity);
        lineChart=findViewById(R.id.chart);
        toolbar = findViewById(R.id.toolbar4);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        toolbar.setLogo(R.mipmap.ic_launcher);
        toolbar.setTitle("慧明智能床");
        Timer timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message message=new Message();
                message.what=UPDATE_TEXT;
                handler.sendMessage(message);
            }
        },500,5000);

    }


//  初始化图表
    private void initChart(LineChart lineChart) {
        /***图表设置***/
        //是否展示网格线
        lineChart.setDrawGridBackground(false);
        //是否显示边界
        lineChart.setDrawBorders(true);
        //是否可以拖动
        lineChart.setDragEnabled(false);
        //是否有触摸事件
        lineChart.setTouchEnabled(true);
        //设置XY轴动画效果
        lineChart.animateY(2500);
        lineChart.animateX(1500);

        /***XY轴的设置***/
        xAxis = lineChart.getXAxis();
        leftYAxis = lineChart.getAxisLeft();
        rightYaxis = lineChart.getAxisRight();
        //X轴设置显示位置在底部
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f);
        //保证Y轴从0开始，不然会上移一点
        leftYAxis.setAxisMinimum(0f);
        leftYAxis.setTextColor(Color.BLUE);
        leftYAxis.setDrawGridLines(true);
        leftYAxis.enableGridDashedLine(10f,10f,0f);
        rightYaxis.setAxisMinimum(0f);
        rightYaxis.setTextColor(Color.GREEN);
        rightYaxis.setDrawGridLines(true);
        rightYaxis.enableGridDashedLine(10f,10f,0f);

        Description description=new Description();
        description.setEnabled(false);
        lineChart.setDescription(description);



        /***折线图例 标签 设置***/
        legend = lineChart.getLegend();
        //设置显示类型，LINE CIRCLE SQUARE EMPTY 等等 多种方式，查看LegendForm 即可
        legend.setForm(Legend.LegendForm.LINE);
        legend.setTextSize(12f);
        //显示位置 左下方
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        //是否绘制在图表里面
        legend.setDrawInside(false);
    }


//    曲线初始化设置 一个LineDataSet 代表一条曲线,lineDataSet:线条,color:线条颜色
    private void initLineDataSet(LineDataSet lineDataSet, int color, LineDataSet.Mode mode) {
        lineDataSet.setColor(color);
        lineDataSet.setCircleColor(color);
        lineDataSet.setLineWidth(1f);
        lineDataSet.setCircleRadius(3f);
        //设置曲线值的圆点是实心还是空心
        lineDataSet.setDrawCircleHole(false);
        lineDataSet.setValueTextSize(10f);
        //设置折线图填充
        lineDataSet.setDrawFilled(true);
        lineDataSet.setFormLineWidth(1f);
        lineDataSet.setFormSize(15.f);
        if (mode == null) {
            //设置曲线展示为圆滑曲线（如果不设置则默认折线）
            lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        } else {
            lineDataSet.setMode(mode);
        }
    }

//     展示曲线，count代表出现多少个数，name表示左下角标签，color代表线条颜色
    private void showLineChart(int count, String name, int color) {
        ArrayList<Entry> y1 = new ArrayList<Entry>();
        for (int i = 0; i < count; i++) {
            float val = (float) (Math.random() * 150);
            Entry entry = new Entry(i, val);
            y1.add(entry);
        }

        // 每一个LineDataSet代表一条线
        LineDataSet lineDataSet1= new LineDataSet(y1, name);
        initLineDataSet(lineDataSet1, color, LineDataSet.Mode.LINEAR);
        LineData lineData = new LineData(lineDataSet1);
        lineChart.setData(lineData);
        lineChart.invalidate();

    }
    //    展示曲线
    private void addLineChart (int count, String name, int color){
        ArrayList<Entry> y2 = new ArrayList<Entry>();
        for (int i = 0; i < count; i++) {
            float val = (float) (Math.random() * 50);
            Entry entry2 = new Entry(i, val);
            y2.add(entry2);
        }

        // 每一个LineDataSet代表一条线
        LineDataSet lineDataSet2 = new LineDataSet(y2, name);
        initLineDataSet(lineDataSet2, color, LineDataSet.Mode.LINEAR);
        lineChart.getLineData().addDataSet(lineDataSet2);
        lineChart.invalidate();

    }

}
