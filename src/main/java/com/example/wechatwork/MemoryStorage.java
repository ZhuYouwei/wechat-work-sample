package com.example.wechatwork;

import com.example.wechatwork.model.AttestUserInfo;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class MemoryStorage {
    private HashMap<String, AttestUserInfo> store = new HashMap<>();

    public HashMap<String, AttestUserInfo> getStore() {
        return store;
    }

    public void setStore(HashMap<String, AttestUserInfo> store) {
        this.store = store;
    }

    public AttestUserInfo getInfoByTaskId(String taskId) {
        return store.get(taskId);
    }

    public void addTaskId(String taskId, AttestUserInfo info) {
        store.put(taskId, info);
    }

    public void removeTaskId(String taskId) {
        store.remove(taskId);
    }
}
