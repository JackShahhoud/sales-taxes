package com.sales_taxes.entitiy;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class GroceryItem extends Item {

  boolean imported;
  ItemType type;

}
