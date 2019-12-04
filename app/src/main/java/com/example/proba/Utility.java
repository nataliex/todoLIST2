package com.example.proba;

import java.util.Date;

public class Utility {
    public class DateNParent{
        Date mDate;
        Integer mParent;
        DateNParent(Date date,Integer parent){
            mDate=date;
            mParent=parent;
        }
    }

    public class StarNParent{
        Boolean mStar;
        Integer mParent;
        StarNParent(Boolean Star,Integer parent){
            mStar=Star;
            mParent=parent;
        }
    }

    public class CompleteNParent{
        Boolean mCompleted;
        Integer mParent;
        CompleteNParent(Boolean Completed,Integer parent){
            mCompleted=Completed;
            mParent=parent;
        }
    }
}

