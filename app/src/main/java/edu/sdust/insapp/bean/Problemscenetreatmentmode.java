package edu.sdust.insapp.bean;

/**
 * Created by Administrator on 2017/11/17.
 */

public class Problemscenetreatmentmode {
    private String inspproblemscenetreatmentmodecode;
    private String inspproblemscenetreatmentmodeentry;

    public String getInspproblemscenetreatmentmodecode() {
        return inspproblemscenetreatmentmodecode;
    }

    public void setInspproblemscenetreatmentmodecode(String inspproblemscenetreatmentmodecode) {
        this.inspproblemscenetreatmentmodecode = inspproblemscenetreatmentmodecode;
    }

    public String getInspproblemscenetreatmentmodeentry() {
        return inspproblemscenetreatmentmodeentry;
    }

    public void setInspproblemscenetreatmentmodeentry(String inspproblemscenetreatmentmodeentry) {
        this.inspproblemscenetreatmentmodeentry = inspproblemscenetreatmentmodeentry;
    }

    @Override
    public String toString() {
        return ""+inspproblemscenetreatmentmodecode+": "+inspproblemscenetreatmentmodeentry;
    }
}
