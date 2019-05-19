package com.ming.mapboxplugins.abase;

public class ItemBean {
    private String itemName;
    private Class<?> cls;

    public ItemBean(String itemName, Class<?> cls) {
        this.itemName = itemName;
        this.cls = cls;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Class<?> getCls() {
        return cls;
    }

    public void setCls(Class<?> cls) {
        this.cls = cls;
    }
}
