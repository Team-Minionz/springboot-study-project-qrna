package com.minionz.backend.shop.service;

import com.minionz.backend.common.domain.Message;
import com.minionz.backend.shop.controller.dto.ShopSaveRequestDto;
import com.minionz.backend.shop.domain.ShopRepository;
import com.minionz.backend.shop.domain.ShopTable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ShopServiceTest {

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private ShopService shopService;

    @AfterEach
    void cleanUp() {
        shopRepository.deleteAll();
    }

    @DisplayName("Shop 생성 테스트")
    @Test
    public void makeTableListTest() {
        // given
        List<ShopTable> list = new ArrayList<>();
        list.add(ShopTable.builder().maxUser(2).build());
        list.add(ShopTable.builder().maxUser(4).build());
        list.add(ShopTable.builder().maxUser(4).build());
        ShopSaveRequestDto shopSaveRequestDto = new ShopSaveRequestDto("테스트", "442-152", "구월동", "인천시 남동구", "032-888-8888", list);
        // when
        Message message = shopService.save(shopSaveRequestDto);
        // then
        assertThat(message.getMessage()).isEqualTo("가게 등록 성공");
    }
}
