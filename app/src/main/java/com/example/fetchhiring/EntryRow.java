package com.example.fetchhiring;

// Object class to store each row in the table

public class EntryRow {
    private String listId;
    private String name;
    private String id;

    public EntryRow(String listId, String name, String id) {
        this.listId = listId;
        this.name = "Item " + name;
        this.id = id;
    }

    public String getListId() {
        return listId;
    }

    public void setListId(String listId) {
        this.listId = listId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
