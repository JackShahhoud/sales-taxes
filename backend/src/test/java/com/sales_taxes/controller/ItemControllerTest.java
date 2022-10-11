package com.sales_taxes.controller;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
public class ItemControllerTest {

  @MockBean
  public ItemRepository itemRepository;

  @Autowired
  public ItemService itemService;

  @Autowired
  private MockMvc mockMvc;


  @Test
  public void getItems_itemsStored_shouldReturnAllItems() throws Exception {
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

    MvcResult requestResult =
        this.mockMvc.perform(get("/items/")).andDo(print()).andExpect(status().isOk()).andReturn();
    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode itemsJson = objectMapper.readTree(requestResult.getResponse().getContentAsString());

    List<ItemDTO> returnedItems = new ArrayList<>();

    if (itemsJson.isArray()) {
      for (JsonNode item : itemsJson) {
        ItemDTO shopItem = new ItemDTO();
        shopItem.setId(item.get("id").asLong());
        shopItem.setName(item.get("name").asText());
        shopItem.setPrice(item.get("price").asDouble());
        returnedItems.add(shopItem);
      }
    }

    List<Item> storedItemsList = new ArrayList<>(storedItems);

    Assertions.assertSame(returnedItems.size(), storedItemsList.size());

    for (int i = 0; i < storedItemsList.size(); ++i) {
      Assertions.assertEquals(storedItemsList.get(i).getId(), returnedItems.get(i).getId());
      Assertions.assertEquals(storedItemsList.get(i).getName(), returnedItems.get(i).getName());
      Assertions.assertEquals(storedItemsList.get(i).getPrice(), returnedItems.get(i).getPrice());
    }

  }

}
