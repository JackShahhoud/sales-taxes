package com.sales_taxes.dto;

import java.math.BigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ReceiptItemDTO extends ItemDTO {

  long amount;
  BigDecimal postTaxPrice;
}
