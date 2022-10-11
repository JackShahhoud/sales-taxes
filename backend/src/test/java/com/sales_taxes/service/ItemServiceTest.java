package com.sales_taxes.service;

import com.sales_taxes.dto.ItemDTO;
import com.sales_taxes.entitiy.Item;
import com.sales_taxes.repository.ItemRepository;
import com.sales_taxes.services.ItemService;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.LinkedHashSet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class ItemServiceTest {

  @MockBean
  public ItemRepository itemRepository;

  @Autowired
  public ItemService itemService;

  @Test
  public void getItems_itemsStored_shouldReturnAllItems() {
    LinkedHashSet<Item> storedItems = new LinkedHashSet<>();
    Random random = new Random();

    for (int i = 0; i < 10; ++i) {
      Item item = new Item();
      item.setId(random.nextInt(Integer.MAX_VALUE));
      item.setPrice(i * i);
      item.setName("Item" + i);
      storedItems.add(item);
    }

    Mockito.when(this.itemRepository.getItems()).thenReturn(storedItems);
    List<ItemDTO> returnedItems = this.itemService.getItems();
    Assertions.assertEquals(returnedItems.size(), storedItems.size());
    List<Item> storedItemsList = new ArrayList<>(storedItems);
    for (int i = 0; i < storedItemsList.size(); ++i) {
      Assertions.assertEquals(storedItemsList.get(i).getId(), returnedItems.get(i).getId());
      Assertions.assertEquals(storedItemsList.get(i).getName(), returnedItems.get(i).getName());
      Assertions.assertEquals(storedItemsList.get(i).getPrice(), returnedItems.get(i).getPrice());
    }

  }


}
