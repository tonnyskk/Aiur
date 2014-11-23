package com.origin.aiur.dao;

import com.origin.aiur.app.AiurApplication;
import com.origin.aiur.dao.storage.APrivateMapStorage;
import com.origin.aiur.dao.storage.ASQLMapHelper;
import com.origin.aiur.dao.storage.ASQLMapStorage;
import com.origin.aiur.dao.storage.IAStorage;

import java.util.HashMap;

/**
 * Created by Administrator on 2014/10/22.
 */
public class AStoreManager {
    private final static AStoreManager instance = new AStoreManager();

    private static final HashMap<StorageName, IAStorage> storages = new HashMap<StorageName, IAStorage>(9);

    static enum StorageName {
        UserIdentityStore, UserStore, UserEventStore, GroupEventStore, FinanceStore, GroupUserStore, RequestEventStore
    }

    private AStoreManager() {

    }

    public static AStoreManager getInstance() {
        return instance;
    }

    public synchronized APrivateMapStorage getIdentityStore() {
        return getPrivateMapStore(StorageName.UserIdentityStore);
    }

    private synchronized APrivateMapStorage getPrivateMapStore(StorageName storageName) {
        if (!storages.containsKey(storageName)) {
            APrivateMapStorage storage = new APrivateMapStorage(AiurApplication.getInstance().getApplicationContext(), storageName.name());
            storages.put(storageName, storage);
            return storage;
        }
        return (APrivateMapStorage) storages.get(storageName);
    }

    public synchronized ASQLMapStorage getUserStore() {
        if (storages.containsKey(StorageName.UserStore)) {
            return (ASQLMapStorage) storages.get(StorageName.UserStore);
        }
        ASQLMapStorage asqlMapStorage = new ASQLMapStorage(ASQLMapHelper.getInstance(), StorageName.UserStore.name());
        storages.put(StorageName.UserStore, asqlMapStorage);
        return asqlMapStorage;
    }

    public synchronized ASQLMapStorage getUserEventStore() {
        if (storages.containsKey(StorageName.UserEventStore)) {
            return (ASQLMapStorage) storages.get(StorageName.UserEventStore);
        }
        ASQLMapStorage asqlMapStorage = new ASQLMapStorage(ASQLMapHelper.getInstance(), StorageName.UserEventStore.name());
        storages.put(StorageName.UserEventStore, asqlMapStorage);
        return asqlMapStorage;
    }

    public synchronized ASQLMapStorage getGroupEventStore() {
        if (storages.containsKey(StorageName.GroupEventStore)) {
            return (ASQLMapStorage) storages.get(StorageName.GroupEventStore);
        }
        ASQLMapStorage asqlMapStorage = new ASQLMapStorage(ASQLMapHelper.getInstance(), StorageName.GroupEventStore.name());
        storages.put(StorageName.GroupEventStore, asqlMapStorage);
        return asqlMapStorage;
    }

    public synchronized ASQLMapStorage getRequestEventStore() {
        if (storages.containsKey(StorageName.RequestEventStore)) {
            return (ASQLMapStorage) storages.get(StorageName.RequestEventStore);
        }
        ASQLMapStorage asqlMapStorage = new ASQLMapStorage(ASQLMapHelper.getInstance(), StorageName.RequestEventStore.name());
        storages.put(StorageName.RequestEventStore, asqlMapStorage);
        return asqlMapStorage;
    }

    public synchronized ASQLMapStorage getFinanceStore() {
        if (storages.containsKey(StorageName.FinanceStore)) {
            return (ASQLMapStorage) storages.get(StorageName.FinanceStore);
        }
        ASQLMapStorage asqlMapStorage = new ASQLMapStorage(ASQLMapHelper.getInstance(), StorageName.FinanceStore.name());
        storages.put(StorageName.FinanceStore, asqlMapStorage);
        return asqlMapStorage;
    }

    public synchronized ASQLMapStorage getGroupUserStore() {
        if (storages.containsKey(StorageName.GroupUserStore)) {
            return (ASQLMapStorage) storages.get(StorageName.GroupUserStore);
        }
        ASQLMapStorage asqlMapStorage = new ASQLMapStorage(ASQLMapHelper.getInstance(), StorageName.GroupUserStore.name());
        storages.put(StorageName.GroupUserStore, asqlMapStorage);
        return asqlMapStorage;
    }
}
