package com.harmazing.intelligentpow.UI;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.harmazing.intelligentpow.R;
import com.harmazing.intelligentpow.tools.API;
import com.harmazing.intelligentpow.tools.AppConfig;
import com.harmazing.intelligentpow.tools.HttpHead;
import com.harmazing.intelligentpow.tools.HttpUtil;
import com.harmazing.intelligentpow.tools.LogUtil;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

public class AddDeviceActivity extends Activity implements View.OnClickListener{
    Button btnConfirm;
    ImageView btnBack;
    EditText editText;
    RelativeLayout layoutScan;
    private final static int SCANNIN_GREQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_device);
        initView();
        setClick();
    }

    private void setClick() {
        btnConfirm.setOnClickListener(this);
        layoutScan.setOnClickListener(this);
        btnBack.setOnClickListener(this);
    }


    private void initView() {
        btnConfirm = (Button) findViewById(R.id.confirm);
        editText = (EditText) findViewById(R.id.editxt_shou);
        layoutScan = (RelativeLayout) findViewById(R.id.layout_add);
        btnBack = (ImageView) findViewById(R.id.btn_back);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.confirm:
                if (!isWhrited()){
                    postData();
                }
                else
                    Toast.makeText(getApplicationContext(),"输入的设备序列号不能为空！",Toast.LENGTH_LONG).show();
                break;
            case R.id.layout_add:
                Intent intent = new Intent();
                intent.setClass(this, MipcaActivityCapture.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
                break;
            case R.id.btn_back:
                finish();
                break;
        }

    }

    private void postData() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在打开网络。。。。");
        RequestParams params = new RequestParams();
        params.put("usrId", AppConfig.getInstance().getUserId());
        params.put("deviceMac",editText.getText().toString());
        LogUtil.v("PARAM", params.toString());
        HttpUtil.post(HttpHead.head+ API.BIND_NET_GATE,params,new JsonHttpResponseHandler(){
            @Override
            public void onStart() {
                if (!isFinishing()){
                    progressDialog.show();
                }
                super.onStart();
            }

            @Override
            public void onFinish() {
                if (!isFinishing()){
                    progressDialog.hide();
                }
                super.onFinish();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtil.v("deviceMacBindsucc", response.toString());
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                super.onSuccess(statusCode, headers, responseString);
                LogUtil.v("deviceMacBindsucc", responseString);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                LogUtil.v("deviceMacBindfail",statusCode+responseString);
                if (responseString.equals("ok"))
                    Toast.makeText(getApplicationContext(),"绑定成功",Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getApplicationContext(),responseString,Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean isWhrited() {
        return  editText.getText().toString().equals("");

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SCANNIN_GREQUEST_CODE:
                if(resultCode == Activity.RESULT_OK){
                    Bundle bundle = data.getExtras();
                    String result = bundle.getString("result");
                    editText.setText(result);
//                    Toast.makeText(getActivity(),result,Toast.LENGTH_LONG).show();
//                    mTextView.setText(bundle.getString("result"));
//                    mImageView.setImageBitmap((Bitmap) data.getParcelableExtra("bitmap"));
                }
                break;
        }
    }
}
