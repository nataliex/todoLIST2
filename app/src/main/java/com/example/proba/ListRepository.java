package com.example.proba;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class ListRepository {
    private ListDAO mListDAO;

    ListRepository(Application app){
        mListDAO = ToDoListDatabase.getDatabase(app).listDAO();
    }
    ListRepository(ToDoListDatabase tdld) {mListDAO = tdld.listDAO();}


    static private class insertTaskAsyncTask extends AsyncTask<Task,Void,Void>{

        private ListDAO mListDAO;

        insertTaskAsyncTask(ListDAO mListDAO){
            this.mListDAO = mListDAO;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            mListDAO.insertTask(tasks[0]);
            return null;
        }
    }

    static private class receiveTaskAsyncTask extends AsyncTask<Integer,Void,Task>{
        private ListDAO mListDAO;

        receiveTaskAsyncTask(ListDAO mListDAO){
            this.mListDAO = mListDAO;
        }

        @Override
        protected Task doInBackground(Integer... integers) {
            return mListDAO.getTask(integers[0]).get(0);
        }
    }

    static private class receiveTasksAsyncTask extends AsyncTask<Void,Void,List<Task>>{

        private ListDAO mListDAO;

        receiveTasksAsyncTask(ListDAO mListDAO){
            this.mListDAO = mListDAO;
        }

        @Override
        protected List<Task> doInBackground(Void... Void) {
            return mListDAO.getTasks();
        }
    }

    static private class receiveTasksByIdAsyncTask extends AsyncTask<Integer,Void,List<Task>>{

        private ListDAO mListDAO;

        receiveTasksByIdAsyncTask(ListDAO mListDAO){
            this.mListDAO = mListDAO;
        }

        @Override
        protected List<Task> doInBackground(Integer... integers) {
            return mListDAO.getChildTasks(integers[0]);
        }
    }

    static private class deleteTaskAsyncTask extends AsyncTask<Task,Void,Void>{

        private ListDAO listDAO;

        deleteTaskAsyncTask(ListDAO listDAO){
            this.listDAO=listDAO;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            listDAO.delete(tasks[0].fromArrayListToStringQuerry());
            listDAO.delete(tasks[0]);
            return null;
        }
    }

    static private class receiveMarkTasksAsyncTask extends AsyncTask<Utility.StarNParent, Void, List<Task>>{

        private ListDAO mListDAO;

        receiveMarkTasksAsyncTask(ListDAO mListDAO){
            this.mListDAO = mListDAO;
        }

        @Override
        protected List<Task> doInBackground(Utility.StarNParent... snps) {
            return mListDAO.getMarkTasks(snps[0].mStar,snps[0].mParent);
        }
    }

    static private class receiveStatusTasksAsyncTask extends AsyncTask<Utility.CompleteNParent, Void, List<Task>>{

        private ListDAO mListDAO;

        receiveStatusTasksAsyncTask(ListDAO mListDAO){
            this.mListDAO = mListDAO;
        }

        @Override
        protected List<Task> doInBackground(Utility.CompleteNParent... cnps) {
            return mListDAO.getStatusTasks(cnps[0].mCompleted,cnps[0].mParent);
        }
    }

    static private class updateTaskAsyncTask extends AsyncTask<Task,Void,Void>{
        ListDAO mListDAO;

        updateTaskAsyncTask(ListDAO ListDAO){
            mListDAO = ListDAO;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            mListDAO.update(tasks[0]);
            return null;
        }
    }

    static private class receiveRootTasksAsyncTask extends AsyncTask<Void,Void,List<Task>>{
        ListDAO mListDAO;

        receiveRootTasksAsyncTask(ListDAO listDAO){
            mListDAO = listDAO;
        }

        @Override
        protected List<Task> doInBackground(Void... voids) {
            return mListDAO.getRootTasks();
        }
    }

    public void insertTasks(Task... tasks){
        insertTaskAsyncTask iTAT = new insertTaskAsyncTask(mListDAO);
        for(int i=0;i<tasks.length;i++){
            iTAT.execute(tasks[i]);
        }
    }

    public void insertTask(Task task){
        insertTaskAsyncTask iTAT = new insertTaskAsyncTask(mListDAO);
        iTAT.execute(task);
    }

    public void deleteTask(Task task){
        deleteTaskAsyncTask dTAT = new deleteTaskAsyncTask(mListDAO);
        try{
            dTAT.execute(task);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public List<Task> receiveObservableTask(Integer index){
        return mListDAO.getTask(index);
    }



    public Task receiveTask(Integer index){
        receiveTaskAsyncTask rTAT = new receiveTaskAsyncTask(mListDAO);
        try {
            return rTAT.execute(index).get();
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public List<Task> receiveChildTasks(Task task){
        receiveTasksByIdAsyncTask rTAT = new receiveTasksByIdAsyncTask(mListDAO);
        try {
            return rTAT.execute(task.getId()).get();
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public List<Task> receiveChildTasks(Integer id){
        receiveTasksByIdAsyncTask rTAT = new receiveTasksByIdAsyncTask(mListDAO);
        try {
            return rTAT.execute(id).get();
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public LiveData<List<Task>> receiveObservableChildTasks(Task task){
        return mListDAO.getObservableChildTasks(task.getId());
    }

    public LiveData<List<Task>> receiveObservableChildTasks(Integer taskId){
        return mListDAO.getObservableChildTasks(taskId);
    }

    public List<Task> receiveAllTasks(){
        receiveTasksAsyncTask rTAT = new receiveTasksAsyncTask(mListDAO);
        try {
            return rTAT.execute().get();
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public LiveData<List<Task>> receiveObservableAllTasks(){
        return mListDAO.getObservableTasks();
    }

    public List<Task> receiveStatusTasks(Boolean status,Integer mParent){
        Utility utility = new Utility();
        Utility.CompleteNParent cnp = utility.new CompleteNParent(status,mParent);
        receiveStatusTasksAsyncTask rSTAT = new receiveStatusTasksAsyncTask(mListDAO);
        try {
            return rSTAT.execute(cnp).get();
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public LiveData<List<Task>> receiveObservableStatusTasks(Boolean status,Integer pId){
        return mListDAO.getObservableStatusTasks(status,pId);
    }

    public List<Task> receiveMarkTasks(Boolean mark,Integer mParent){
        Utility utility = new Utility();
        Utility.StarNParent snp = utility.new StarNParent(mark,mParent);
        receiveMarkTasksAsyncTask rMTAT = new receiveMarkTasksAsyncTask(mListDAO);
        try {
            return rMTAT.execute(snp).get();
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public LiveData<List<Task>> receiveObservableMarkTasks(Boolean mark,Integer pId){
        return mListDAO.getObservableMarkTasks(mark,pId);
    }

    public List<Task> receiveRootTasks(){
        receiveRootTasksAsyncTask rRTAT = new receiveRootTasksAsyncTask(mListDAO);
        try {
            return rRTAT.execute().get();
        } catch (Exception e) {
            e.printStackTrace();
        } return null;
    }

    public LiveData<List<Task>> receiveObservableRootTasks(){
        return mListDAO.getObservableRootTasks();
    }

    public void updateTask(Task task){
        updateTaskAsyncTask uTAT = new updateTaskAsyncTask(mListDAO);
        uTAT.execute(task);//TODO возможно асинхронное обращение к БД в main потоке!!!
    }
}