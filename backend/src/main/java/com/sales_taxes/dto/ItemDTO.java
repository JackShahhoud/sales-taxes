package com.sales_taxes.dto;

import com.sales_taxes.entitiy.GroceryItem;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class ItemDTO {

  long id;
  String name;
  BigDecimal preTaxPrice;

  public void convertToDTO(GroceryItem item) {
    this.id = item.getId();
    this.name = item.getName();
    this.preTaxPrice = item.getPreTaxPrice();
  }

}
