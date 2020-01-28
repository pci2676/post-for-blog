package chap2.item6;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AdapterTest {

    @DisplayName("어댑터 패턴이 적용된 keySet 테스트")
    @Test
    void name() {
        Map<Integer, String> map = new HashMap<>();
        map.put(1, "첫번째");
        map.put(2, "두번째");
        map.put(3, "세번째");

        // keySet 은 view 의 역할을 한다.
        Set<Integer> keySet = map.keySet();
        assertThat(keySet).contains(1, 2, 3);

        // 따라서 뒷단 객체인 map 이 기능을 담당하고 있다.
        map.remove(3);
        assertThat(keySet).doesNotContain(3);

        // view 인 keySet 에서 기능을 동작하려 하면 exception 이 발생한다.
        assertThatThrownBy(() -> keySet.add(3))
                .isInstanceOf(UnsupportedOperationException.class);
    }
}