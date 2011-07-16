package hudson.plugins.violations.util;

import java.util.HashMap;

/**
 * An extension of a hash map with a default value for
 * keys not present.
 * @param <K> the type of key for this map.
 * @param <V> the type of mapped value.
 */
public class HashMapWithDefault<K, V> extends HashMap<K, V> {
    private final V defaultValue;
    /**
     * Construct a new hash map.
     * @param defaultValue the value to use if a key cannot be found.
     */
    public HashMapWithDefault(V defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * Get a value for a key.
     * @param k the key to use to lookup the map.
     * @return the value for the key, or the default value if the key
     *         is not in the map.
     */
    @Override
    public V get(Object k) {
        V ret = super.get(k);
        if (ret == null) {
            return defaultValue;
        } else {
            return ret;
        }
    }
}
