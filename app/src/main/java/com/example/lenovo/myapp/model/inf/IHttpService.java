package com.example.lenovo.myapp.model.inf;

import com.example.lenovo.myapp.entity.response.result.JobApplyResult;
import com.example.lenovo.myapp.entity.response.result.JobDetailResult;
import com.example.lenovo.myapp.entity.response.result.JobListResult;
import com.example.lenovo.myapp.entity.response.result.JobSignedResult;
import com.example.lenovo.myapp.entity.response.result.LoginResult;
import com.example.lenovo.myapp.entity.response.result.MessageResult;
import com.example.lenovo.myapp.entity.response.result.ModifyInfoResult;
import com.example.lenovo.myapp.entity.response.result.RegisterResult;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 网络请求接口
 */

public interface IHttpService {

    @POST("loginAsign/login.do")        //密码登录
    Observable<LoginResult> pwdLogin(@Body RequestBody send);

    @POST("loginAsign/loginWithPhoneCode.do")       //验证码登录
    Observable<LoginResult> msgLogin(@Body RequestBody send);

    @POST("jobFinding/queryJobsByDis.do")       //查询附近岗位
    Observable<JobListResult> getJobDataByDis(@Body RequestBody send);

    @POST("loginAsign/sendCode.do")         //获取验证码
    Observable<MessageResult> getMsg(@Body RequestBody send);

    @POST("loginAsign/regist.do")           //注册
    Observable<RegisterResult> register(@Body RequestBody send);

    @POST("jobManage/queryOneDetailJob.do")         //获取兼职详情
    Observable<JobDetailResult> getJobDetail(@Body RequestBody send);

    @POST("jobApply/apply.do")                  //报名岗位
    Observable<JobApplyResult> applyJob(@Body RequestBody send);

    @POST("jobFinding/queryJobsByKeyWords.do")          //关键字查询兼职
    Observable<JobListResult> getJobDataByKw(@Body RequestBody send);

    @POST("jobFinding/queryJobsByCriteria.do")          //条件查询兼职
    Observable<JobListResult> getJobDataByCr(@Body RequestBody send);

    @POST("jobmanage/myJobList.do")             // 查看我报名的岗位信息
    Observable<JobSignedResult> getJobSigned(@Body RequestBody send);

    @POST("accountInfo/update.do")          //修改个人信息
    Observable<ModifyInfoResult> modInfo(@Body RequestBody send);

    @POST("jobmanage/getjoblist.do")        // 查看正在热招中的兼职
    Observable<JobListResult> getJobDataHiring(@Body RequestBody send);
}
