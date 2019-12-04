package com.example.proba;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.regex.Pattern;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@SuppressWarnings("WeakerAccess")
@Entity(tableName =  "TaskTable")
public class Task{

    @ColumnInfo(name = "Id")
    @NonNull
    @PrimaryKey(autoGenerate = true)
    private Integer mId;

    @ColumnInfo(name = "starMark")
    private Boolean mStarMark;

    @ColumnInfo(name = "Name")
    private String mName;

    @ColumnInfo(name = "Description")
    private String mDescription;

    @ColumnInfo(name = "ParentId")
    private Integer mParentId;

    @ColumnInfo(name = "isDone")
    private Boolean mIsDone;


    @ColumnInfo(name = "Path")
    @TypeConverters({ArrayListConverter.class})
    private ArrayList<Integer> mPath;


    @Ignore
    Task(String name,String description,boolean isDone,Integer Id, boolean StarMark){
        mName = name;
        mDescription = description;
        mIsDone = isDone;
        mId = Id;
        mPath = new ArrayList<Integer>();
        mStarMark = StarMark;
    }
    @Ignore
    Task(String name,String description,boolean isDone, boolean StarMark){
        mName = name;
        mDescription = description;
        mIsDone = isDone;
        mPath = new ArrayList<Integer>();
        mStarMark = StarMark;
    }
    Task(String mName,Integer mParentId,Boolean mIsDone,
         String mDescription,ArrayList<Integer> mPath, Integer mId,Boolean mStarMark){
        this.mName = mName;
        this.mId = mId;
        this.mDescription = mDescription;
        this.mParentId = mParentId;
        this.mIsDone = mIsDone;
        this.mPath = new ArrayList<Integer>();
        this.mPath.addAll(mPath);
        this.mStarMark = mStarMark;
    }



    public String fromArrayListToStringQuerry(){
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<mPath.size();i++){
            sb.append(mPath.get(i));
            if(i!=mPath.size()-1) sb.append('.');
        }
        sb.append(".%");
        return sb.toString();
    }

    public Map<String,Object> getMapRepresentation(){
        Map<String, Object> myMap = new HashMap<>();
        myMap.put("Id",mId);
        myMap.put("starMark", mStarMark);
        myMap.put("Name",mName);
        myMap.put("Description", mDescription);
        myMap.put("ParentId", mParentId);
        myMap.put("isDone",mIsDone);
        myMap.put("Path",mPath);
        return myMap;
    }

    public ArrayList<Integer> getPath(){
        return mPath;
    }

    public Integer getId(){
        return mId;
    }

    public String getName(){
        return mName;
    }

    public Integer getParentId(){
        return mParentId;
    }

    public String getDescription() {
        return mDescription;
    }

    public Boolean getIsDone() {
        return mIsDone;
    }

    public Boolean getStarMark(){return mStarMark;}

    public void setPath(ArrayList<Integer> mPath) {
        this.mPath.addAll(mPath);
    }

    public void setId(Integer mId) {
        this.mId = mId;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public void setParentId(Integer mParentId) {
        this.mParentId = mParentId;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public void setIsDone(Boolean mIsDone) {
        this.mIsDone = mIsDone;
    }

    public void setStarMark(Boolean starMark){
        this.mStarMark = starMark;
    }

    public static class ArrayListConverter{
        @TypeConverter
        public static ArrayList<Integer> fromStringToArrayList(String string){
            ArrayList<Integer> arrayList = new ArrayList<Integer>();
            String[] prepStr;
            if(string.contains(".")) {
                prepStr = string.split(Pattern.quote("."));
                for (int i = 0; i < string.split(Pattern.quote(".")).length; i++) {
                    arrayList.add(Integer.parseInt(prepStr[i]));
                }
            } else arrayList.add(Integer.parseInt(string));
            return arrayList;
        }

        @TypeConverter
        public static String fromArrayListToString(ArrayList<Integer> arrayList){
            StringBuilder sb = new StringBuilder();
            for(int i=0;i<arrayList.size();i++){
                sb.append(arrayList.get(i));
                if(i!=arrayList.size()-1) sb.append('.');
            }
            Log.d("StringBuilder",sb.toString());
            return sb.toString();
        }
    }
}