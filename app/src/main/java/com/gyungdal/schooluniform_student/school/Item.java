package com.gyungdal.schooluniform_student.school;

/**
 * Created by GyungDal on 2016-09-05.
 */
public class Item {
    public String state;
    public String detailArea;
    public String schoolType;
    public String schoolName;

    public Item(String state, String detailArea,
                     String schoolType, String schoolName){
        this.state = state;
        this.detailArea = detailArea;
        this.schoolType = schoolType;
        this.schoolName = schoolName;
    }
}
