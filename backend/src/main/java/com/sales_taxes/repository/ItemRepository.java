package com.sales_taxes.repository;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sales_taxes.entitiy.Item;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Random;
import java.util.LinkedHashSet;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;
import org.springframework.util.ResourceUtils;

@Repository
public class ItemRepository {

  LinkedHashSet<Item> items = new LinkedHashSet<>();
  double salesTax;
  double importedSalesTax;

  public ItemRepository() throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    Resource resource = new ClassPathResource("static/shop.json");
    InputStream inputStream = resource.getInputStream();

    JsonNode shopFileJson = objectMapper.readTree(inputStream);
    salesTax = shopFileJson.get("sales_tax").asDouble();
    importedSalesTax = shopFileJson.get("imported_sales_tax").asDouble();

    JsonNode itemsJson = shopFileJson.get("items");
    Random random = new Random();

    if (itemsJson.isArray()) {
      for (JsonNode item : itemsJson) {
        Item shopItem = new Item();
        shopItem.setId(random.nextInt(Integer.MAX_VALUE));
        shopItem.setName(item.get("name").asText());
        shopItem.setPrice(item.get("price").asDouble());
        this.items.add(shopItem);
      }
    }
  }

  public LinkedHashSet<Item> getItems() {
    return this.items;
  }
}
