package com.qiuweixin.veface.mvp.bean;

import android.os.Parcel;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;


/**
 * Created by Allen Lake on 2015/12/1 0001.
 */
public class ChannelInfo implements Serializable {

    @JSONField(name = "id")
    private int id;
    @JSONField(name = "name")
    private String name;
    @JSONField(name = "thumb")
    private String thumb;

    public ChannelInfo(){

    }

    public ChannelInfo(int id,String name,String thumb) {
        this.id = id;
        this.name = name;
        this.thumb = thumb;
    }

    protected ChannelInfo(Parcel in) {
        id = in.readInt();
        name = in.readString();
        thumb = in.readString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }
}
