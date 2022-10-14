package com.sales_taxes.services;

import com.sales_taxes.dto.ItemDTO;
import com.sales_taxes.dto.PurchaseItemDTO;
import com.sales_taxes.dto.ReceiptDTO;
import com.sales_taxes.dto.ReceiptItemDTO;
import com.sales_taxes.entitiy.GroceryItem;
import com.sales_taxes.utils.taxCalculator.GroceryTaxCalculator;
import com.sales_taxes.repository.ItemRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemService {

  ItemRepository itemRepository;


  @Autowired
  public ItemService(ItemRepository itemRepository) {
    this.itemRepository = itemRepository;
  }

  /**
   * Returns all stored items
   *
   * @return      list of all items
   * @see         ItemDTO
   */
  public List<ItemDTO> getItems() {
    List<GroceryItem> itemsList = new ArrayList<>(this.itemRepository.getItems());
    List<ItemDTO> itemsDTO = new ArrayList<>();
    for (GroceryItem item : itemsList) {
      ItemDTO itemDTO = new ItemDTO();
      itemDTO.convertToDTO(item);
      itemsDTO.add(itemDTO);
    }
    return itemsDTO;
  }

  /**
   * Returns the receipt for given items, calculating taxes and prices.
   *
   * @param  purchaseItems  list of wanted items to purchase
   * @return  Receipt including all items with pre- and post-tax prices,
   *          total sales taxes and total price
   * @see     ReceiptDTO
   */
  public ReceiptDTO purchase(List<PurchaseItemDTO> purchaseItems) {

    ReceiptDTO receiptDTO = new ReceiptDTO();
    GroceryTaxCalculator groceryTaxCalculator =
        new GroceryTaxCalculator(this.itemRepository.getSalesTax(),
            this.itemRepository.getImportedSalesTax());

    BigDecimal totalSalesTax = new BigDecimal(0);
    BigDecimal totalPrice = new BigDecimal(0);

    for (PurchaseItemDTO purchaseItemDTO : purchaseItems) {
      ReceiptItemDTO receiptItemDTO = new ReceiptItemDTO();
      receiptItemDTO.setAmount(purchaseItemDTO.getAmount());

      GroceryItem groceryItem = this.itemRepository.getItem(purchaseItemDTO.getId());
      if(groceryItem == null) {
        return null;
      }
      receiptItemDTO.setId(groceryItem.getId());
      receiptItemDTO.setName(groceryItem.getName());

      BigDecimal salesTax = groceryTaxCalculator.calculateTax(groceryItem);

      receiptItemDTO.setPreTaxPrice(groceryItem.getPreTaxPrice());
      receiptItemDTO.setPostTaxPrice(
          (groceryItem.getPreTaxPrice().add(salesTax)).multiply(
                  BigDecimal.valueOf(receiptItemDTO.getAmount()))
              .setScale(2, RoundingMode.UNNECESSARY));

      receiptDTO.addItem(receiptItemDTO);
      totalSalesTax =
          totalSalesTax.add(salesTax.multiply(BigDecimal.valueOf(receiptItemDTO.getAmount())));
      totalPrice = totalPrice.add(receiptItemDTO.getPostTaxPrice());

    }
    receiptDTO.setSalesTaxes(totalSalesTax.setScale(2, RoundingMode.UNNECESSARY));
    receiptDTO.setTotal(totalPrice.setScale(2, RoundingMode.UNNECESSARY));
    return receiptDTO;
  }

}
