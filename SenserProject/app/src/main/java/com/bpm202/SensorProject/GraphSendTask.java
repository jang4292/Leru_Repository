package com.bpm202.SensorProject;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.bpm202.SensorProject.Account.AccountManager;
import com.bpm202.SensorProject.Common.AppPreferences;
import com.bpm202.SensorProject.ValueObject.PersonalInfoObj;
import com.bpm202.SensorProject.db.DBUser;
import com.bpm202.SensorProject.db.SendGrapthData;
import com.bpm202.SensorProject.db.SendScheduleData;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GraphSendTask extends AsyncTask<SendScheduleData, Void, Message> {
    private final String SEND_URL = "http://106.240.7.187:7198/Health/Health/api/App_plan";

    private Handler mHandler;
    private Context mCon;
    private Boolean isTest = true;

    public GraphSendTask(Context con, Handler handler) {
        mCon = con;
        mHandler = handler;
    }

    private int getCurrentYear(int age) {
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());
        String year = yearFormat.format(currentTime);
        return (Integer.parseInt(year) - age) + 1;
    }

    @Override
    protected Message doInBackground(SendScheduleData... urls) {
        Message msg = new Message();
        PersonalInfoObj saveInfo = getUserDB();

        SendGrapthData data = new SendGrapthData();
        data.nickname = saveInfo.getNickname();
        data.email = saveInfo.getEmail();
        data.height = saveInfo.getHeight();
        data.weight = saveInfo.getWeight();
        data.gender = saveInfo.getGender();
        data.age = getCurrentYear(saveInfo.getAge());
        data.regionStr = saveInfo.getRegionStr();

        for (SendScheduleData scheduleData : urls) {
            data.list.add(scheduleData);
        }

        Log.e("data", data.toJson());


        String result = null;
        try {
            HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, 10000);
            HttpConnectionParams.setSoTimeout(httpParameters, 7000);
            DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
            HttpPost httppost = new HttpPost(SEND_URL);
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("nick", saveInfo.getNickname()));
            nameValuePairs.add(new BasicNameValuePair("data", data.toJson()));
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8);
            httppost.setEntity(entity);

            HttpResponse response = httpClient.execute(httppost);
            StatusLine status = response.getStatusLine();
            HttpEntity httpentity = response.getEntity();
            InputStream in = httpentity.getContent();

            if (status.getStatusCode() == 200) {
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String line = "";
                StringBuffer source = new StringBuffer();

                while ((line = br.readLine()) != null) {
                    source.append(line + "\n");
                }
                result = source.toString();
            } else {
                result = String.valueOf(status.getStatusCode());
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        msg.arg1 = 1;
//        msg.obj = result;
        msg.obj = isTest ? data.toJson() : result;
        return msg;
    }

    @Override
    protected void onPostExecute(Message msg) {

        if (isTest) {
            String str = (String) msg.obj;
            Toast.makeText(mCon, str, Toast.LENGTH_LONG).show();
        }
        mHandler.sendMessage(msg);
    }


    private PersonalInfoObj getUserDB() {
        PersonalInfoObj saveInfo = new PersonalInfoObj();
        DBUser db = new DBUser(mCon);
        db.open();
//        ArrayList<PersonalInfoObj> lists =
//                db.selectDB(AccountManager.Instance().getPersonalInfoObj());

        String token = new AppPreferences(mCon).getStringPref(AppPreferences.KEY_TOKEN);
        ArrayList<PersonalInfoObj> lists = db.selectTokenDB(token);
        if (!lists.isEmpty()) {
            saveInfo = lists.get(0);
        }
        db.close();
        return saveInfo;
    }

    private void saveUserDB() {
        PersonalInfoObj obj = AccountManager.Instance().getPersonalInfoObj();
        obj.setEmail("skystony@gmail.com");
        obj.setRegionStr("대구 달서구");
        DBUser db = new DBUser(mCon);
        db.open();
        ArrayList<PersonalInfoObj> lists = db.selectDB(obj);
        if (lists.isEmpty()) {
            db.insertDB(obj);
        }
        db.close();
    }

}
