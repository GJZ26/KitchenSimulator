package com.upchiapas.render.collection;

import com.upchiapas.model.RenderData;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

public class RenderEntityCollection {

    private Map<Object, RenderData> collection;

    public RenderEntityCollection() {
        collection = new ConcurrentHashMap<>();
    }

    public void addOrUpdate(Object key, RenderData value) {
        collection.put(key, value);
    }

    public void remove(Object key) {
        collection.remove(key);
    }

    public RenderData[] getRenderData() {
        RenderData[] result = new RenderData[collection.size()];
        int i = 0;
        for (RenderData value : collection.values()) {
            result[i++] = value;
        }
        return result;
    }
}
