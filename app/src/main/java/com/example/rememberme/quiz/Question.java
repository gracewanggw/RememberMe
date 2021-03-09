package com.example.rememberme.quiz;

import android.os.Parcel;
import android.os.Parcelable;

public class Question implements Parcelable {

    public int id;
    public String person;
    public String qType;
    public String qDataType;
    public String qStructure;
    public String mQuestion;
    public String aDataType;
    public String aStructure;
    public String answer;
    public Boolean review;


    public Question() {
    }

    public Question(Parcel in) {
        id = in.readInt();
        person = in.readString();
        qType = in.readString();
        qDataType = in.readString();
        qStructure = in.readString();
        mQuestion = in.readString();
        aDataType = in.readString();
        aStructure = in.readString();
        answer = in.readString();
        review = in.readBoolean();
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    public void setId(int id) {this.id = id;}
    public int getId() {return id;}

    public void setPerson(String person) {this.person = person;}
    public String getPerson() {return person;}

    public void setQType(String qType) {this.qType = qType;}
    public String getQType() {return qType;}

    public void setQDataType(String qDataType) {this.qDataType = qDataType;}
    public String getQDataTypen() {return qDataType;}

    public void setQStructure(String qStructure) {this.qStructure = qStructure;}
    public String getQStructure() {return qStructure;}

    public void setmQuestion(String mQuestion) {this.mQuestion = mQuestion;}
    public String getmQuestion() {return mQuestion;}

    public void setADataType(String aDataType) {this.aDataType = aDataType;}
    public String getADataTypen() {return aDataType;}

    public void setAStructure(String aStructure) {this.aStructure = aStructure;}
    public String getAStructure() {return aStructure;}

    public void setAnswer(String answer) {this.answer = answer;}
    public String getAnswer() {return answer;}

    public void setReview(Boolean review) {this.review = review;}
    public Boolean getReview() {return review;}


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.person);
        dest.writeString(this.qType);
        dest.writeString(this.qDataType);
        dest.writeString(this.qStructure);
        dest.writeString(this.mQuestion);
        dest.writeString(this.aDataType);
        dest.writeString(this.aStructure);
        dest.writeString(this.answer);
        dest.writeBoolean(this.review);
    }


}
