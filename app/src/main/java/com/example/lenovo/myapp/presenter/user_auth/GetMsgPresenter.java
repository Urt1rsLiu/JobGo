package com.example.lenovo.myapp.presenter.user_auth;

import android.util.Log;

import com.example.lenovo.myapp.contract.user_auth.GetMsgContract;
import com.example.lenovo.myapp.entity.response.result.MessageResult;
import com.example.lenovo.myapp.model.inf.IHttpCallBack;
import com.example.lenovo.myapp.model.user_auth.GetMsgModel;

/**
 * Created by lenovo on 2018/4/26.
 */

public class GetMsgPresenter implements GetMsgContract.IGetMsgPresenter {
    private GetMsgContract.IGetMsgModel mIGetMsgModel;
    private GetMsgContract.IGetMsgView mIGetMsgView;

    public GetMsgPresenter(GetMsgContract.IGetMsgView mIGetMsgView){
        this.mIGetMsgView = mIGetMsgView;
        this.mIGetMsgModel = new GetMsgModel();
    }

    @Override
    public void getMsg(String phone, byte type) {
        mIGetMsgModel.getMsg(phone, type, new IHttpCallBack<MessageResult>() {
            @Override
            public void onSuccess(MessageResult messageResult) {
                mIGetMsgView.showGetMsg(messageResult);
            }

            @Override
            public void onFail(String errorMsg) {
                Log.i("###getMsg",errorMsg);
            }
        });
    }

    @Override
    public void onDestroy() {
        mIGetMsgView = null;
    }
}
