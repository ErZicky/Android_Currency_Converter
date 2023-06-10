package com.example.mycurrency;

import java.lang.annotation.Target;

public class SavedCurrencies {

    private String SourceCur;
    private String TargetCur;


    public  SavedCurrencies(String cur1, String cur2)
    {
        this.SourceCur = cur1;
        this.TargetCur = cur2;
    }

    public String GetSource() {

        return SourceCur;
    }

    public String getTargetCur()
    {
        return TargetCur;
    }


}
