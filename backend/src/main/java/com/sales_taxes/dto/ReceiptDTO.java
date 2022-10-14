package com.sales_taxes.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 * ReceiptDTO is used to document all purchased items with pre- and post-tax prices,
 * total sales taxes and price
 * */
@Data
public class ReceiptDTO {

  List<ReceiptItemDTO> items = new ArrayList<>();
  BigDecimal salesTaxes;
  BigDecimal total;

  public void addItem(ReceiptItemDTO receiptItemDTO) {
    this.items.add(receiptItemDTO);
  }

}
