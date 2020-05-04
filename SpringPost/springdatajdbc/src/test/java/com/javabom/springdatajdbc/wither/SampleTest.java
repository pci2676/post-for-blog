package com.javabom.springdatajdbc.wither;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class SampleTest {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Autowired
    private SampleInterface sampleInterface;

    @DisplayName("생성 테스트")
    @Test
    void name() {
        //given
        Sample sample = new Sample("sample");
//        Sample sample = new Sample(null, "sample");
        Sample save = sampleInterface.save(sample);
        System.out.println(gson.toJson(save));

        //when
        Sample sample1 = sampleInterface.findById(save.getId())
                .orElseThrow(RuntimeException::new);


        //then
        assertThat(sample1.getId()).isEqualTo(save.getId());
        assertThat(sample1.getSampleName()).isEqualTo(save.getSampleName());
    }
}