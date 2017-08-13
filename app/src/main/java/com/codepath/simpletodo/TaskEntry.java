package com.codepath.simpletodo;



public class TaskEntry {

    private long _id;
    private String _title;
    private String _date;
    private int _priority;
    private String _status;

    public TaskEntry() {
    }

    public TaskEntry(String _title, String _date, int _priority, String _status) {
        this._title = _title;
        this._date = _date;
        this._priority = _priority;
        this._status = _status;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String get_title() {
        return _title;
    }

    public void set_title(String _title) {
        this._title = _title;
    }

    public String get_date() {
        return _date;
    }

    public void set_date(String _date) {
        this._date = _date;
    }

    public int get_priority() {
        return _priority;
    }

    public void set_priority(int _priority) {
        this._priority = _priority;
    }

    public String get_status() {
        return _status;
    }

    public void set_status(String _status) {
        this._status = _status;
    }
}
