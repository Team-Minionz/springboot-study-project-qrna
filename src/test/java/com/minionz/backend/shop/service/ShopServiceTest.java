package com.minionz.backend.shop.service;

import com.minionz.backend.common.domain.Address;
import com.minionz.backend.common.exception.NotFoundException;
import com.minionz.backend.shop.controller.dto.CommonShopResponseDto;
import com.minionz.backend.shop.controller.dto.ShopRequestDto;
import com.minionz.backend.shop.controller.dto.ShopSaveResponseDto;
import com.minionz.backend.shop.controller.dto.ShopTableRequestDto;
import com.minionz.backend.shop.domain.Shop;
import com.minionz.backend.shop.domain.ShopRepository;
import com.minionz.backend.user.domain.Owner;
import com.minionz.backend.user.domain.OwnerRepository;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ShopServiceTest {

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private ShopService shopService;

    @Autowired
    private OwnerRepository ownerRepository;

    @AfterEach
    void cleanUp() {
        shopRepository.deleteAll();
        ownerRepository.deleteAll();
    }

    @DisplayName("Shop 생성 테스트")
    @Test
    public void saveShopTest() {
        // given
        List<ShopTableRequestDto> list = new ArrayList<>();
        list.add(new ShopTableRequestDto(2));
        list.add(new ShopTableRequestDto(4));
        list.add(new ShopTableRequestDto(4));
        Address address = Address.builder().zipcode("111-222").street("구월동").city("인천시 남동구").build();
        Owner owner = Owner.builder()
                .name("주인")
                .email("jhnj841@naba.com")
                .password("123")
                .telNumber("123123")
                .build();
        Owner savedOwner = ownerRepository.save(owner);
        ShopRequestDto shopRequestDto = new ShopRequestDto("name", address, "032-888-8888", list, savedOwner.getId());
        // when
        ShopSaveResponseDto shopSaveResponseDto = shopService.save(shopRequestDto);
        Shop shop = shopRepository.findByTelNumber(shopRequestDto.getTelNumber())
                .orElseThrow(() -> new NotFoundException("매장 등록 실패"));
        // then
        assertThat(shopSaveResponseDto.getId()).isEqualTo(shop.getId());
    }

    @DisplayName("근처매장_조회_성공")
    @Test
    void 근처매장_조회_성공() {
        List<ShopTableRequestDto> list1 = new ArrayList<>();
        list1.add(new ShopTableRequestDto(2));
        list1.add(new ShopTableRequestDto(4));
        list1.add(new ShopTableRequestDto(4));
        Address address1 = Address.builder().zipcode("111-222").street("구월동").city("인천시 남동구").latitude(37.518378).longitude(126.940114).build();
        List<ShopTableRequestDto> list2 = new ArrayList<>();
        list2.add(new ShopTableRequestDto(2));
        list2.add(new ShopTableRequestDto(4));
        list2.add(new ShopTableRequestDto(4));
        Address address2 = Address.builder().zipcode("111-222").street("구월동").city("인천시 남동구").latitude(38.518378).longitude(127.940114).build();
        Owner owner = Owner.builder()
                .name("주인")
                .email("jhnj841@naba.com")
                .password("123")
                .telNumber("123123")
                .build();
        Owner savedOwner = ownerRepository.save(owner);
        ShopRequestDto shopRequestDto1 = new ShopRequestDto("맘스터치", address1, "032-888-8838", list1, savedOwner.getId());
        ShopRequestDto shopRequestDto2 = new ShopRequestDto("롯데리아", address2, "032-888-8888", list2, savedOwner.getId());
        // when
        shopService.save(shopRequestDto1);
        shopService.save(shopRequestDto2);
        // then
        List<CommonShopResponseDto> commonShopResponseDtos = shopService.nearShop(37.515, 126.940);
        assertThat(commonShopResponseDtos.size()).isEqualTo(1);
    }

    @DisplayName("근처매장_조회_실패")
    @Test
    void 근처매장_조회_실패() {
        List<ShopTableRequestDto> list1 = new ArrayList<>();
        list1.add(new ShopTableRequestDto(2));
        list1.add(new ShopTableRequestDto(4));
        list1.add(new ShopTableRequestDto(4));
        Address address1 = Address.builder().zipcode("111-222").street("구월동").city("인천시 남동구").latitude(37.518378).longitude(126.940114).build();
        List<ShopTableRequestDto> list2 = new ArrayList<>();
        list2.add(new ShopTableRequestDto(2));
        list2.add(new ShopTableRequestDto(4));
        list2.add(new ShopTableRequestDto(4));
        Address address2 = Address.builder().zipcode("111-222").street("구월동").city("인천시 남동구").latitude(.518378).longitude(127.940114).build();
        Owner owner = Owner.builder()
                .name("주인")
                .email("jhnj841@naba.com")
                .password("123")
                .telNumber("123123")
                .build();
        Owner savedOwner = ownerRepository.save(owner);
        ShopRequestDto shopRequestDto1 = new ShopRequestDto("맘스터치", address1, "032-888-8838", list1, savedOwner.getId());
        ShopRequestDto shopRequestDto2 = new ShopRequestDto("롯데리아", address2, "032-888-8888", list2, savedOwner.getId());
        // when
        shopService.save(shopRequestDto1);
        shopService.save(shopRequestDto2);
        // then
        assertThatThrownBy(() -> shopService.nearShop(40.515, 132.940))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("등록된 매장이 존재하지 않습니다.");
    }
}
