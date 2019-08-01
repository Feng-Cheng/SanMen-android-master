package com.test.ydool.sanmen.activities;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dou361.dialogui.DialogUIUtils;
import com.test.ydool.sanmen.R;
import com.test.ydool.sanmen.adapter.GridViewAdapter;
import com.test.ydool.sanmen.base.BaseActivity;
import com.test.ydool.sanmen.bean.Point2D;
import com.test.ydool.sanmen.bean.PositionRootBean;
import com.test.ydool.sanmen.bean.VillageButtonBean;
import com.test.ydool.sanmen.net.repository.UserRepository;
import com.test.ydool.sanmen.utils.AssetsJsonManager;
import com.test.ydool.sanmen.utils.IsPtInPoly;
import com.test.ydool.sanmen.utils.ScreenManager;
import com.test.ydool.sanmen.utils.ToastUtil;
import com.test.ydool.sanmen.view.MyTextview;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.codeboy.android.aligntextview.AlignTextView;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2018/5/29.
 *
 * 礼堂地图
 */

public class AuditorMapActivity extends BaseActivity{

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_menu)
    ImageView ivBack;
    @BindView(R.id.iv_map)
    ImageView ivMap;
    @BindView(R.id.tv_jiedao_name)
    TextView tvJieDao;
//    @BindView(R.id.rl_bttons)
//    RelativeLayout rlButtons;
    @BindView(R.id.grid_view)
    GridView gridView;
//    @BindView(R.id.tv_sanmen_jieshao)
//    TextView tvJieShao;
    @BindView(R.id.tv_sanmen_jieshao)
    AlignTextView tvJieShao;
    @BindView(R.id.tv_jiedao_jieshao)
    MyTextview tvJieJieShao;
    private UserRepository userRepository;

    private CompositeSubscription subscriptions;


    Button btn[] ;

    private List<String> positon = new ArrayList<>();
    private List<String> name = new ArrayList<>();
    private AssetsJsonManager mAssetsJsonManager;

//331022100000海游街道
//331022101000沙柳街道
//331022102000珠岙镇
//331022103000亭旁镇
//331022105000健跳镇
//331022106000横渡镇
//331022108000花桥镇
//331022205000蛇蟠乡
//659004502515海润街道
//659004502517浦坝港镇
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenManager.getInstance().pushActivity(this);
        setContentView(R.layout.activity_auditor_map);
        ButterKnife.bind(this);
        mAssetsJsonManager = AssetsJsonManager.getInstance(mContext);

        initView();
    }

    double x;
    double y;
    float multiples;
    private GridViewAdapter adapter;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0){
//                ivMap.setImageResource(0);
                ivMap.setBackgroundResource(R.drawable.ic_zhushan);
                tvJieDao.setText("珠岙镇");
            }else if (msg.what == 1){
//                ivMap.setImageResource(0);
                ivMap.setBackgroundResource(R.drawable.ic_tingpang);
                tvJieDao.setText("亭旁镇");
            }else if (msg.what == 2){
//                ivMap.setImageResource(0);
                ivMap.setBackgroundResource(R.drawable.ic_haiyou);
                tvJieDao.setText("海游街道");
            }else if (msg.what == 3){
//                ivMap.setImageResource(0);
                ivMap.setBackgroundResource(R.drawable.ic_shaliu);
                tvJieDao.setText("沙柳街道");
            }else if (msg.what == 4){
//                ivMap.setImageResource(0);
                ivMap.setBackgroundResource(R.drawable.ic_hairun);
                tvJieDao.setText("海润街道");
            }else if (msg.what == 5){
//                ivMap.setImageResource(0);
                ivMap.setBackgroundResource(R.drawable.ic_hengdu);
                tvJieDao.setText("横渡镇");
            }else if (msg.what == 6){
//                ivMap.setImageResource(0);
                ivMap.setBackgroundResource(R.drawable.ic_huaqiao);
                tvJieDao.setText("花桥镇");
            }else if (msg.what == 7){
//                ivMap.setImageResource(0);
                ivMap.setBackgroundResource(R.drawable.ic_pubeigang);
                tvJieDao.setText("浦坝港镇");
            }else if (msg.what == 8){
//                ivMap.setImageResource(0);
                ivMap.setBackgroundResource(R.drawable.ic_jiantiao);
                tvJieDao.setText("健跳镇");
            }else if (msg.what == 9){
//                ivMap.setImageResource(0);
                ivMap.setBackgroundResource(R.drawable.ic_shepan);
                tvJieDao.setText("蛇蟠乡");
            }
            if (msg.what != -1){
                String desc = mAssetsJsonManager.getVillageIntroduce(positon.get(msg.what));
                tvJieJieShao.setText(desc);
                tvJieJieShao.setVisibility(View.VISIBLE);
                tvJieShao.setVisibility(View.GONE);
                tvJieDao.setVisibility(View.VISIBLE);
                Subscription subscription = null;
                subscription = userRepository.getPositionRoot(positon.get(msg.what))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response ->{
                            List<VillageButtonBean> list = new ArrayList<>();
                            List<PositionRootBean.row> jsonArray = response.getRows();
                            for (int i = 0 ; i < jsonArray.size() ; i++){
                                list.add(new VillageButtonBean(jsonArray.get(i).getId(),jsonArray.get(i).getPosition_name()));
                            }
                            adapter = new GridViewAdapter(getApplication(), list);
                            gridView.setAdapter(adapter);
                            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    Intent intent = new Intent(getApplication(),AuditorInformActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("id",list.get(i).getId());
                                    bundle.putString("name",list.get(i).getVillageName());
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            });
                        },throwable -> {
                            throwable.printStackTrace();
                            ToastUtil.showLongMessage(mContext,"网络异常！");
                        });

                subscriptions.add(subscription);
