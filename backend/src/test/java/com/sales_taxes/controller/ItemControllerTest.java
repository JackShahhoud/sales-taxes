package com.sales_taxes.controller;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sales_taxes.dto.ItemDTO;
import com.sales_taxes.dto.PurchaseItemDTO;
import com.sales_taxes.dto.ReceiptDTO;
import com.sales_taxes.entitiy.GroceryItem;
import com.sales_taxes.entitiy.ItemType;
import com.sales_taxes.repository.ItemRepository;
import com.sales_taxes.services.ItemService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
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
    LinkedHashSet<GroceryItem> storedItems = new LinkedHashSet<>();
    Random random = new Random();

    for (int i = 0; i < 10; ++i) {
      GroceryItem item = new GroceryItem();
      item.setId(random.nextInt(Integer.MAX_VALUE));
      item.setPreTaxPrice(BigDecimal.valueOf(i * i));
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
        shopItem.setPreTaxPrice(BigDecimal.valueOf(item.get("preTaxPrice").asDouble()));
        returnedItems.add(shopItem);
      }
    }

    List<GroceryItem> storedItemsList = new ArrayList<>(storedItems);

    Assertions.assertSame(returnedItems.size(), storedItemsList.size());

    for (int i = 0; i < storedItemsList.size(); ++i) {
      Assertions.assertEquals(storedItemsList.get(i).getId(), returnedItems.get(i).getId());
      Assertions.assertEquals(storedItemsList.get(i).getName(), returnedItems.get(i).getName());
      Assertions.assertEquals(storedItemsList.get(i).getPreTaxPrice().doubleValue(),
          returnedItems.get(i).getPreTaxPrice().doubleValue());
    }

  }

  @Test
  public void purchaseItems_selectedItems_shouldReturnReceipt() throws Exception {
    Random random = new Random();
    ObjectMapper mapper = new ObjectMapper();

    GroceryItem item1 = new GroceryItem();
    item1.setId(random.nextInt(Integer.MAX_VALUE));
    item1.setPreTaxPrice(BigDecimal.valueOf(10.00));
    item1.setName("book");
    item1.setType(ItemType.BOOK);

    List<PurchaseItemDTO> purchaseItemsRequestDTOS = new ArrayList<>();
    PurchaseItemDTO purchaseItemDTO = new PurchaseItemDTO();
    purchaseItemDTO.setId(item1.getId());
    purchaseItemDTO.setAmount(1);

    purchaseItemsRequestDTOS.add(purchaseItemDTO);

    Mockito.when(this.itemRepository.getItem(item1.getId())).thenReturn(item1);
    MvcResult requestResult =
        this.mockMvc.perform(post("/items/purchase/", purchaseItemDTO).contentType(
                    MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(purchaseItemsRequestDTOS))).andDo(print())
            .andExpect(status().isCreated())
            .andReturn();

    JsonNode itemsJson = mapper.readTree(requestResult.getResponse().getContentAsString());
    ReceiptDTO receiptDTO = mapper.treeToValue(itemsJson, ReceiptDTO.class);

    Assertions.assertEquals(receiptDTO.getSalesTaxes().doubleValue(), 0.00);
    Assertions.assertEquals(receiptDTO.getTotal().doubleValue(), 10.00);
    Assertions.assertEquals(receiptDTO.getItems().size(), purchaseItemsRequestDTOS.size());

    Assertions.assertEquals(receiptDTO.getItems().get(0).getId(), item1.getId());
    Assertions.assertEquals(receiptDTO.getItems().get(0).getName(), item1.getName());
    Assertions.assertEquals(receiptDTO.getItems().get(0).getPostTaxPrice().doubleValue(), 10.00);
    Assertions.assertEquals(receiptDTO.getItems().get(0).getPreTaxPrice().doubleValue(), 10.00);
    Assertions.assertEquals(receiptDTO.getItems().get(0).getAmount(), 1);
  }

  @Test
  public void purchaseItems_itemNotExist_shouldReturnNotFound() throws Exception {
    Random random = new Random();
    ObjectMapper mapper = new ObjectMapper();

    GroceryItem item1 = new GroceryItem();
    item1.setId(random.nextInt(Integer.MAX_VALUE));
    item1.setPreTaxPrice(BigDecimal.valueOf(10.00));
    item1.setName("book");
    item1.setType(ItemType.BOOK);

    List<PurchaseItemDTO> purchaseItemsRequestDTOS = new ArrayList<>();
    PurchaseItemDTO purchaseItemDTO = new PurchaseItemDTO();
    purchaseItemDTO.setId(random.nextInt(Integer.MAX_VALUE));
    purchaseItemDTO.setAmount(1);

    purchaseItemsRequestDTOS.add(purchaseItemDTO);

    Mockito.when(this.itemRepository.getItem(item1.getId())).thenReturn(item1);
    MvcResult requestResult =
        this.mockMvc.perform(post("/items/purchase/", purchaseItemDTO).contentType(
                    MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(purchaseItemsRequestDTOS))).andDo(print())
            .andExpect(status().isNotFound())
            .andReturn();
  }

}
