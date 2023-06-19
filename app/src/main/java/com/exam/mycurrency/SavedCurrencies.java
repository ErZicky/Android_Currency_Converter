package com.exam.mycurrency;

public class SavedCurrencies {

    private String SourceCur; //prima valuta
    private String TargetCur; //seconda valuta


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
