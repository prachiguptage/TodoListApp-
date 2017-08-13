package com.codepath.simpletodo;


public class CheckListItem {

    private long _id;
    private String _item_name;
    private String _checked;
    private long _list_id;

    public CheckListItem(String _item_name, String _checked, long _list_id) {
        this._item_name = _item_name;
        this._checked = _checked;
        this._list_id = _list_id;
    }

    public CheckListItem(String _item_name, String _checked) {
        this._item_name = _item_name;
        this._checked = _checked;
    }

    public CheckListItem() {
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String get_item_name() {
        return _item_name;
    }

    public void set_item_name(String _item_name) {
        this._item_name = _item_name;
    }

    public String get_checked() {
        return _checked;
    }

    public void set_checked(String _checked) {
        this._checked = _checked;
    }

    public long get_list_id() {
        return _list_id;
    }

    public void set_list_id(long _list_id) {
        this._list_id = _list_id;
    }
}
