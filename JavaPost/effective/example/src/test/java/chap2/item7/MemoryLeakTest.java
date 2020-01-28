package chap2.item7;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class MemoryLeakTest {

    @DisplayName("HashMap 테스트")
    @Test
    void test1() {
        //given
        Map<Foo, String> map = new HashMap<>();

        Foo key = new Foo();
        map.put(key, "1");

        //when
        key = null;
        System.gc();

        //then
        assertAll(
                "HashMap 은 참조를 끊어도 map 이 비어있지 않다.",
                () -> assertThat(map.isEmpty()).isFalse()
        );

    }

    @DisplayName("WeakHashMap 테스트")
    @Test
    void test2() {
        //given
        Map<Foo, String> map = new WeakHashMap<>();

        Foo key = new Foo();
        map.put(key, "1");

        //when
        key = null;
        System.gc();

        //then
        assertAll(
                "WeakHashMap 은 참조를 끊으면 map 이 비어있다.",
                () -> assertThat(map.isEmpty()).isTrue()
        );
    }
}