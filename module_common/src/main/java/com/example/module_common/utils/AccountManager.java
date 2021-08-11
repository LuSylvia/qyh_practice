package com.example.module_common.utils;

import com.example.module_common.entity.AppConfigEntity;
import com.tencent.mmkv.MMKV;

/**
 * 账号工具类
 */
public class AccountManager {
    private boolean hasLogined=false;
    private AppConfigEntity appConfigEntity;
    private static MMKV kv;


    private AccountManager(){}

    private static class Singleton{
        public static AccountManager accountManager =new AccountManager();
    }

    public static AccountManager getInstance(){
        kv=MMKV.defaultMMKV();
        return Singleton.accountManager;
    }

    /**
     * 保存账号信息
     * @param userId 手机号
     */
    public void saveAccount(String userId){
        kv.putString("userId",userId);
        this.hasLogined=true;
    }

    /**
     * 读取账号
     * @return 用户手机号
     */
    public String loadUserId(){
        String userId=kv.decodeString("userId",null);
        return userId;
    }



    /**
     * 该用户是否登录过
     * @return
     */
    public  boolean hasLogined(){
        loadUserId();
        if(loadUserId() !=null){
            hasLogined=true;
        }else{
            hasLogined=false;
        }
        return hasLogined;
    }

    public void setAppConfigEntity(AppConfigEntity appConfigEntity) {
        this.appConfigEntity = appConfigEntity;
    }
}
