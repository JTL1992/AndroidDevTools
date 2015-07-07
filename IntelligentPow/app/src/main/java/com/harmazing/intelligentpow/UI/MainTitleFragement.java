package com.harmazing.intelligentpow.UI;



import android.nfc.Tag;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.harmazing.intelligentpow.R;
import com.harmazing.intelligentpow.model.Weather;
import com.harmazing.intelligentpow.tools.API;
import com.harmazing.intelligentpow.tools.AppConfig;
import com.harmazing.intelligentpow.tools.GsonUtil;
import com.harmazing.intelligentpow.tools.HttpHead;
import com.harmazing.intelligentpow.tools.HttpUtil;
import com.harmazing.intelligentpow.tools.LogUtil;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link android.app.Fragment} subclass.
 *主界面的Fragment
 */
public class MainTitleFragement extends android.support.v4.app.Fragment {
    private TextView name, mobile,insideTemp,city,weather;//侧滑栏中的姓名和电话
    int temp;
    ImageView imgWeather;
    Weather myWeather;
    RelativeLayout titleLayout;
    public MainTitleFragement() {
}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getActivity().getActionBar() != null)
        getActivity().getActionBar().setLogo(getResources().getDrawable(R.drawable.icon_person));        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_aty_fragement,null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        insideTemp = (TextView) getActivity().findViewById(R.id.temp);
        city = (TextView) getActivity().findViewById(R.id.city);
        weather = (TextView)getActivity().findViewById(R.id.whether);
        name = (TextView) getActivity().findViewById(R.id.key);
        mobile = (TextView) getActivity().findViewById(R.id.num);
        imgWeather = (ImageView) getActivity().findViewById(R.id.icon_wehther);
        titleLayout = (RelativeLayout) getActivity().findViewById(R.id.title_layout);
        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle != null){
            String nameString = bundle.getString("name");
            AppConfig.getInstance().setName(nameString);
            String mobileString = bundle.getString("mobile");
            AppConfig.getInstance().setMobil(mobileString);
          name.setText(nameString);
          mobile.setText(mobileString);
        }
        else{
            name.setText(AppConfig.getInstance().getName());
            mobile.setText(AppConfig.getInstance().getMobil());
        }
       getWeatherInfo();
//        RequestParams params = new RequestParams();
//        params.add("userId", AppConfig.getInstance().getUserId());
//        HttpUtil.get(HttpHead.head + API.GET_USER_DEVICE_LIST, params,new JsonHttpResponseHandler(){
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
//                super.onSuccess(statusCode, headers, response);
//                LogUtil.v("MainTitle", response.toString());
//                if (!response.toString().equals("[]")){
//                    for (int i = 0; i < response.length(); i++){
//                        try {
//                            if (response.getJSONObject(i).getInt("type") == 2)
//                             temp = response.getJSONObject(i).getInt("temp");
//                             insideTemp.setText(temp+"°c");
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }
//        });
    }
//
    private void getWeatherInfo() {
        HttpUtil.get(HttpHead.head+API.WEATHER_INFO,new JsonHttpResponseHandler(){
            JSONObject weatherInfo;
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    LogUtil.v("TITLE RESPONSE",response.toString());
                    weatherInfo = response.getJSONObject("weatherinfo");
                    //myWeather = new Weather();
                    myWeather = GsonUtil.json2Bean(weatherInfo.toString(),Weather.class);
                    setWeather(myWeather);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(),"地区获取失败，无法加载当前天气。",Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private void setWeather(Weather myWeather) {
        weather.setText(myWeather.getWeather());
        insideTemp.setText(myWeather.getTemp1());
        city.setText(myWeather.getCity());
       // setPicture(myWeather);
    }

    private void setPicture(Weather myWeather) {

    }


}

