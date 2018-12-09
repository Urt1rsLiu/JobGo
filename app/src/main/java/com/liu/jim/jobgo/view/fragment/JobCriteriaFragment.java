package com.liu.jim.jobgo.view.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.liu.jim.jobgo.R;
import com.liu.jim.jobgo.contract.job_info.JobDataByCrContract;
import com.liu.jim.jobgo.entity.response.bean.Area;
import com.liu.jim.jobgo.entity.response.bean.JobBasicInfo;
import com.liu.jim.jobgo.entity.response.bean.Screen;
import com.liu.jim.jobgo.presenter.job_info.JobDataByCrPresenter;
import com.liu.jim.jobgo.util.CriteriaUtil;
import com.liu.jim.jobgo.view.adapter.list_view_adapter.JobAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by jim on 2018/3/6.
 */

public class JobCriteriaFragment extends Fragment implements AdapterView.OnItemSelectedListener,  CompoundButton.OnCheckedChangeListener, View.OnClickListener, JobDataByCrContract.IJobDataByCrView {
    public View mView;
    private View topFilter;
    private View popupScreenView;
    private PopupWindow popupWndScreen;
    public ListView mListView;
    public List<JobBasicInfo> mData;
    public SwipeRefreshLayout swpRefresh;
    public Activity context;
    private JobAdapter jobAdapter;
    private boolean LoadingMore = false;  //指示是否正在加载更多数据

    private AppCompatSpinner spinner_city;

    private CheckBox ck_screen;
    private CheckBox[] ck_jobType = new CheckBox[20];         //工作类型筛选
    private CheckBox[] ck_gender = new CheckBox[3];           //工作性别
    private CheckBox[] ck_payType = new CheckBox[5];          //支付类型

    private List<String> jobTypes;          //工作类型
    private Area area = new Area();         //地区
    private Screen screen = new Screen();   //其它筛选条件  包括支付方式、性别、是否需要学生、是否紧急

    private JobDataByCrContract.IJobDataByCrPresenter jobDataByCrPresenter;
    private boolean FirstChoose = true;


    public JobCriteriaFragment() {

    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化数据和视图
        this.context = this.getActivity();
        this.mView = getActivity().getLayoutInflater().inflate(R.layout.fg_job_cirterial, null, false);
        this.popupScreenView = getActivity().getLayoutInflater().inflate(R.layout.popup_screen, null, false);
        this.jobDataByCrPresenter = new JobDataByCrPresenter(this);
        bindView();
        initPopWndScreen();
        initListView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initSwipeRefresh();
        setOnScrollListener();
        return this.mView;
    }


