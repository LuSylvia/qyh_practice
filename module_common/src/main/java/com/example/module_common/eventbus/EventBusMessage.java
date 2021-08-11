package com.example.module_common.eventbus;

/**
 * Event发送消息的实体类
 */
public class EventBusMessage {
    private  String userID;
    private int workcity;

    public EventBusMessage(String userID) {
        this.userID = userID;
    }

    public EventBusMessage(int workcity){
        this.workcity=workcity;
    }

    public void setUserID(String userID){
        this.userID=userID;
    }

    public String getUserID(){
        return userID;
    }

    public int getWorkcity() {
        return workcity;
    }

    public void setWorkcity(int workcity) {
        this.workcity = workcity;
    }
}
