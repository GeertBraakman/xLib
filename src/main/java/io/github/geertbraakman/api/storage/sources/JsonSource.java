package io.github.geertbraakman.api.storage.sources;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.com.geertbraakman.api.storage.Source;

public abstract class JsonSource implements Source {

    private ObjectMapper mapper;

    public JsonSource(){
        reload();
    }

    @Override
    public boolean reload() {
        mapper = new ObjectMapper();
        return onReload();
    }

    public abstract boolean onReload();

    public ObjectMapper getMapper() {
        return mapper;
    }

}
