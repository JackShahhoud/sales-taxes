package com.sales_taxes.dto;

import lombok.Data;

/**
 * PurchaseItemDTO is used to represent purchase request for specific item given id and amount
 * */
@Data
public class PurchaseItemDTO {

  long id;
  long amount;

}
