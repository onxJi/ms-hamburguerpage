package com.onx.hamburgerpage.repositories;
import com.google.cloud.firestore.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class FirestoreService {

    private final Firestore firestore;

    public FirestoreService(Firestore firestore) {
        this.firestore = firestore;
    }

    public <T> List<T> getAll(String collection, Class<T> clazz) throws ExecutionException, InterruptedException {
        List<T> items = new ArrayList<>();
        firestore.collection(collection).get().get().forEach(doc -> {
            items.add(doc.toObject(clazz));
        });
        return items;
    }

    public <T> T getById(String collection, String id, Class<T> clazz) throws ExecutionException, InterruptedException {
        DocumentSnapshot doc = firestore.collection(collection).document(id).get().get();
        return doc.toObject(clazz);
    }

    public void save(String collection, String id, Object data) throws ExecutionException, InterruptedException {
        firestore.collection(collection).document(id).set(data).get();
    }

    public void delete(String collection, String id) throws ExecutionException, InterruptedException {
        firestore.collection(collection).document(id).delete().get();
    }
}