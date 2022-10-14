package com.sales_taxes.utils.taxCalculator;

import com.sales_taxes.dto.ReceiptDTO;
import com.sales_taxes.entitiy.GroceryItem;
import com.sales_taxes.entitiy.Item;
import com.sales_taxes.entitiy.ItemType;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class GroceryTaxCalculator implements TaxCalculator {

  BigDecimal salesTax;
  BigDecimal importedSalesTax;

  public GroceryTaxCalculator(BigDecimal salesTax, BigDecimal importedSalesTax) {
    this.salesTax = salesTax;
    this.importedSalesTax = importedSalesTax;
  }


  /**
   * Calculates taxes according to the items.
   * Items that excepted from sales tax: BOOK, FOOD, MEDICAL.
   * Imported items have additional import sales tax.
   * @return sales tax applied on given item.
   * @see ItemType
   * */
  @Override
  public BigDecimal calculateTax(Item item) {
    GroceryItem groceryItem = (GroceryItem) item;
    BigDecimal taxApplied = new BigDecimal(0);
    if (groceryItem.isImported()) {
      taxApplied = taxApplied.add(importedSalesTax);
    }
    if (!groceryItem.getType().equals(ItemType.FOOD) &&
        !groceryItem.getType().equals(ItemType.BOOK) &&
        !groceryItem.getType().equals(ItemType.MEDICAL)) {
      taxApplied = taxApplied.add(salesTax);
    }

    BigDecimal salesTax =
        groceryItem.getPreTaxPrice().multiply(taxApplied).divide(BigDecimal.valueOf(100))
            .multiply(BigDecimal.valueOf(20));
    salesTax = salesTax.setScale(0, RoundingMode.CEILING).divide(BigDecimal.valueOf(20))
        .setScale(2, RoundingMode.UNNECESSARY);

    return salesTax;
  }
}
