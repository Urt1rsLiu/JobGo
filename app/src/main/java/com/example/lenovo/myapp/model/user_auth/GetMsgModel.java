package com.example.lenovo.myapp.model.user_auth;

import com.example.lenovo.myapp.contract.user_auth.GetMsgContract;
import com.example.lenovo.myapp.entity.request.MessageRequest;
import com.example.lenovo.myapp.entity.response.result.MessageResult;
import com.example.lenovo.myapp.model.inf.IHttpCallBack;
import com.example.lenovo.myapp.model.inf.IHttpService;
import com.example.lenovo.myapp.manager.RetrofitManager;
import com.google.gson.Gson;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import okhttp3.RequestBody;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by lenovo on 2018/4/26.
 */

public class GetMsgModel implements GetMsgContract.IGetMsgModel {
    @Override
    public void getMsg(String phone, byte type, final IHttpCallBack<MessageResult> callBack) {
        MessageRequest messageRequest = initGetMsgReq(phone,type);
        String reqStr = new Gson().toJson(messageRequest);
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), reqStr);
        RetrofitManager.getRetrofit()
                .create(IHttpService.class)
                .getMsg(requestBody)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<MessageResult>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (e instanceof HttpException) {
                            HttpException httpException = (HttpException) e;
                            int code = httpException.code();
                            if (code == 500 || code == 404) {
                                callBack.onFail("服务器出错");
                            }
                        } else if (e instanceof ConnectException) {
                            callBack.onFail("网络断开,请打开网络!");
                        } else if (e instanceof SocketTimeoutException) {
                            callBack.onFail("网络连接超时!!");
                        } else {
                            callBack.onFail("发生未知错误" + e.getMessage());
                        }
                    }

                    @Override
                    public void onNext(MessageResult messageResult) {
                        callBack.onSuccess(messageResult);
                    }
                });
    }

    private MessageRequest initGetMsgReq(String phone,Byte type){
        MessageRequest msgReq = new MessageRequest();
        msgReq.setAccountPhone(phone);
        msgReq.setType(type);
        return msgReq;
    }
}
