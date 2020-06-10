package edu.sdust.insapp.utils;

import edu.sdust.insapp.bean.AttachmentID;

/**
 * Created by Administrator on 2017/11/18.
 */

public class AttachmentIDUtil {
    private static AttachmentID attachment;
    public static AttachmentID getInstance(){
        if(attachment == null){
            attachment = new AttachmentID();
        }
        return attachment;
    }

}
