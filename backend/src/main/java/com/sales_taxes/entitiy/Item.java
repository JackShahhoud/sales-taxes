package com.sales_taxes.entitiy;

import java.math.BigDecimal;
import lombok.Data;

@Data
public abstract class Item {

  long id;
  String name;
  BigDecimal preTaxPrice;

}
