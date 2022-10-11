package com.sales_taxes.repository;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sales_taxes.entitiy.Item;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.LinkedHashSet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ResourceUtils;

@SpringBootTest
public class ItemRepositoryTest {

  @Autowired
  public ItemRepository itemRepository;
  LinkedHashSet<Item> storedItems;

  public ItemRepositoryTest() throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    File shopFile = ResourceUtils.getFile("classpath:static/shop.json");
    JsonNode shopFileJson = objectMapper.readTree(shopFile);

    JsonNode itemsJson = shopFileJson.get("items");
    Random random = new Random();
    this.storedItems = new LinkedHashSet<>();

    if (itemsJson.isArray()) {
      for (JsonNode item : itemsJson) {
        Item shopItem = new Item();
        shopItem.setId(random.nextInt(Integer.MAX_VALUE));
        shopItem.setName(item.get("name").asText());
        shopItem.setPrice(item.get("price").asDouble());
        this.storedItems.add(shopItem);
      }
    }
  }

  @Test
  public void getItems_itemsStored_shouldReturnAllItems() {
    LinkedHashSet<Item> returnedItems = this.itemRepository.getItems();
    Assertions.assertSame(returnedItems.size(), this.storedItems.size());
  }


}
