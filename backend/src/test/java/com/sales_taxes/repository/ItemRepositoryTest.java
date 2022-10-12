package com.sales_taxes.repository;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sales_taxes.entitiy.GroceryItem;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Random;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ResourceUtils;

@SpringBootTest
public class ItemRepositoryTest {

  @Autowired
  public ItemRepository itemRepository;
  LinkedHashSet<GroceryItem> storedItems;

  BigDecimal salesTax;
  BigDecimal importedSalesTax;

  public ItemRepositoryTest() throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    File shopFile = ResourceUtils.getFile("classpath:static/shop.json");
    JsonNode shopFileJson = objectMapper.readTree(shopFile);

    salesTax = BigDecimal.valueOf(shopFileJson.get("sales_tax").asDouble());
    importedSalesTax = BigDecimal.valueOf(shopFileJson.get("imported_sales_tax").asDouble());

    JsonNode itemsJson = shopFileJson.get("items");

    Random random = new Random();
    this.storedItems = new LinkedHashSet<>();

    if (itemsJson.isArray()) {
      for (JsonNode item : itemsJson) {
        GroceryItem shopItem = new GroceryItem();
        shopItem.setId(random.nextInt(Integer.MAX_VALUE));
        shopItem.setName(item.get("name").asText());
        shopItem.setPreTaxPrice(BigDecimal.valueOf(item.get("price").asDouble()));
        this.storedItems.add(shopItem);
      }
    }
  }

  @Test
  public void getItems_itemsStored_shouldReturnAllItems() {
    LinkedHashSet<GroceryItem> returnedItems = this.itemRepository.getItems();
    Assertions.assertEquals(returnedItems.size(), this.storedItems.size());
  }

  @Test
  public void getSalesTax_shouldReturnSalesTax() {
    Assertions.assertEquals(this.itemRepository.getSalesTax().doubleValue(),
        this.salesTax.doubleValue());
  }

  @Test
  public void getImportedSalesTax_shouldReturnImportedSalesTax() {
    Assertions.assertEquals(this.itemRepository.getImportedSalesTax().doubleValue(),
        this.importedSalesTax.doubleValue());
  }


}
