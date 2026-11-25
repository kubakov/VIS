package data.identitymap;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class IdentityMap<T> {
    private final Map<Long, T> cache = new HashMap<>();

    public Optional<T> get(Long id) {
        if(id == null){
            return Optional.empty();
        }
        return Optional.ofNullable(cache.get(id));
    }

    public void put(Long id, T entity) {
        if(id != null && entity != null) {
            cache.put(id, entity);
        }
    }

    public boolean contains(Long id) {
        return id != null && cache.containsKey(id);
    }

    public void remove(Long id) {
        if(id != null) {
            cache.remove(id);
        }
    }

    public void clear() {
        cache.clear();
    }
}