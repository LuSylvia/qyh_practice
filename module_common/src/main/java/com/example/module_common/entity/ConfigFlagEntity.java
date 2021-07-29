package com.example.module_common.entity;

public class ConfigFlagEntity extends ResponseEntity.Data {
    //1开启,0关闭
    public int status=1;
    //1,  vip 2,  红娘 3,  绑定手机 4,  绑定微信
    // 5， 是否有购买行为 6,  注册分享奖励是否获得 7,  视频认证通过 8,  红娘咨询呼入开启
    public int type=-1;

}
