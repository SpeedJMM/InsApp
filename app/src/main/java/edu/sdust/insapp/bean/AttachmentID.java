package edu.sdust.insapp.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/18.
 */

public class AttachmentID {
    private Map<Integer, String> attachmentIDs;
    private int count;

    public AttachmentID() {
        attachmentIDs = new HashMap<>();
        count = 0;
    }

    public Map<Integer, String> getAttachmentIDs() {
        return attachmentIDs;
    }

    public void setAttachmentIDs(Map<Integer, String> attachmentIDs) {
        this.attachmentIDs = attachmentIDs;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
