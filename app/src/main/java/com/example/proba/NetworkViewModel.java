package com.example.proba;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class NetworkViewModel {
    ListRepository lr;
    ExecutorService mExecutor;
    final FirebaseFirestore dataBase;
    ArrayList<Task> stack;
    TreeMap<Integer,Integer> oldIdsToNew;
    TreeMap<Integer,ArrayList<Integer>> oldIdsToPaths;
    TreeMap<Integer, DocumentReference> idsToDocs;
    Context mContext;
    NetworkViewModel(Application application){
        ToDoListDatabase tdld = ToDoListDatabase.getDatabase(application);
        lr = new ListRepository(tdld);
        mExecutor = Executors.newFixedThreadPool(1);
        dataBase = FirebaseFirestore.getInstance();
        mContext = application.getApplicationContext();
    }

    public void addIfNotExistsTree(final String name,final Task chosenTask){
        dataBase.collection(name).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(queryDocumentSnapshots.getDocuments().size()!=0){
                    Log.d("Такой документ","уже создан");
                }else{
                    mExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            addTree(name,chosenTask);
                        }
                    });

                }
            }
        });
    }

    private void fillStack(String name,Task task){
        stack = null;
        stack = new ArrayList<Task>();
        stack.add(task);
        int i=0;
        while(stack.size()>i){
            for (Task t:lr.receiveChildTasks(stack.get(i))) {
                stack.add(t);
            }
            i++;
        }
    }

    private void addTree(String name,final Task task){
        fillStack(name,task);
        idsToDocs=null;
        idsToDocs = new TreeMap<>();
        int i=0;
        dataBase.collection(name);
        idsToDocs.put(task.getId(),dataBase.collection(name).document(Task.ArrayListConverter.
                fromArrayListToString(task.getPath())));
        idsToDocs.get(task.getId()).set(task.getMapRepresentation()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> gmsTask) {
                idsToDocs.get(task.getId()).collection(Task.ArrayListConverter.fromArrayListToString(task.getPath()));
                buildTree(1);
            }
        });
    }

    private void buildTree(final Integer current){
        if(current<stack.size()){
            final Task currentTask = stack.get(current);
            Task parentTask = lr.receiveTask(currentTask.getParentId());
            idsToDocs.put(currentTask.getId(),idsToDocs.get(currentTask.getParentId()).collection(Task.ArrayListConverter.
                    fromArrayListToString(parentTask.getPath())).document(Task.ArrayListConverter.
                    fromArrayListToString(currentTask.getPath())));
            idsToDocs.get(currentTask.getId()).set(currentTask.getMapRepresentation()).
                    addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                            idsToDocs.get(currentTask.getId()).
                                    collection(Task.ArrayListConverter.fromArrayListToString(currentTask.getPath()));
                            int tempCurrent = current+1;
                            buildTree(tempCurrent);
                        }
                    });
        }
    }

    public void getIfExistsTree(final String name){
        dataBase.collection(name).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(queryDocumentSnapshots.getDocuments().size()==0){
                    Log.d("Такой документ","не существует");
                }else{
                    oldIdsToNew = new TreeMap<>();
                    oldIdsToPaths = new TreeMap<>();
                    mExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            getTree(dataBase.collection(name));
                        }
                    });
                }
            }
        });
    }

    private void getTree(CollectionReference collectionReference){
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<QuerySnapshot> task) {
                if(task.getResult().size()!=0) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        addToDB(document);
                        getTree(document.getReference().collection(Task.ArrayListConverter.
                                fromArrayListToString((ArrayList<Integer>) document.getData().get("Path"))));
                    }
                }
            }
        });
    }

    private void addToDB(final QueryDocumentSnapshot doc){
        Map<String,Object> taskMap = doc.getData();
        Log.d("Getter",(String) taskMap.get("Name"));
        Task task = new Task((String) taskMap.get("Name"),(String) taskMap.get("Description"),
                false,false);
        int curId = getCurId();
        int oldCurId = ((Long) taskMap.get("Id")).intValue();
        oldIdsToNew.put(oldCurId,curId);
        Integer oldParentId = ((Long) taskMap.get("ParentId")).intValue();
        if(oldParentId == -1){
            ArrayList<Integer> path = new ArrayList<>();
            path.add(curId);
            oldIdsToPaths.put(oldCurId,path);
            task.setParentId(-1);
            task.setPath(path);
            task.setId(curId);
        }else{
            ArrayList<Integer> path = new ArrayList<>();
            path.addAll(oldIdsToPaths.get(oldParentId));
            path.add(curId);
            oldIdsToPaths.put(oldCurId,path);
            task.setParentId(oldIdsToNew.get(oldParentId));
            task.setPath(oldIdsToPaths.get(oldCurId));
            task.setId(curId);
        }
        lr.insertTask(task);
    }

    private int getCurId(){
        SharedPreferences sharedPreferencesCurId = sharedPreferencesCurId = PreferenceManager.getDefaultSharedPreferences(mContext);
        if(sharedPreferencesCurId.getInt("CUR_ID", -1)==-1){
            final SharedPreferences.Editor editor = sharedPreferencesCurId.edit();
            editor.putInt("CUR_ID",0);
            editor.apply();
        }
        int curId = sharedPreferencesCurId.getInt("CUR_ID", -1);
        incrementCurId();//IMPORTANT ызов этого метода автоматически увеличивает счетчик тасков
        return curId;
    }

    private void incrementCurId(){
        SharedPreferences sharedPreferencesCurId = sharedPreferencesCurId = PreferenceManager.getDefaultSharedPreferences(mContext);
        final SharedPreferences.Editor editor = sharedPreferencesCurId.edit();
        editor.putInt("CUR_ID",sharedPreferencesCurId.getInt("CUR_ID",-2)+1);
        editor.apply();
    }
}
