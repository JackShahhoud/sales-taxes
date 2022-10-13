package com.sales_taxes.utils.taxCalculator;

import com.sales_taxes.entitiy.Item;
import java.math.BigDecimal;

public interface TaxCalculator {

  BigDecimal calculateTax(Item item);

}