    public void bindView() {
        mListView = this.mView.findViewById(R.id.job_list);
        swpRefresh = this.mView.findViewById(R.id.swipe_refresh_job_list);
        spinner_city = this.mView.findViewById(R.id.spinner_city);
        spinner_city.setOnItemSelectedListener(this);
        ck_screen = this.mView.findViewById(R.id.ck_screen);
        ck_screen.setOnCheckedChangeListener(this);
        ck_jobType[0] = this.popupScreenView.findViewById(R.id.ck_screen_job_type_all);
        ck_jobType[1] = this.popupScreenView.findViewById(R.id.ck_screen_job_type1);
        ck_jobType[2] = this.popupScreenView.findViewById(R.id.ck_screen_job_type2);
        ck_jobType[3] = this.popupScreenView.findViewById(R.id.ck_screen_job_type3);
        ck_jobType[4] = this.popupScreenView.findViewById(R.id.ck_screen_job_type4);
        ck_jobType[5] = this.popupScreenView.findViewById(R.id.ck_screen_job_type5);
        ck_jobType[6] = this.popupScreenView.findViewById(R.id.ck_screen_job_type6);
        ck_jobType[7] = this.popupScreenView.findViewById(R.id.ck_screen_job_type7);
        ck_jobType[8] = this.popupScreenView.findViewById(R.id.ck_screen_job_type8);
        ck_jobType[9] = this.popupScreenView.findViewById(R.id.ck_screen_job_type9);
        ck_jobType[10] = this.popupScreenView.findViewById(R.id.ck_screen_job_type10);
        ck_jobType[11] = this.popupScreenView.findViewById(R.id.ck_screen_job_type11);
        ck_jobType[12] = this.popupScreenView.findViewById(R.id.ck_screen_job_type12);
        ck_jobType[13] = this.popupScreenView.findViewById(R.id.ck_screen_job_type13);
        ck_jobType[14] = this.popupScreenView.findViewById(R.id.ck_screen_job_type14);
        ck_jobType[15] = this.popupScreenView.findViewById(R.id.ck_screen_job_type15);
        ck_jobType[16] = this.popupScreenView.findViewById(R.id.ck_screen_job_type16);
        ck_jobType[17] = this.popupScreenView.findViewById(R.id.ck_screen_job_type17);
        ck_jobType[18] = this.popupScreenView.findViewById(R.id.ck_screen_job_type18);
        ck_jobType[19] = this.popupScreenView.findViewById(R.id.ck_screen_job_type19);
        ck_gender[0] = this.popupScreenView.findViewById(R.id.ck_screen_job_gender_all);
        ck_gender[1] = this.popupScreenView.findViewById(R.id.ck_screen_job_gender_man);
        ck_gender[2] = this.popupScreenView.findViewById(R.id.ck_screen_job_gender_woman);
        ck_payType[0] = this.popupScreenView.findViewById(R.id.ck_screen_pay_type_all);
        ck_payType[1] = this.popupScreenView.findViewById(R.id.ck_screen_pay_type_day);
        ck_payType[2] = this.popupScreenView.findViewById(R.id.ck_screen_pay_type_week);
        ck_payType[3] = this.popupScreenView.findViewById(R.id.ck_screen_pay_type_half_month);
        ck_payType[4] = this.popupScreenView.findViewById(R.id.ck_screen_pay_type_month);
        for (int i = 0; i < ck_jobType.length; i++) {
            ck_jobType[i].setOnCheckedChangeListener(this);
        }
        for (int i = 0; i < ck_gender.length; i++) {
            ck_gender[i].setOnCheckedChangeListener(this);
        }
        for (int i = 0; i < ck_payType.length; i++) {
            ck_payType[i].setOnCheckedChangeListener(this);
        }
        Button btn_start_screen = this.popupScreenView.findViewById(R.id.btn_start_screen);
        Button btn_reset_screen = this.popupScreenView.findViewById(R.id.btn_reset_screen);
        btn_start_screen.setOnClickListener(this);
        btn_reset_screen.setOnClickListener(this);
        topFilter = this.mView.findViewById(R.id.ll_filter);
    }

