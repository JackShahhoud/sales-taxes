package com.sales_taxes.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class ReceiptDTO {

  List<ReceiptItemDTO> items = new ArrayList<>();
  BigDecimal salesTaxes;
  BigDecimal total;

  public void addItem(ReceiptItemDTO receiptItemDTO) {
    this.items.add(receiptItemDTO);
  }

}
