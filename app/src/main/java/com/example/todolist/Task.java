package com.example.todolist;

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

    @ColumnInfo(name = "deadline")
    @TypeConverters({DateConverter.class})
    private Date mDeadline;

    @ColumnInfo(name = "Path")
    @TypeConverters({ArrayListConverter.class})
    private ArrayList<Integer> mPath;

    @Ignore
<<<<<<< HEAD
    Task(String name,String description,Date deadline,boolean isDone,Integer Id){
=======
    Task(String name,String description,Date deadline,boolean isDone,Integer Id, boolean StarMark){
>>>>>>> FirstCommit by Ruslan, edited xml
        mName = name;
        mDescription = description;
        mDeadline = deadline;
        mIsDone = isDone;
        mId = Id;
        mPath = new ArrayList<Integer>();
<<<<<<< HEAD
=======
        mStarMark = StarMark;
>>>>>>> FirstCommit by Ruslan, edited xml
    }

    Task(String mName,Integer mParentId,Boolean mIsDone,Date mDeadline,
         String mDescription,ArrayList<Integer> mPath,Integer mId,Boolean mStarMark){
        this.mName = mName;
        this.mId = mId;
        this.mDescription = mDescription;
        this.mParentId = mParentId;
        this.mIsDone = mIsDone;
        this.mDeadline = mDeadline;
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

    public Date getDeadline() {
        return mDeadline;
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

    public void setDeadline(Date mDeadline) {
        this.mDeadline = mDeadline;
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
        public ArrayList<Integer> fromStringToArrayList(String string){
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        String[] prepStr = string.split(".");
        for(int i=0;i<prepStr.length;i++){
            arrayList.add(Integer.getInteger(prepStr[i]));
        }
        return arrayList;
    }

        @TypeConverter
        public String fromArrayListToString(ArrayList<Integer> arrayList){
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<arrayList.size();i++){
            sb.append(arrayList.get(i));
            if(i!=arrayList.size()-1) sb.append('.');
        }
        return sb.toString();
    }
}

    public static class DateConverter{
        static final Long Indefinitely = Long.valueOf(-1);

        @TypeConverter
        public Long fromDateToLong(Date date){
            if (date!=null)
                return date.getTime();
            else
                return Indefinitely;
        }

        @TypeConverter
        public Date fromLongToDate(Long wide){
            if (wide!=-1)
                return new Date(wide);
            else
                return null;
        }
    }
}