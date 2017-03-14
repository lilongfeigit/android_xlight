package com.umarbhutta.xlightcompanion.okHttp;

import com.umarbhutta.xlightcompanion.Tools.Logger;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by guangbinw on 2017/3/12.
 */
public abstract class BaseHttp {
    /**
     * 错误结果
     *
     * @param errResult
     */
    public abstract void okOnError(String errResult);

    /**
     * 服务器返回数据
     *
     * @param result
     */
    public abstract void okOnResponse(String result);


    /**
     * get方式获取数据
     *
     * @param url
     */
    protected void getData(String url) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = okHttpClient.newCall(request);


        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                okOnError(call.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                okOnResponse(response.body().string());
            }
        });
    }

    /**
     * post方式请求数据
     *
     * @param url
     * @param jsonParam
     */
    protected void postData(String url, String jsonParam) {
        OkHttpClient okHttpClient = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, jsonParam);

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        Call call = okHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                okOnError(call.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Logger.i("result = " + result);
                okOnResponse(result);
            }
        });
    }

    /**
     * put请求
     *
     * @param url
     * @param jsonParam
     */
    protected void putData(String url, String jsonParam) {
        OkHttpClient okHttpClient = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, jsonParam);

        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .build();

        Call call = okHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                okOnError(call.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Logger.i("result = " + result);
                okOnResponse(result);
            }
        });
    }

    /**
     * Delete请求
     *
     * @param url
     * @param jsonParam
     */
    protected void deleteData(String url, String jsonParam) {
        OkHttpClient okHttpClient = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, jsonParam);

        Request request = new Request.Builder()
                .url(url)
                .delete(body)
                .build();

        Call call = okHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                okOnError(call.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Logger.i("result = " + result);
                okOnResponse(result);
            }
        });


    }

}
