package com.codepath.simpletodo;


import java.util.Comparator;

/*Sorting list according to priority*/

public class SortByPriority implements Comparator<TaskEntry>{
    @Override
    public int compare(TaskEntry task1, TaskEntry task2) {
        return task1.get_priority()-task2.get_priority();
    }
}
