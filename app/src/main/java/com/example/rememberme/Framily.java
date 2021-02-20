package com.example.rememberme;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;

public class Framily {

    public int id;
    public String nameFirst;
    public String nameLast;
    public String relationship;
    public int age;
    public String birthday;
    public String location;
    public String phoneNumber;
    public ArrayList<Integer> memories;
    public int image;


    public Framily() {
        memories = new ArrayList<Integer>();
        image = R.drawable._pic;
    }

    public void setId(int id) {this.id = id;}
    public int getId() {return id;}

    public void setNameFirst(String nameFirst) {this.nameFirst = nameFirst;}
    public String getNameFirst() {return nameFirst;}

    public void setNameLast(String nameLast) {this.nameLast = nameLast;}
    public String getNameLast() {return nameLast;}

    public void setRelationship(String relationship) {this.relationship = relationship;}
    public String getRelationship() {return relationship;}

    public void setAge(int age) {this.age = age;}
    public int getAge() {return age;}

    public void setBirthday(String birthday) {this.birthday = birthday;}
    public String getBirthday() {return birthday;}

    public void setLocation(String location) {this.location = location;}
    public String getLocation() {return location;}

    public void setPhoneNumber(String phoneNumber) {this.phoneNumber = phoneNumber;}
    public String getPhoneNumber() {return phoneNumber;}

    public void setMemories(ArrayList<Integer> memories) {this.memories = memories;}
    public ArrayList<Integer> getMemories() {return memories;}
    public void addMemory(int memory) {memories.add(memory);}

    public void setImage(int image) {this.image = image;}
    public int getImage() {return image;}

    public String toString() {
        return nameFirst + " " + nameLast + ", Relationship = " + relationship;
    }
}
