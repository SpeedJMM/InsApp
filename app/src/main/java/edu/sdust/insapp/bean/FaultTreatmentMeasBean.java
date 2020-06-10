package edu.sdust.insapp.bean;

public class FaultTreatmentMeasBean {
    private String faulttreatmentmeascode;
    private String faulttreatmentmeascatgcode;
    private String faulttreatmentmeascontent;

    public String getFaulttreatmentmeascode() {
        return faulttreatmentmeascode;
    }

    public void setFaulttreatmentmeascode(String faulttreatmentmeascode) {
        this.faulttreatmentmeascode = faulttreatmentmeascode;
    }

    public String getFaulttreatmentmeascatgcode() {
        return faulttreatmentmeascatgcode;
    }

    public void setFaulttreatmentmeascatgcode(String faulttreatmentmeascatgcode) {
        this.faulttreatmentmeascatgcode = faulttreatmentmeascatgcode;
    }

    public String getFaulttreatmentmeascontent() {
        return faulttreatmentmeascontent;
    }

    public void setFaulttreatmentmeascontent(String faulttreatmentmeascontent) {
        this.faulttreatmentmeascontent = faulttreatmentmeascontent;
    }

    @Override
    public String toString() {
        return ""+faulttreatmentmeascode+": "+faulttreatmentmeascontent;
    }
}
