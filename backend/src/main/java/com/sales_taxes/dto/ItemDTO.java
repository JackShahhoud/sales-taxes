package com.sales_taxes.dto;

import com.sales_taxes.entitiy.Item;
import lombok.Data;

@Data
public class ItemDTO {

  long id;

  String name;

  double price;


  public void convertToDTO(Item item) {
    this.id = item.getId();
    this.name = item.getName();
    this.price = item.getPrice();
  }

}
