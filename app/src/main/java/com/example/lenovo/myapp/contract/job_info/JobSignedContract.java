package com.example.lenovo.myapp.contract.job_info;

import com.example.lenovo.myapp.entity.response.bean.JobSignedInfo;
import com.example.lenovo.myapp.entity.response.result.JobSignedResult;
import com.example.lenovo.myapp.model.inf.IHttpCallBack;

import java.util.List;

/**
 * 查询已报名的工作
 */

public class JobSignedContract {
    //视图层
    public interface IJobSignedView {
        public void showJobSigned(List<JobSignedInfo> jobSignedInfoList);
    }

    //视图与逻辑处理的业务层
    public interface IJobSignedPresenter {
        void getJobSigned();
        void updateJobSigned();
        void onDestroy();           //presenter对应的view消失时调用
    }

    //逻辑业务层，主要为获取后台数据
    public interface IJobSignedModel {
        void getJobSigned(IHttpCallBack<JobSignedResult> callBack);
    }
}
