package com.example.bleapp;

public class NoteModel {
    int id;
    String noteTitle,noteDetails,noteDate,noteTime;

    public NoteModel() {

    }

    public NoteModel(String noteTitle, String noteDetails, String noteDate, String noteTime) {
        this.noteTitle = noteTitle;
        this.noteDetails = noteDetails;
        this.noteDate = noteDate;
        this.noteTime = noteTime;
    }

    public NoteModel(int id, String noteTitle, String noteDetails, String noteDate, String noteTime) {
        this.id = id;
        this.noteTitle = noteTitle;
        this.noteDetails = noteDetails;
        this.noteDate = noteDate;
        this.noteTime = noteTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getNoteDetails() {
        return noteDetails;
    }

    public void setNoteDetails(String noteDetails) {
        this.noteDetails = noteDetails;
    }

    public String getNoteDate() {
        return noteDate;
    }

    public void setNoteDate(String noteDate) {
        this.noteDate = noteDate;
    }

    public String getNoteTime() {
        return noteTime;
    }

    public void setNoteTime(String noteTime) {
        this.noteTime = noteTime;
    }
}
