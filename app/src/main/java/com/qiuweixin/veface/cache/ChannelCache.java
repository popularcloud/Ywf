package com.qiuweixin.veface.cache;

import android.os.Environment;
import android.util.Log;

import com.qiuweixin.veface.mvp.model.ChannelInfo;
import com.qiuweixin.veface.util.PreferenceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.util.ArrayList;

/**
 * Created by Allen Lake on 2015/12/2 0002.
 */
public class ChannelCache {

    private static FileOutputStream fileOutputStream;
    private static ObjectOutputStream objectOutputStream;
    private static FileInputStream fileInputStream;
    private static ObjectInputStream objectInputStream;


    public static ArrayList<ChannelInfo> getSelChannelInfoList() {
        //取出数据
        ArrayList<ChannelInfo> savedArrayList = null;
        try {
            fileInputStream = new FileInputStream(Environment.getExternalStorageDirectory().toString() + File.separator + PreferenceUtils.Channel.SELECTCHANNEL+".txt");
            objectInputStream = new ObjectInputStream(fileInputStream);
            savedArrayList =(ArrayList<ChannelInfo>) objectInputStream.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (OptionalDataException e) {
            e.printStackTrace();
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return savedArrayList;
    }

    public static ArrayList<ChannelInfo> getNotSelChannelInfoList() {
        ArrayList<ChannelInfo> savedArrayList = null;
        try {
            fileInputStream = new FileInputStream(Environment.getExternalStorageDirectory().toString() + File.separator +PreferenceUtils.Channel.NOTSELECTCHANNEL+".txt");
            objectInputStream = new ObjectInputStream(fileInputStream);
            savedArrayList =(ArrayList<ChannelInfo>) objectInputStream.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (OptionalDataException e) {
            e.printStackTrace();
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return savedArrayList;
    }


    public static void saveSelChannelInfoList(ArrayList<ChannelInfo> selChannelInfoList) {
       // storage.put(PreferencesKey.Channels.SELECTED_CHANNEL, selChannelInfoList);

        try {
        //存入数据
        File file = new File(Environment.getExternalStorageDirectory().toString() + File.separator +PreferenceUtils.Channel.SELECTCHANNEL+".txt");
            Log.d("DiscoveryFragment","频道数据存储路径1="+file.getPath());
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        if (!file.exists()) {
            file.createNewFile();
        }
            fileOutputStream= new FileOutputStream(file.toString());
            objectOutputStream= new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(selChannelInfoList);
        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    public static void saveNotSelChannelInfoList(ArrayList<ChannelInfo> notSelChannelInfoListJson) {
        //storage.put(PreferencesKey.Channels.NOTSELECTED_CHANNEL, notSelChannelInfoListJson);
        try {
            //存入数据
            File file = new File(Environment.getExternalStorageDirectory().toString() + File.separator +PreferenceUtils.Channel.NOTSELECTCHANNEL+".txt");
            Log.d("DiscoveryFragment","频道数据存储路径="+file.getPath());
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            if (!file.exists()) {
                file.createNewFile();
            }
            fileOutputStream= new FileOutputStream(file.toString());
            objectOutputStream= new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(notSelChannelInfoListJson);
        } catch (Exception e) {
            // TODO: handle exception
        }

    }
}
