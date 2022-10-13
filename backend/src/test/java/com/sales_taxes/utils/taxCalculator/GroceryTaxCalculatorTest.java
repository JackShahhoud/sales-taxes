package com.sales_taxes.utils.taxCalculator;

import com.sales_taxes.entitiy.GroceryItem;
import com.sales_taxes.entitiy.ItemType;
import java.math.BigDecimal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class GroceryTaxCalculatorTest {


  @Test
  public void calculateTax_bookItem_shouldNotApplySalesTax() {
    GroceryTaxCalculator groceryTaxCalculator = new GroceryTaxCalculator(BigDecimal.valueOf(10),
        BigDecimal.valueOf(5));

    GroceryItem groceryItem = new GroceryItem();
    groceryItem.setName("book");
    groceryItem.setType(ItemType.BOOK);
    groceryItem.setPreTaxPrice(BigDecimal.valueOf(10.00));
    groceryItem.setImported(false);

    Assertions.assertEquals(groceryTaxCalculator.calculateTax(groceryItem).doubleValue(), 0);
  }

  @Test
  public void calculateTax_otherItem_shouldApplySalesTax() {
    GroceryTaxCalculator groceryTaxCalculator = new GroceryTaxCalculator(BigDecimal.valueOf(10),
        BigDecimal.valueOf(5));

    GroceryItem groceryItem = new GroceryItem();
    groceryItem.setName("music CD");
    groceryItem.setType(ItemType.OTHER);
    groceryItem.setPreTaxPrice(BigDecimal.valueOf(10.00));
    groceryItem.setImported(false);

    Assertions.assertEquals(groceryTaxCalculator.calculateTax(groceryItem).doubleValue(), 1);
  }

  @Test
  public void calculateTax_foodItem_shouldApplySalesTax() {
    GroceryTaxCalculator groceryTaxCalculator = new GroceryTaxCalculator(BigDecimal.valueOf(10),
        BigDecimal.valueOf(5));

    GroceryItem groceryItem = new GroceryItem();
    groceryItem.setName("chocolate bar");
    groceryItem.setType(ItemType.FOOD);
    groceryItem.setPreTaxPrice(BigDecimal.valueOf(10.00));
    groceryItem.setImported(false);

    Assertions.assertEquals(groceryTaxCalculator.calculateTax(groceryItem).doubleValue(), 0);
  }

  @Test
  public void calculateTax_medicalItem_shouldApplySalesTax() {
    GroceryTaxCalculator groceryTaxCalculator = new GroceryTaxCalculator(BigDecimal.valueOf(10),
        BigDecimal.valueOf(5));

    GroceryItem groceryItem = new GroceryItem();
    groceryItem.setName("pills");
    groceryItem.setType(ItemType.MEDICAL);
    groceryItem.setPreTaxPrice(BigDecimal.valueOf(10.00));
    groceryItem.setImported(false);

    Assertions.assertEquals(groceryTaxCalculator.calculateTax(groceryItem).doubleValue(), 0);
  }

  @Test
  public void calculateTax_importedItem_shouldApplyImportTax() {
    GroceryTaxCalculator groceryTaxCalculator = new GroceryTaxCalculator(BigDecimal.valueOf(10),
        BigDecimal.valueOf(5));

    GroceryItem groceryItem = new GroceryItem();
    groceryItem.setName("book");
    groceryItem.setType(ItemType.BOOK);
    groceryItem.setPreTaxPrice(BigDecimal.valueOf(10.00));
    groceryItem.setImported(true);

    Assertions.assertEquals(groceryTaxCalculator.calculateTax(groceryItem).doubleValue(), 0.5);
  }

  @Test
  public void calculateTax_importedAndOtherItem_shouldApplyImportAndSalesTax() {
    GroceryTaxCalculator groceryTaxCalculator = new GroceryTaxCalculator(BigDecimal.valueOf(10),
        BigDecimal.valueOf(5));

    GroceryItem groceryItem = new GroceryItem();
    groceryItem.setName("music CD");
    groceryItem.setType(ItemType.OTHER);
    groceryItem.setPreTaxPrice(BigDecimal.valueOf(10.00));
    groceryItem.setImported(true);

    Assertions.assertEquals(groceryTaxCalculator.calculateTax(groceryItem).doubleValue(), 1.5);
  }
}
