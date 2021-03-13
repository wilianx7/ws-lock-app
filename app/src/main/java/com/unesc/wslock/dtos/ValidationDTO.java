package com.unesc.wslock.dtos;

public class ValidationDTO {
    private String table;
    private String column;
    private String ignore;
    private String value;

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getIgnore() {
        return ignore;
    }

    public void setIgnore(String ignore) {
        this.ignore = ignore;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String toJson() {
        return "{"
                + "\"table\": \"" + this.table + "\","
                + "\"column\": \"" + this.column + "\","
                + "\"ignore\": \"" + this.ignore + "\","
                + "\"value\": \"" + this.value
                + "\"}";
    }
}
