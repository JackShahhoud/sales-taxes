package com.sales_taxes.service;

import com.sales_taxes.dto.ItemDTO;
import com.sales_taxes.dto.PurchaseItemDTO;
import com.sales_taxes.dto.ReceiptDTO;
import com.sales_taxes.dto.ReceiptItemDTO;
import com.sales_taxes.entitiy.GroceryItem;
import com.sales_taxes.entitiy.ItemType;
import com.sales_taxes.utils.taxCalculator.GroceryTaxCalculator;
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
    List<ItemDTO> returnedItems = this.itemService.getItems();
    Assertions.assertEquals(returnedItems.size(), storedItems.size());
    List<GroceryItem> storedItemsList = new ArrayList<>(storedItems);
    for (int i = 0; i < storedItemsList.size(); ++i) {
      Assertions.assertEquals(storedItemsList.get(i).getId(), returnedItems.get(i).getId());
      Assertions.assertEquals(storedItemsList.get(i).getName(), returnedItems.get(i).getName());
      Assertions.assertEquals(storedItemsList.get(i).getPreTaxPrice().doubleValue(),
          returnedItems.get(i).getPreTaxPrice().doubleValue());
    }

  }

  @Test
  public void purchaseItems_input1Selected_shouldReturnReceipt() {
    LinkedHashSet<GroceryItem> storedItems = new LinkedHashSet<>();
    Random random = new Random();

    GroceryItem item1 = new GroceryItem();
    item1.setId(random.nextInt(Integer.MAX_VALUE));
    item1.setPreTaxPrice(BigDecimal.valueOf(12.49));
    item1.setName("book");
    item1.setType(ItemType.BOOK);
    storedItems.add(item1);

    GroceryItem item2 = new GroceryItem();
    item2.setId(random.nextInt(Integer.MAX_VALUE));
    item2.setPreTaxPrice(BigDecimal.valueOf(14.99));
    item2.setName("music CD");
    item2.setType(ItemType.OTHER);
    storedItems.add(item2);

    GroceryItem item3 = new GroceryItem();
    item3.setId(random.nextInt(Integer.MAX_VALUE));
    item3.setPreTaxPrice(BigDecimal.valueOf(0.85));
    item3.setName("chocolate bar");
    item3.setType(ItemType.FOOD);
    storedItems.add(item3);

    Mockito.when(this.itemRepository.getItem(item1.getId())).thenReturn(item1);
    Mockito.when(this.itemRepository.getItem(item2.getId())).thenReturn(item2);
    Mockito.when(this.itemRepository.getItem(item3.getId())).thenReturn(item3);
    Mockito.when(this.itemRepository.getSalesTax()).thenReturn(BigDecimal.valueOf(10));
    Mockito.when(this.itemRepository.getImportedSalesTax()).thenReturn(BigDecimal.valueOf(5));

    List<PurchaseItemDTO> purchaseItemsRequestDTOS = new ArrayList<>();
    PurchaseItemDTO purchaseItem1RequestDTO = new PurchaseItemDTO();
    PurchaseItemDTO purchaseItem2RequestDTO = new PurchaseItemDTO();
    PurchaseItemDTO purchaseItem3RequestDTO = new PurchaseItemDTO();

    purchaseItem1RequestDTO.setId(item1.getId());
    purchaseItem1RequestDTO.setAmount(1);
    purchaseItem2RequestDTO.setId(item2.getId());
    purchaseItem2RequestDTO.setAmount(1);
    purchaseItem3RequestDTO.setId(item3.getId());
    purchaseItem3RequestDTO.setAmount(1);

    purchaseItemsRequestDTOS.add(purchaseItem1RequestDTO);
    purchaseItemsRequestDTOS.add(purchaseItem2RequestDTO);
    purchaseItemsRequestDTOS.add(purchaseItem3RequestDTO);
    ReceiptDTO receiptDTO = this.itemService.purchase(purchaseItemsRequestDTOS);

    List<GroceryItem> storedItemsList = new ArrayList<>(storedItems);
    GroceryTaxCalculator groceryTaxCalculator =
        new GroceryTaxCalculator(this.itemRepository.getSalesTax(),
            this.itemRepository.getImportedSalesTax());

    List<ReceiptItemDTO> receiptItemDTOS = receiptDTO.getItems();
    for (int i = 0; i < purchaseItemsRequestDTOS.size(); ++i) {
      Assertions.assertEquals(receiptItemDTOS.get(i).getId(), storedItemsList.get(i).getId());
      Assertions.assertEquals(receiptItemDTOS.get(i).getName(), storedItemsList.get(i).getName());
      Assertions.assertEquals(receiptItemDTOS.get(i).getAmount(),
          purchaseItemsRequestDTOS.get(i).getAmount());
      Assertions.assertEquals(receiptItemDTOS.get(i).getPostTaxPrice().doubleValue(),
          storedItemsList.get(i).getPreTaxPrice()
              .add(groceryTaxCalculator.calculateTax(storedItemsList.get(i))).doubleValue());
    }

    Assertions.assertEquals(receiptDTO.getSalesTaxes().doubleValue(), 1.50);
    Assertions.assertEquals(receiptDTO.getTotal().doubleValue(),29.83);
  }

  @Test
  public void purchaseItems_input2Selected_shouldReturnReceipt() {
    LinkedHashSet<GroceryItem> storedItems = new LinkedHashSet<>();
    Random random = new Random();

    GroceryItem item1 = new GroceryItem();
    item1.setId(random.nextInt(Integer.MAX_VALUE));
    item1.setPreTaxPrice(BigDecimal.valueOf(10.00));
    item1.setName("imported box of chocolates");
    item1.setType(ItemType.FOOD);
    item1.setImported(true);
    storedItems.add(item1);

    GroceryItem item2 = new GroceryItem();
    item2.setId(random.nextInt(Integer.MAX_VALUE));
    item2.setPreTaxPrice(BigDecimal.valueOf(47.50));
    item2.setName("imported bottle of perfume");
    item2.setType(ItemType.OTHER);
    item2.setImported(true);
    storedItems.add(item2);

    Mockito.when(this.itemRepository.getItem(item1.getId())).thenReturn(item1);
    Mockito.when(this.itemRepository.getItem(item2.getId())).thenReturn(item2);
    Mockito.when(this.itemRepository.getSalesTax()).thenReturn(BigDecimal.valueOf(10));
    Mockito.when(this.itemRepository.getImportedSalesTax()).thenReturn(BigDecimal.valueOf(5));

    List<PurchaseItemDTO> purchaseItemsRequestDTOS = new ArrayList<>();
    PurchaseItemDTO purchaseItem1RequestDTO = new PurchaseItemDTO();
    PurchaseItemDTO purchaseItem2RequestDTO = new PurchaseItemDTO();

    purchaseItem1RequestDTO.setId(item1.getId());
    purchaseItem1RequestDTO.setAmount(1);
    purchaseItem2RequestDTO.setId(item2.getId());
    purchaseItem2RequestDTO.setAmount(1);

    purchaseItemsRequestDTOS.add(purchaseItem1RequestDTO);
    purchaseItemsRequestDTOS.add(purchaseItem2RequestDTO);
    ReceiptDTO receiptDTO = this.itemService.purchase(purchaseItemsRequestDTOS);

    List<GroceryItem> storedItemsList = new ArrayList<>(storedItems);
    GroceryTaxCalculator groceryTaxCalculator =
        new GroceryTaxCalculator(this.itemRepository.getSalesTax(),
            this.itemRepository.getImportedSalesTax());

    List<ReceiptItemDTO> receiptItemDTOS = receiptDTO.getItems();
    for (int i = 0; i < purchaseItemsRequestDTOS.size(); ++i) {
      Assertions.assertEquals(receiptItemDTOS.get(i).getId(), storedItemsList.get(i).getId());
      Assertions.assertEquals(receiptItemDTOS.get(i).getName(), storedItemsList.get(i).getName());
      Assertions.assertEquals(receiptItemDTOS.get(i).getAmount(),
          purchaseItemsRequestDTOS.get(i).getAmount());
      Assertions.assertEquals(receiptItemDTOS.get(i).getPostTaxPrice().doubleValue(),
          storedItemsList.get(i).getPreTaxPrice()
              .add(groceryTaxCalculator.calculateTax(storedItemsList.get(i))).doubleValue());
    }

    Assertions.assertEquals(receiptDTO.getSalesTaxes().doubleValue(), 7.65);
    Assertions.assertEquals(receiptDTO.getTotal().doubleValue(),65.15);
  }

  @Test
  public void purchaseItems_input3Selected_shouldReturnReceipt() {
    LinkedHashSet<GroceryItem> storedItems = new LinkedHashSet<>();
    Random random = new Random();

    GroceryItem item1 = new GroceryItem();
    item1.setId(random.nextInt(Integer.MAX_VALUE));
    item1.setPreTaxPrice(BigDecimal.valueOf(27.99));
    item1.setName("imported bottle of perfume");
    item1.setType(ItemType.OTHER);
    item1.setImported(true);
    storedItems.add(item1);

    GroceryItem item2 = new GroceryItem();
    item2.setId(random.nextInt(Integer.MAX_VALUE));
    item2.setPreTaxPrice(BigDecimal.valueOf(18.99));
    item2.setName("bottle of perfume");
    item2.setType(ItemType.OTHER);
    storedItems.add(item2);

    GroceryItem item3 = new GroceryItem();
    item3.setId(random.nextInt(Integer.MAX_VALUE));
    item3.setPreTaxPrice(BigDecimal.valueOf(9.75));
    item3.setName("packet of headache pills");
    item3.setType(ItemType.MEDICAL);
    storedItems.add(item3);

    GroceryItem item4 = new GroceryItem();
    item4.setId(random.nextInt(Integer.MAX_VALUE));
    item4.setPreTaxPrice(BigDecimal.valueOf(11.25));
    item4.setName("imported box of chocolates");
    item4.setType(ItemType.FOOD);
    item4.setImported(true);
    storedItems.add(item4);

    Mockito.when(this.itemRepository.getItem(item1.getId())).thenReturn(item1);
    Mockito.when(this.itemRepository.getItem(item2.getId())).thenReturn(item2);
    Mockito.when(this.itemRepository.getItem(item3.getId())).thenReturn(item3);
    Mockito.when(this.itemRepository.getItem(item4.getId())).thenReturn(item4);
    Mockito.when(this.itemRepository.getSalesTax()).thenReturn(BigDecimal.valueOf(10));
    Mockito.when(this.itemRepository.getImportedSalesTax()).thenReturn(BigDecimal.valueOf(5));

    List<PurchaseItemDTO> purchaseItemsRequestDTOS = new ArrayList<>();
    PurchaseItemDTO purchaseItem1RequestDTO = new PurchaseItemDTO();
    PurchaseItemDTO purchaseItem2RequestDTO = new PurchaseItemDTO();
    PurchaseItemDTO purchaseItem3RequestDTO = new PurchaseItemDTO();
    PurchaseItemDTO purchaseItem4RequestDTO = new PurchaseItemDTO();

    purchaseItem1RequestDTO.setId(item1.getId());
    purchaseItem1RequestDTO.setAmount(1);
    purchaseItem2RequestDTO.setId(item2.getId());
    purchaseItem2RequestDTO.setAmount(1);
    purchaseItem3RequestDTO.setId(item3.getId());
    purchaseItem3RequestDTO.setAmount(1);
    purchaseItem4RequestDTO.setId(item4.getId());
    purchaseItem4RequestDTO.setAmount(1);

    purchaseItemsRequestDTOS.add(purchaseItem1RequestDTO);
    purchaseItemsRequestDTOS.add(purchaseItem2RequestDTO);
    purchaseItemsRequestDTOS.add(purchaseItem3RequestDTO);
    purchaseItemsRequestDTOS.add(purchaseItem4RequestDTO);
    ReceiptDTO receiptDTO = this.itemService.purchase(purchaseItemsRequestDTOS);

    List<GroceryItem> storedItemsList = new ArrayList<>(storedItems);
    GroceryTaxCalculator groceryTaxCalculator =
        new GroceryTaxCalculator(this.itemRepository.getSalesTax(),
            this.itemRepository.getImportedSalesTax());

    List<ReceiptItemDTO> receiptItemDTOS = receiptDTO.getItems();
    for (int i = 0; i < purchaseItemsRequestDTOS.size(); ++i) {
      Assertions.assertEquals(receiptItemDTOS.get(i).getId(), storedItemsList.get(i).getId());
      Assertions.assertEquals(receiptItemDTOS.get(i).getName(), storedItemsList.get(i).getName());
      Assertions.assertEquals(receiptItemDTOS.get(i).getAmount(),
          purchaseItemsRequestDTOS.get(i).getAmount());
      Assertions.assertEquals(receiptItemDTOS.get(i).getPostTaxPrice().doubleValue(),
          storedItemsList.get(i).getPreTaxPrice()
              .add(groceryTaxCalculator.calculateTax(storedItemsList.get(i))).doubleValue());
    }

    Assertions.assertEquals(receiptDTO.getSalesTaxes().doubleValue(), 6.70);
    Assertions.assertEquals(receiptDTO.getTotal().doubleValue(),74.68);
  }


}