    private void initPopWndScreen() {
        popupWndScreen = new PopupWindow(popupScreenView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,true);
        popupWndScreen.setAnimationStyle(R.style.PopupWndScreen);
        popupWndScreen.setFocusable(true);
        popupWndScreen.setOutsideTouchable(true);      //设置屏幕外点击无效，即不能通过点击外面使popupwindow消失
        popupWndScreen.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                ck_screen.setChecked(false);
            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.getId() == R.id.ck_screen) {          //筛选栏的改变
            if (isChecked) {         //checked为true时显示
                popupWndScreen.showAsDropDown(topFilter);
            } else {                //checked为false时隐藏
                popupWndScreen.dismiss();
            }
        }
        switch (buttonView.getId()) {
            case R.id.ck_screen_job_type_all:
                if (isChecked) {     //当 所有工作类型 checkbox 被选择时，其他 checkbox设为check false
                    for (int i = 1; i < ck_jobType.length; i++) {
                        if (ck_jobType[i].isChecked()) {
                            ck_jobType[i].setChecked(false);
                        }
                    }
                }
                break;
            case R.id.ck_screen_job_type1:
            case R.id.ck_screen_job_type2:
            case R.id.ck_screen_job_type3:
            case R.id.ck_screen_job_type4:
            case R.id.ck_screen_job_type5:
            case R.id.ck_screen_job_type6:
            case R.id.ck_screen_job_type7:
            case R.id.ck_screen_job_type8:
            case R.id.ck_screen_job_type9:
            case R.id.ck_screen_job_type10:
            case R.id.ck_screen_job_type11:
            case R.id.ck_screen_job_type12:
            case R.id.ck_screen_job_type13:
            case R.id.ck_screen_job_type14:
            case R.id.ck_screen_job_type15:
            case R.id.ck_screen_job_type16:
            case R.id.ck_screen_job_type17:
            case R.id.ck_screen_job_type18:
            case R.id.ck_screen_job_type19:
                if (isChecked) {                 //当有checkbox被选择时，取消全部职位的选中状态
                    ck_jobType[0].setChecked(false);
                }
                break;

            case R.id.ck_screen_job_gender_all:
                if (isChecked) {
                    ck_gender[1].setChecked(false);
                    ck_gender[2].setChecked(false);
                }
                break;
            case R.id.ck_screen_job_gender_man:
                if (isChecked) {
                    ck_gender[0].setChecked(false);
                    ck_gender[2].setChecked(false);
                }
                break;
            case R.id.ck_screen_job_gender_woman:
                if (isChecked) {
                    ck_gender[0].setChecked(false);
                    ck_gender[1].setChecked(false);
                }
                break;
            case R.id.ck_screen_pay_type_all:
                if (isChecked) {
                    ck_payType[1].setChecked(false);
                    ck_payType[2].setChecked(false);
                    ck_payType[3].setChecked(false);
                    ck_payType[4].setChecked(false);
                }
                break;
            case R.id.ck_screen_pay_type_day:
                if (isChecked) {
                    ck_payType[0].setChecked(false);
                    ck_payType[2].setChecked(false);
                    ck_payType[3].setChecked(false);
                    ck_payType[4].setChecked(false);
                }
                break;
            case R.id.ck_screen_pay_type_week:
                if (isChecked) {
                    ck_payType[0].setChecked(false);
                    ck_payType[1].setChecked(false);
                    ck_payType[3].setChecked(false);
                    ck_payType[4].setChecked(false);
                }
                break;
            case R.id.ck_screen_pay_type_half_month:
                if (isChecked) {
                    ck_payType[0].setChecked(false);
                    ck_payType[1].setChecked(false);
                    ck_payType[2].setChecked(false);
                    ck_payType[4].setChecked(false);
                }
                break;
            case R.id.ck_screen_pay_type_month:
                if (isChecked) {
                    ck_payType[0].setChecked(false);
                    ck_payType[1].setChecked(false);
                    ck_payType[2].setChecked(false);
                    ck_payType[3].setChecked(false);
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start_screen:
                startScreen();
                ck_screen.setChecked(false);    //开始筛选并隐藏筛选菜单
                break;
            case R.id.btn_reset_screen:
                resetScreen();
                ck_screen.setChecked(false);
                break;
        }
    }

    /**
     * 开始根据各CheckBox状态 收集 筛选条件
     */
    private void initScreenState() {
        CriteriaUtil cu = new CriteriaUtil();           //筛选条件的格式转换工具
        if (ck_jobType[0].isChecked()) {     //选中了全部时，直接设为null
            jobTypes = null;
        } else {                            //没有选择全部岗位
            jobTypes = new ArrayList<>();
            for (int i = 1; i < ck_jobType.length; i++) {
                if (ck_jobType[i].isChecked()) {      //将选择的jobType放入工作类型条件数组中
                    jobTypes.add(cu.getWorkTypeStr(i));
                }
            }
            if (jobTypes.size() == 0) {          //如果一个条件都没选，则默认为选择所有职位
                jobTypes = null;
                ck_jobType[0].setChecked(true);     //ckeckbox条件设为全部
            }
        }
        String cityCode = cu.getCityCode(spinner_city.getSelectedItemPosition());       //设置筛选地区
        area.setCity(cityCode);
        String gender;             //性别
        if (ck_gender[1].isChecked()) {      //选择了仅限男性
            gender = "男性";
        } else if (ck_gender[2].isChecked()) {     //选择了仅限女性
            gender = "女性";
        } else {
            gender = null;
            ck_gender[0].setChecked(true);
        }
        screen.setGender(gender);
        String payType;
        if (ck_payType[1].isChecked()) {
            payType = "日结";
        } else if (ck_payType[2].isChecked()) {
            payType = "周结";
        } else if (ck_payType[3].isChecked()) {
            payType = "半月结";
        } else if (ck_payType[4].isChecked()) {
            payType = "月结";
        } else {
            payType = null;
            ck_payType[0].setChecked(true);
        }
        screen.setPayType(payType);
    }