//                addLinearLayout(100);

               ivMap.setClickable(false);
               ivMap.setOnTouchListener(null);
            }
        }
    };
    private void initView() {
        tvTitle.setText("礼堂风采");
        tvTitle.setTextColor(0xfffaa8ae);
        ivBack.setImageResource(R.drawable.ic_back);
        userRepository = new UserRepository();
        subscriptions = new CompositeSubscription();

        String jieshao = "\u3000\u3000"+getResources().getString(R.string.jieshao);
        tvJieShao.setText(jieshao);

        positon.add("331022102000");
        positon.add("331022103000");
        positon.add("331022100000");
        positon.add("331022101000");
        positon.add("659004502515");
        positon.add("331022106000");
        positon.add("331022108000");
        positon.add("659004502517");
        positon.add("331022105000");
        positon.add("331022205000");


        ViewTreeObserver vto2 = ivMap.getViewTreeObserver();
        vto2.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ivMap.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                // 中心坐标
                x = (ivMap.getRight() - ivMap.getLeft()) / 2;
                y = (ivMap.getBottom() - ivMap.getTop()) / 2;
            }
        });

        // 获取屏幕测量工具DisplayMetrics
        DisplayMetrics dm = this.getResources().getDisplayMetrics();
        float density = dm.density;//屏幕密度系数
        multiples = density/2;

        ivMap.setOnTouchListener(new onIVTouch());


        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvJieShao.getVisibility()==View.GONE){
                    ivMap.setBackgroundResource(R.drawable.ic_sanmen1);
                    tvJieDao.setVisibility(View.GONE);
                    tvJieJieShao.setVisibility(View.GONE);
                    tvJieJieShao.setText("");
                    adapter = new GridViewAdapter(getApplication(), new ArrayList<>());
                    gridView.setAdapter(adapter);
                    ivMap.setClickable(true);
                    tvJieShao.setVisibility(View.VISIBLE);
                    ivMap.setOnTouchListener(new onIVTouch());
                }else {
                    finish();
                }
            }
        });

    }

    class onIVTouch implements View.OnTouchListener {

        private double A;
        private int stnumber;
        int i=-1;
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    java.text.DecimalFormat df = new java.text.DecimalFormat(
                            "#0.00");
                    // double B = Math.acos((a*a + c*c - b*b)/(2.0*a*c));
                    int[] aa = getPosition(ivMap);
//                    ToastUtil.showLongMessage(mContext,aa[0]+"---"+aa[1]);
                    double multiple = (double) (aa[1])/238;
                    double X = (double) event.getX();
                    double Y = (double) event.getY();
                    // 利用三角函数算法   得到点到圆心的连线长和角度
                    int lin = (int) Math
                            .sqrt((X - x*multiple) * (X - x*multiple) + (Y - y*multiple) * (Y - y*multiple));
                    double B = Math.acos(((x*multiple - X) * (x*multiple - X) + lin * lin - (y*multiple - Y)
                            * (y*multiple - Y))
                            / (2.0 * (X - x*multiple) * lin));
                    A = Math.abs(90 - Math.toDegrees(B));// 绝对值
                    String temp = df.format(A);


                    Log.i("TAG", "点击x坐标=" + X + "点击y坐标=" + Y);
//                    Log.i("TAG", "线段长=" + lin);
//                    Log.i("TAG", "角度a=" + temp);


                    Point2D point2D = new Point2D(X,Y);

                    List<Point2D> list = new ArrayList<>();
                    list.add(new Point2D(213*multiple,550*multiple));
                    list.add(new Point2D(235*multiple,506*multiple));
                    list.add(new Point2D(295*multiple,468*multiple));
                    list.add(new Point2D(302*multiple,353*multiple));
                    list.add(new Point2D(284*multiple,323*multiple));
                    list.add(new Point2D(291*multiple,323*multiple));
                    list.add(new Point2D(272*multiple,364*multiple));
                    list.add(new Point2D(209*multiple,338*multiple));
                    list.add(new Point2D(187*multiple,375*multiple));
                    list.add(new Point2D(150*multiple,428*multiple));

                    List<Point2D> list1 = new ArrayList<>();
                    list1.add(new Point2D(373.22*multiple,394.18*multiple));
                    list1.add(new Point2D(302.16*multiple,487.23*multiple));
                    list1.add(new Point2D(258.13*multiple,513.25*multiple));
                    list1.add(new Point2D(287.18*multiple,625.28*multiple));
                    list1.add(new Point2D(347.21*multiple,603.25*multiple));
                    list1.add(new Point2D(376.21*multiple,666.29*multiple));
                    list1.add(new Point2D(410.23*multiple,606.29*multiple));
                    list1.add(new Point2D(484.32*multiple,595.28*multiple));
                    list1.add(new Point2D(462.30*multiple,450.50*multiple));

                    List<Point2D> list2 = new ArrayList<>();
                    list2.add(new Point2D(324.18*multiple,431.21*multiple));
                    list2.add(new Point2D(395.24*multiple,360.20*multiple));
                    list2.add(new Point2D(492.31*multiple,424.18*multiple));
                    list2.add(new Point2D(466.30*multiple,320.12*multiple));
                    list2.add(new Point2D(469.29*multiple,282.15*multiple));
                    list2.add(new Point2D(376.21*multiple,260.12*multiple));
                    list2.add(new Point2D(328.18*multiple,327.15*multiple));

                    List<Point2D> list3 = new ArrayList<>();
                    list3.add(new Point2D(373.22*multiple,245.12*multiple));
                    list3.add(new Point2D(410.22*multiple,148.09*multiple));
                    list3.add(new Point2D(429.25*multiple,118.09*multiple));
                    list3.add(new Point2D(466.30*multiple,208.09*multiple));
                    list3.add(new Point2D(600.38*multiple,238.09*multiple));
                    list3.add(new Point2D(551.33*multiple,252.15*multiple));
                    list3.add(new Point2D(521.32*multiple,226.14*multiple));
                    list3.add(new Point2D(488.32*multiple,260.12*multiple));
                    list3.add(new Point2D(436.28*multiple,219.10*multiple));

                    List<Point2D> list4 = new ArrayList<>();
                    list4.add(new Point2D(655.4*multiple,545.12*multiple));
                    list4.add(new Point2D(562.36*multiple,286.14*multiple));
                    list4.add(new Point2D(518.33*multiple,255.12*multiple));
                    list4.add(new Point2D(514.33*multiple,331.14*multiple));
                    list4.add(new Point2D(492.31*multiple,342.15*multiple));
                    list4.add(new Point2D(536.35*multiple,379.18*multiple));
                    list4.add(new Point2D(503.30*multiple,472.23*multiple));
                    list4.add(new Point2D(556.32*multiple,416.21*multiple));
                    list4.add(new Point2D(655.40*multiple,450.20*multiple));
                    list4.add(new Point2D(640.41*multiple,357.15*multiple));
                    list4.add(new Point2D(674.42*multiple,308.17*multiple));
                    list4.add(new Point2D(756.47*multiple,290.12*multiple));

                    List<Point2D> list5 = new ArrayList<>();
                    list5.add(new Point2D(566.36*multiple,558.25*multiple));
                    list5.add(new Point2D(633.38*multiple,513.25*multiple));
                    list5.add(new Point2D(711.43*multiple,528.25*multiple));
                    list5.add(new Point2D(715.47*multiple,416.21*multiple));
                    list5.add(new Point2D(585.35*multiple,413.17*multiple));
                    list5.add(new Point2D(540.35*multiple,457.23*multiple));
                    list5.add(new Point2D(566.36*multiple,491.21*multiple));

                    List<Point2D> list6 = new ArrayList<>();
                    list6.add(new Point2D(566.36*multiple,577.23*multiple));
                    list6.add(new Point2D(614.39*multiple,662.31*multiple));
                    list6.add(new Point2D(722.46*multiple,636.29*multiple));
                    list6.add(new Point2D(711.43*multiple,558.25*multiple));
                    list6.add(new Point2D(640.41*multiple,539.26*multiple));

                    List<Point2D> list7 = new ArrayList<>();
                    list7.add(new Point2D(1377.90*multiple,744.34*multiple));
                    list7.add(new Point2D(1194.78*multiple,565.28*multiple));
                    list7.add(new Point2D(1172.76*multiple,491.21*multiple));
                    list7.add(new Point2D(1012.63*multiple,487.23*multiple));
                    list7.add(new Point2D(886.55*multiple,632.31*multiple));
                    list7.add(new Point2D(733.44*multiple,629.26*multiple));
                    list7.add(new Point2D(804.50*multiple,744.34*multiple));
                    list7.add(new Point2D(741.48*multiple,937.31*multiple));
                    list7.add(new Point2D(644.41*multiple,893.40*multiple));
                    list7.add(new Point2D(778.49*multiple,882.39*multiple));
                    list7.add(new Point2D(986.61*multiple,822.31*multiple));
                    list7.add(new Point2D(1206.78*multiple,867.39*multiple));
                    list7.add(new Point2D(1202.78*multiple,740.28*multiple));

                    List<Point2D> list8 = new ArrayList<>();
                    list8.add(new Point2D(685.41*multiple,338.17*multiple));
                    list8.add(new Point2D(852.53*multiple,275.12*multiple));
                    list8.add(new Point2D(1031.66*multiple,267.15*multiple));
                    list8.add(new Point2D(1139.72*multiple,342.15*multiple));
                    list8.add(new Point2D(1165.73*multiple,465.20*multiple));
                    list8.add(new Point2D(990.61*multiple,457.23*multiple));
                    list8.add(new Point2D(927.59*multiple,517.23*multiple));
                    list8.add(new Point2D(897.58*multiple,588.25*multiple));
                    list8.add(new Point2D(771.50*multiple,584.26*multiple));
                    list8.add(new Point2D(752.47*multiple,435.20*multiple));
                    list8.add(new Point2D(696.44*multiple,450.20*multiple));
                    list8.add(new Point2D(689.41*multiple,334.18*multiple));

                    List<Point2D> list9 = new ArrayList<>();
                    list9.add(new Point2D(726.45*multiple,197.18*multiple));
                    list9.add(new Point2D(748.47*multiple,163.09*multiple));
                    list9.add(new Point2D(815.53*multiple,163.07*multiple));
                    list9.add(new Point2D(975.62*multiple,252.15*multiple));
                    list9.add(new Point2D(752.47*multiple,264.10*multiple));
                    list9.add(new Point2D(704.44*multiple,238.09*multiple));
                    list9.add(new Point2D(730.45*multiple,234.10*multiple));
                    list9.add(new Point2D(745.48*multiple,208.09*multiple));

                    if (IsPtInPoly.isPtInPoly(point2D,list)) {
                       i=0;
                    }else if (IsPtInPoly.isPtInPoly(point2D,list1)) {
                        i=1;
                    }else if (IsPtInPoly.isPtInPoly(point2D,list2)) {
                        i=2;
                    }else if (IsPtInPoly.isPtInPoly(point2D,list3)) {
                        i=3;
                    }else if (IsPtInPoly.isPtInPoly(point2D,list4)) {
                        i=4;
                    }else if (IsPtInPoly.isPtInPoly(point2D,list5)) {
                        i=5;
                    }else if (IsPtInPoly.isPtInPoly(point2D,list6)) {
                        i=6;
                    }else if (IsPtInPoly.isPtInPoly(point2D,list7)) {
                        i=7;
                    }else if (IsPtInPoly.isPtInPoly(point2D,list8)) {
                        i=8;
                    }else if (IsPtInPoly.isPtInPoly(point2D,list9)) {
                        i=9;
                    }

                    new Thread(new Runnable(){
                        @Override
                        public void run() {
                            Message message = new Message();
                            message.what = i;
                            mHandler.sendMessage(message);
                        }
                    }).start();
                    break;
                default:
                    break;
            }
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        if (tvJieShao.getVisibility()==View.GONE){
            ivMap.setBackgroundResource(R.drawable.ic_sanmen1);
            tvJieDao.setVisibility(View.GONE);
            tvJieJieShao.setVisibility(View.GONE);
            tvJieJieShao.setText("");
            adapter = new GridViewAdapter(getApplication(), new ArrayList<>());
            gridView.setAdapter(adapter);
            ivMap.setClickable(true);
            tvJieShao.setVisibility(View.VISIBLE);
            ivMap.setOnTouchListener(new onIVTouch());
        }else {
            finish();
        }
    }

    /**
     * 获取指定控件的坐标[只能再oncreate之后的生命周期中执行，否则返回（0,0）]
     *
     * @param view
     *            指定控件
     * @return 长度为2的int数组pos，X=pos[0] ,Y=pos[1]
     */
    public static int[] getPosition(View view) {
        int pos[] = new int[2];
        // 获取在当前窗口内的绝对坐标
        // view.getLocationOnScreen(pos);

        // 得到view在整个屏幕内上的位置
        view.getLocationInWindow(pos);
//        Log.i("TAG", pos[0] + "---" + pos[1]);
        return pos;
    }

}
