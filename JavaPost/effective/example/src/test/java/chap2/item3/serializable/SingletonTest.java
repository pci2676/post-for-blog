package chap2.item3.serializable;

import com.google.gson.Gson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class SingletonTest {

    @DisplayName("자바 직렬화 테스트")
    @Test
    void singletonTest1() throws IOException, ClassNotFoundException {
        Singleton singleton = Singleton.getInstance();
        byte[] serializedSingletonByte;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                oos.writeObject(singleton);
                // serializedSingletonByte -> 직렬화된 객체
                serializedSingletonByte = baos.toByteArray();
            }
        }
        // 바이트 배열로 생성된 직렬화 데이터를 base64로 변환
        String serializedSingleton = Base64.getEncoder().encodeToString(serializedSingletonByte);

        byte[] deSerializedSingletonByte = Base64.getDecoder().decode(serializedSingleton);

        ByteArrayInputStream bais = new ByteArrayInputStream(deSerializedSingletonByte);
        ObjectInputStream ois = new ObjectInputStream(bais);

        // 역직렬화된 Singleton 객체를 읽어온다.
        Object objectMember = ois.readObject();
        Singleton deSerializedSingleton = (Singleton) objectMember;

        assertAll(
                () -> assertThat(singleton).isEqualTo(deSerializedSingleton),
                () -> assertThat(singleton == deSerializedSingleton).isTrue()
        );
    }

    @DisplayName("gson 직렬화 테스트")
    @Test
    void singletonTest2() {
        Singleton singleton = Singleton.getInstance();

        Gson gson = new Gson();
        String jsonSingleton = gson.toJson(singleton);

        Singleton deSerializedSingleton = gson.fromJson(jsonSingleton, Singleton.class);

        assertAll(
                () -> assertThat(singleton).isNotEqualTo(deSerializedSingleton),
                () -> assertThat(singleton == deSerializedSingleton).isFalse()
        );
    }
}