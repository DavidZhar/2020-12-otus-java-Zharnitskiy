package ru.otus.cachehw;

import java.lang.ref.WeakReference;
import java.util.*;

/**
 * @author sergey
 * created on 14.12.18.
 */
public class MyCache<K, V> implements HwCache<K, V> {
//Надо реализовать эти методы

    private final Map<K, V> cache = new WeakHashMap<>();
    private final List<WeakReference<HwListener<K, V>>> listeners = new ArrayList<>();

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
        notifyListeners(key, value, "Putting in cache");
    }

    @Override
    public void remove(K key) {
        V value = cache.remove(key);
        notifyListeners(key, value, "Removing from cache");
    }

    @Override
    public V get(K key) {
        return cache.get(key);
    }

    private void notifyListeners(K key, V value, String action) {
        listeners.forEach(l -> {
            HwListener<K, V> listener = l.get();
            if (listener != null) {
                listener.notify(key, value, action);
            }
        });
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(new WeakReference<>(listener));
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.removeIf(weakRef -> weakRef.get() == listener);
    }
}
