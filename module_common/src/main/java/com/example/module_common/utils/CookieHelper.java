package com.example.module_common.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.tencent.mmkv.MMKV;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

public class CookieHelper {
    private static final String MMKV_COOKIE_KEY = "QyhPracticeCookie";
    private volatile ConcurrentHashMap<String, ArrayList<Cookie>> cookieStore = new ConcurrentHashMap();
    private MMKV kv;
    private volatile boolean isDiskEmpty = false;


    private CookieHelper() {
        kv = MMKV.defaultMMKV();
    }

    public static CookieHelper getInstance() {
        return Singleton.instance;
    }


    public void clearCookie(){
        this.cookieStore.clear();
        try {
            kv.putString(MMKV_COOKIE_KEY, "");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 将Cookies保存到本地
     *
     * @param url
     * @param cookies
     */
    public synchronized void saveCookies(HttpUrl url, List<Cookie> cookies) {
        String domain = getDomain(url);
        //保存用来存本地的最终cookie（包含原有cookie和新cookie，并且必定不存在重复的cookie)
        ArrayList<Cookie> cookiesResult = new ArrayList<>(cookies);
        //先从本地取出该domain对应的cookie（如果有的话）
        List<Cookie> cookiesLocal = this.cookieStore.get(domain);

        if (cookiesLocal != null) {
            //等会用于从cookieResult中删除所有重复的cookies，只保留新增的cookie
            List<Cookie> cookiesSame = new ArrayList();
            Iterator it1 = cookiesLocal.iterator();

            while (true) {
                while (it1.hasNext()) {
                    Cookie cookieLocal = (Cookie) it1.next();
                    Iterator it2 = cookies.iterator();

                    while (it2.hasNext()) {
                        Cookie cookie = (Cookie) it2.next();
                        //两个cookie相同，保存到cookiesSame中，准备删除
                        if (cookie.name() != null && cookieLocal.name() != null && cookie.name().equals(cookieLocal.name())) {
                            cookiesSame.add(cookie);
                        }
                    }
                }
                //如果存在相同的cookies，从result中将其删除
                if (!cookiesSame.isEmpty()) {
                    cookiesResult.removeAll(cookiesSame);
                }
                //已有cookie放入result中
                cookiesResult.addAll(cookiesLocal);
                break;
            }
            //遍历arraylist，删除所有空白cookie
            for (int i = cookiesResult.size() - 1; i >= 0; --i) {
                Cookie temp = (Cookie) cookiesResult.get(i);
                String value = temp.value();
                if (value == null || value.isEmpty()) {
                    cookiesResult.remove(temp);
                }
            }


        }
        //存放到cookieStore中
        this.cookieStore.put(domain, cookiesResult);
        //存放到磁盘里
        this.saveCookiedtoDisk();

    }

    /**
     * 对外暴露接口，用来读取List<Cookie>
     *
     * @param url
     * @return
     */
    public List<Cookie> loadCookieByUrl(@NotNull HttpUrl url) {
        loadCookieStoreFromDiskIfNeed();
        try {
            ArrayList<Cookie> cookies = this.cookieStore.get(getDomain(url));
            if (cookies != null) {
                return cookies;
            } else {
                return new ArrayList<>();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private String getDomain(HttpUrl url) {
        if (url.topPrivateDomain() != null) {
            //return url.host();
            return url.host() == null ? "" : url.host();
        } else {
            return "";
        }
    }

    private void loadCookieStoreFromDiskIfNeed() {
        if (this.cookieStore.isEmpty() && !this.isDiskEmpty) {
            ConcurrentHashMap<String, ArrayList<Cookie>> map = this.loadCookieStoreFromDisk();
            if (map != null) {
                this.cookieStore = map;
            }
        }
    }

    /**
     * 用于从磁盘读取序列化后的ConcurrentHashMap<String, ArrayList<Cookie>> cookieStore
     */
    private ConcurrentHashMap<String, ArrayList<Cookie>> loadCookieStoreFromDisk() {
        try {
            //从本地读
            ConcurrentHashMap<String, ArrayList<Cookie>> result = CookieHelper.Data.fromJson(kv.decodeString(MMKV_COOKIE_KEY, null));
            if (result == null) {
                this.isDiskEmpty = true;
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将ConcurrentHashMap<String, ArrayList<Cookie>> cookieStore序列化后存到本地
     */
    private void saveCookiedtoDisk() {
        try {
            String json = (new CookieHelper.Data(this.cookieStore)).toJson();
            Log.d("CookieHelper", "json是" + json);
            //存本地
            kv.putString(MMKV_COOKIE_KEY, json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class Singleton {
        public static CookieHelper instance = new CookieHelper();
    }

    private static class Data implements Serializable {
        ConcurrentHashMap<String, ArrayList<Cookie>> cookieStore;

        private Data(ConcurrentHashMap<String, ArrayList<Cookie>> cookieStore) {
            this.cookieStore = cookieStore;
        }

        private static ConcurrentHashMap<String, ArrayList<Cookie>> fromJson(String json) {
            if (json == null) {
                return null;
            } else {
                try {
                    ConcurrentHashMap<String, ArrayList<Cookie>> cookieStore = (new Gson()).fromJson(json, Data.class).cookieStore;
                    if (cookieStore != null && !cookieStore.isEmpty()) {
                        Iterator it1 = cookieStore.values().iterator();
                        while (true) {
                            ArrayList list;
                            do {
                                do {
                                    if (!it1.hasNext()) {
                                        return cookieStore;
                                    }
                                    list = (ArrayList) it1.next();
                                } while (list == null);
                            } while (list.isEmpty());

                            Iterator it2 = list.iterator();

                            while (it2.hasNext()) {
                                Object ob = it2.next();
                                if (!(ob instanceof Cookie)) {
                                    return null;
                                }
                            }
                        }

                    } else {
                        return cookieStore;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }


        }

        private String toJson() {
            return (new Gson()).toJson(this);
        }

    }


}