    /**
     * 开始发送请求查询jobList
     */
    private void startScreen() {
        jobAdapter.clearList();     //更换条件筛选前清空list内的数据源
        initScreenState();   //收集筛选条件
        jobDataByCrPresenter.startScreen(jobTypes, area, screen);
    }

    /**
     * 重置所有的筛选条件（不包括city），并查询jobList
     */
    private void resetScreen() {
        ck_jobType[0].setChecked(true);
        ck_gender[0].setChecked(true);
        ck_payType[0].setChecked(true);
        startScreen();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //判断是否为进入页面时的首次选择
        if (!FirstChoose) {
            switch (parent.getId()) {
                case R.id.spinner_city:
                    startScreen();
                    break;
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }


    /**
     * 进入页面时初始化listView，设置listView适配器，查询内容
     */
    private void initListView() {
        mData = new ArrayList<>();
        jobAdapter = new JobAdapter(getActivity(), mData);
        mListView.setAdapter(jobAdapter);
        startScreen();
        FirstChoose = false;
    }

    /**
     * 初始化刷新布局RefreshLayout
     */
    public void initSwipeRefresh() {
        //设置RefreshLayout样式
        swpRefresh.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.YELLOW, Color.RED);  //改变加载显示的颜色
        swpRefresh.setBackgroundColor(Color.WHITE);     //设置背景颜色
        swpRefresh.setSize(SwipeRefreshLayout.DEFAULT);     //设置初始时的大小
        swpRefresh.setDistanceToTriggerSync(300);       //设置向下拉多少出现刷新
        swpRefresh.setProgressViewEndTarget(false, 200);    //设置刷新旋转出现的位置
        swpRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {    //设置SwipeRefreshLayout监听，监听是否下拉刷新
            @Override
            public void onRefresh() {
                //检查是否处于刷新状态
                if (swpRefresh.isRefreshing()) {
                    swpRefresh.setRefreshing(true);
                    //模拟加载网络数据
                    new Handler().post(new Runnable() {
                        public void run() {
                            updateJobList();
                        }
                    });
                }
            }
        });
    }

    /**
     * 更新joblist
     */
    private void updateJobList() {
        jobAdapter.clearList();
        jobDataByCrPresenter.refreshList(jobTypes, area, screen);
    }

    /**
     * 设置ListView的滑动监听，监听是否上拉滑动到底部
     */
    public void setOnScrollListener() {
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:        // 当不滚动时
                        if (view.getLastVisiblePosition() == (view.getCount() - 1) && !LoadingMore) {       // 判断滚动到底部且是否正在加载
                            LoadingMore = true;
                            jobDataByCrPresenter.LoadMore(jobTypes,area,screen);
                        }
                        break;
                }
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    @Override
    public void showJobDataByCr(List<JobBasicInfo> jbi) {
        jobAdapter.addListFromBottom(jbi);
        if (swpRefresh.isRefreshing()) {
            swpRefresh.setRefreshing(false);
        }
        LoadingMore = false;
    }

    /**
     * 通知回收Presenter,防止内存泄漏
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (jobDataByCrPresenter != null) {
            jobDataByCrPresenter.onDestroy();
            jobDataByCrPresenter = null;
            System.gc();
        }
    }
}
