import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class HashMapTest {
    @Test
    void test1() {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("1", "123");
        hashMap.put("2", "123");

        Set<String> set = hashMap.keySet();
        set.remove("1");

        assertThat(hashMap.get("1")).isNull();
    }

    @Test
    void test2() {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("1", "123");
        hashMap.put("2", "123");

        Set<String> set = hashMap.keySet();
        hashMap.put("3", "123");

        assertThat(set.contains("3")).isNotNull();
    }
}
