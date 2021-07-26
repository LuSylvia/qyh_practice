package com.example.module_common.netService;

import android.os.Environment;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class RetrofitManager {

    private static RetrofitManager retrofitManager;
    private OkHttpClient okHttpClient;
    private Retrofit retrofit;
    private RetrofitApiService retrofitApiService;

    private RetrofitManager() {
        initOkHttpClient();
        initRetrofit();
    }


    public static RetrofitManager getInstance() {
        if (retrofitManager == null) {
            synchronized (RetrofitManager.class) {
                if (retrofitManager == null) {
                    retrofitManager = new RetrofitManager();
                }
            }
        }
        return retrofitManager;
    }

    public static RetrofitApiService getApiService() {
        return getInstance().retrofitApiService;
    }

    private void initOkHttpClient() {
        okHttpClient = new OkHttpClient.Builder()
                //设置缓存文件路径，和文件大小
                .cache(new Cache(new File(Environment.getExternalStorageDirectory() + "/okhttp_cache/"), 50 * 1024 * 1024))
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();

    }


    private void initRetrofit() {
        retrofit = new Retrofit.Builder().baseUrl(SystemHelper.getInstance().getApiUrl())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitApiService = retrofit.create(RetrofitApiService.class);
    }


}
