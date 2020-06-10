package edu.sdust.insapp.bean;

public class FaultCauseBean {
    private String faultcausecode;
    private String faultcausecontent;

    public String getFaultcausecode() {
        return faultcausecode;
    }

    public void setFaultcausecode(String faultcausecode) {
        this.faultcausecode = faultcausecode;
    }

    public String getFaultcausecontent() {
        return faultcausecontent;
    }

    public void setFaultcausecontent(String faultcausecontent) {
        this.faultcausecontent = faultcausecontent;
    }

    @Override
    public String toString() {
        return ""+faultcausecode+": "+faultcausecontent;
    }
}
