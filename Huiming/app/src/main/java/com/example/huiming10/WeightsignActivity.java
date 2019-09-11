package com.example.huiming10;



import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class WeightsignActivity extends AppCompatActivity {
    private static String TAG = "WeightsignActivity";
    private Toolbar toolbar;
    private static final int UPDATE_TEXT = 1;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_TEXT:
                    LineChart chart = (LineChart) findViewById(R.id.chart);
                    // 制作7个数据点（沿x坐标轴）
                    LineData mLineData = makeLineData(7);
                    setChartStyle(chart, mLineData, Color.WHITE);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weightsignactivity);
        initView();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = UPDATE_TEXT;
                handler.sendMessage(message);
            }
        }, 500, 5000);


    }
    public void  initView(){
        toolbar = findViewById(R.id.toolbar4);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        toolbar.setLogo(R.mipmap.ic_launcher);
        toolbar.setTitle("慧明智能床");
        setSupportActionBar(toolbar);

    }
  /*  public class LineChartMarkView extends MarkerView {

        private TextView tvDate;
        private TextView tvValue;
        private IAxisValueFormatter xAxisValueFormatter;
        DecimalFormat df = new DecimalFormat(".00");

        public LineChartMarkView(Context context, IAxisValueFormatter xAxisValueFormatter) {
            super(context, R.layout.layout_markview);
            this.xAxisValueFormatter = xAxisValueFormatter;
            tvDate = findViewById(R.id.tv_date);
            tvValue = findViewById(R.id.tv_value);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void refreshContent(Entry e, Highlight highlight) {
            //展示自定义X轴值 后的X轴内容
            tvDate.setText(xAxisValueFormatter.getFormattedValue(e.getX(), null));
            tvValue.setText("我的收益：" + df.format(e.getY() * 100) + "%");
            super.refreshContent(e, highlight);
        }

        @Override
        public MPPointF getOffset() {
            return new MPPointF(-(getWidth() / 2), -getHeight());
        }
    }*/

    // 设置chart显示的样式
    private void setChartStyle(LineChart mLineChart, LineData lineData,
                               int color) {
        // 是否在折线图上添加边框
        mLineChart.setDrawBorders(false);

        /*  mLineChart.setDescription("");// 数据描述*/

        // 如果没有数据的时候，会显示这个，类似listview的emtpyview

        // 是否绘制背景颜色。
        // 如果mLineChart.setDrawGridBackground(false)，
        // 那么mLineChart.setGridBackgroundColor(Color.CYAN)将失效;
        mLineChart.setDrawGridBackground(false);
        mLineChart.setGridBackgroundColor(Color.CYAN);

        // 触摸
        mLineChart.setTouchEnabled(true);

        // 拖拽
        mLineChart.setDragEnabled(true);

        // 缩放
        mLineChart.setScaleEnabled(true);

        mLineChart.setPinchZoom(false);
        // 显示右边 的坐标轴
        mLineChart.getAxisRight().setEnabled(true);
        Log.d(TAG, mLineChart.getXAxis().toString());
//        设置x坐标轴的颜色为白色
       mLineChart.getXAxis().setTextColor(Color.BLACK);
//       设置y轴左侧坐标轴的颜色为蓝色
       mLineChart.getAxisLeft().setTextColor(Color.BLUE);
//       设置y轴右侧坐标轴的颜色为
       mLineChart.getAxisRight().setTextColor(Color.GREEN);
        // 让x轴在下面
        mLineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        //是否展示网格线
        mLineChart.setDrawGridBackground(false);
        // // 隐藏左边坐标轴横网格线
         mLineChart.getAxisLeft().setDrawGridLines(false);
        // // 显示右边坐标轴横网格线
        mLineChart.getAxisRight().setDrawGridLines(true);
        // // 显示X轴竖网格线
         mLineChart.getXAxis().setDrawGridLines(true);
//        设置左边的y轴为虚线,(实体线长度、间隔距离、偏移量：通常使用 0)
        mLineChart.getAxisLeft().enableGridDashedLine(10f,10f,0f);
        mLineChart.getAxisRight().enableGridDashedLine(10f,10f,0f);

        // 设置背景
        mLineChart.setBackgroundColor(color);

        // 设置x,y轴的数据
        mLineChart.setData(lineData);

        // 设置比例图标识，图形下方的语句
        Legend mLegend = mLineChart.getLegend();

        mLegend.setPosition(LegendPosition.BELOW_CHART_CENTER);
        mLegend.setForm(LegendForm.CIRCLE);// 样式
        mLegend.setFormSize(15.0f);// 字体
        mLegend.setTextColor(Color.RED);// 颜色

        // 沿x轴动画，时间2000毫秒。
        mLineChart.animateX(1000);
    }

    //  生成数据
    private LineData makeLineData(int count) {
        ArrayList<String> x = new ArrayList<String>();
        for (int i = 0; i < count; i++) {
            // x轴显示的数据
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            x.add(sdf.format(new Date()));

        }

        // 模拟左侧y轴呼吸率的数据
        ArrayList<Entry> y1 = new ArrayList<Entry>();
        for (int i = 0; i < count; i++) {
            float val = (float) (Math.random() * 150);
            Entry entry = new Entry(val, i);
            y1.add(entry);
        }

        // 模拟右侧y轴心率的数据
        ArrayList<Entry> y2 = new ArrayList<Entry>();
        for (int i = 0; i < count; i++) {
            float val = (float) (Math.random() * 40);
            Entry entry = new Entry(val, i);
            y2.add(entry);
        }

        // y轴数据集
        LineDataSet mLineDataSet1 = new LineDataSet(y1, "蓝色：心率  ");
        LineDataSet mLineDataSet2=new LineDataSet(y2,"绿色:呼吸率");
        // 用y轴的集合来设置参数
        // 线宽
        mLineDataSet1.setLineWidth(3.0f);
        mLineDataSet2.setLineWidth(3.0f);
        // 显示的圆形大小
        mLineDataSet1.setCircleSize(4.0f);
        mLineDataSet2.setCircleSize(4.0f);
        // 折线的颜色
        mLineDataSet1.setColor(Color.BLUE);
        mLineDataSet2.setColor(Color.GREEN);
        //折线设置圆滑
        mLineDataSet1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        mLineDataSet2.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        // 圆球的颜色

        mLineDataSet1.setCircleColor(Color.CYAN);
        mLineDataSet1.setCircleColor(Color.YELLOW);

        // 设置mLineDataSet.setDrawHighlightIndicators(false)后，
        // Highlight的十字交叉的纵横线将不会显示，
        // 同时，mLineDataSet.setHighLightColor(Color.CYAN)失效。
        mLineDataSet1.setDrawHighlightIndicators(true);
        mLineDataSet2.setDrawHighlightIndicators(true);
        // 按击后，十字交叉线的颜色
        mLineDataSet1.setHighLightColor(Color.RED);
        mLineDataSet2.setHighLightColor(Color.BLUE);

        // 设置这项上显示的数据点的字体大小。
        mLineDataSet1.setValueTextSize(12.0f);
        mLineDataSet2.setValueTextSize(12.0f);
        mLineDataSet1.setDrawCircleHole(false);
        mLineDataSet2.setDrawCircleHole(false);

        // 改变折线样式，用曲线。
        // mLineDataSet.setDrawCubic(true);
        // 默认是直线
        // 曲线的平滑度，值越大越平滑。
        // mLineDataSet.setCubicIntensity(0.2f);

        // 填充曲线下方的区域，红色，半透明。
        // mLineDataSet.setDrawFilled(true);
        // mLineDataSet.setFillAlpha(128);
        // mLineDataSet.setFillColor(Color.RED);

        // 填充折线上数据点、圆球里面包裹的中心空白处的颜色。
        mLineDataSet1.setCircleColorHole(Color.YELLOW);
        mLineDataSet2.setCircleColorHole(Color.YELLOW);

        // 设置折线上显示数据的格式。如果不设置，将默认显示float数据格式。
     /*   mLineDataSet1.setValueFormatter(new ValueFormatter() {

//			@Override
//			public String getFormattedValue(float value) {
//				int n = (int) value;
//				String s = "y:" + n;
//				return s;
//			}

            @Override
            public String getFormattedValue(float value, Entry entry,
                                            int dataSetIndex, ViewPortHandler viewPortHandler) {
                // TODO Auto-generated method stub
                int n = (int) value;
                String s = "y:" + n;
                return s;

            }
        });
        mLineDataSet2.setValueFormatter(new ValueFormatter() {

//			@Override
//			public String getFormattedValue(float value) {
//				int n = (int) value;
//				String s = "y:" + n;
//				return s;
//			}

            @Override
            public String getFormattedValue(float value, Entry entry,
                                            int dataSetIndex, ViewPortHandler viewPortHandler) {
                // TODO Auto-generated method stub
                int n = (int) value;
                String s = "y:" + n;
                return s;

            }
        });*/
        ArrayList<LineDataSet> mLineDataSets = new ArrayList<LineDataSet>();
        mLineDataSets.add(mLineDataSet1);
        mLineDataSets.add(mLineDataSet2);

        LineData mLineData = new LineData(mLineDataSet1,mLineDataSet2);
        return mLineData;
    }

    //  重写退出方法
    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: ");
        AlertDialog.Builder dialog = new AlertDialog.Builder(WeightsignActivity.this);
        dialog.setTitle("家居床");
        dialog.setMessage("确认要离开么");
        dialog.setCancelable(false);
        dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
