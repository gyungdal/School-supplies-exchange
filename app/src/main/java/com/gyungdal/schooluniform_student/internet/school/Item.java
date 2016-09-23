package com.gyungdal.schooluniform_student.internet.school;

/**
 * Created by GyungDal on 2016-09-22.
 */

public class Item {
    public String id;
    public String name;
    public int grade;
    public int Class;
    public int number;
    public int year;

    public Item(){
        id = name = "";
        grade = Class = number = year = 0;
    }
    public Item(String id, String name, int grade
            , int Class, int number, int year){
        this.id = id;
        this.name = name;
        this.grade = grade;
        this.Class = Class;
        this.number = number;
        this.year = year;
    }
}
