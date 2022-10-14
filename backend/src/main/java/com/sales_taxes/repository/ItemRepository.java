package com.sales_taxes.repository;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sales_taxes.dto.ReceiptDTO;
import com.sales_taxes.entitiy.GroceryItem;
import com.sales_taxes.entitiy.ItemType;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Random;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class ItemRepository {

  LinkedHashSet<GroceryItem> items = new LinkedHashSet<>();
  BigDecimal salesTax;
  BigDecimal importedSalesTax;

  /**
   * Store all items from the file shops, including sales taxes and imported sales taxes.
   *
   * @throws IOException If parsing exception happens during parsing shop.json file
   */
  public ItemRepository() throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    Resource resource = new ClassPathResource("static/shop.json");
    InputStream inputStream = resource.getInputStream();

    JsonNode shopFileJson = objectMapper.readTree(inputStream);
    salesTax = BigDecimal.valueOf(shopFileJson.get("sales_tax").asDouble());
    importedSalesTax = BigDecimal.valueOf(shopFileJson.get("imported_sales_tax").asDouble());

    JsonNode itemsJson = shopFileJson.get("items");
    Random random = new Random();

    if (itemsJson.isArray()) {
      for (JsonNode item : itemsJson) {
        GroceryItem shopItem = new GroceryItem();
        shopItem.setId(random.nextInt(Integer.MAX_VALUE));
        shopItem.setName(item.get("name").asText());
        shopItem.setPreTaxPrice(BigDecimal.valueOf(item.get("price").asDouble()));
        shopItem.setType(ItemType.valueOf(item.get("type").asText()));
        shopItem.setImported(item.get("imported").asBoolean());
        this.items.add(shopItem);
      }
    }
  }

  /**
   * Returns all stored grocery items parsed from the file.
   *
   * @return all stored grocery items
   * @see GroceryItem
   */
  public LinkedHashSet<GroceryItem> getItems() {
    return this.items;
  }

  /**
   * Returns specified grocery items by id.
   *
   * @param id id of the stored grocery item
   * @return specified stored grocery item
   * @see GroceryItem
   */
  public GroceryItem getItem(long id) {
    for (GroceryItem item : this.items) {
      if (item.getId() == id) {
        return item;
      }
    }
    return null;
  }

  /**
   * Returns sales tax parsed from the file.
   *
   * @return sales tax
   */
  public BigDecimal getSalesTax() {
    return this.salesTax;
  }

  /**
   * Returns imported sales tax parsed from the file.
   *
   * @return imported sales tax
   */
  public BigDecimal getImportedSalesTax() {
    return this.importedSalesTax;
  }
}
