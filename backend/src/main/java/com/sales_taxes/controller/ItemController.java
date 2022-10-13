package com.sales_taxes.controller;

import com.sales_taxes.dto.ItemDTO;
import com.sales_taxes.dto.PurchaseItemDTO;
import com.sales_taxes.dto.ReceiptDTO;
import com.sales_taxes.services.ItemService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController()
@RequestMapping("/items")
public class ItemController {

  ItemService itemService;

  @Autowired
  public ItemController(ItemService itemService) {
    this.itemService = itemService;
  }

  @GetMapping("/")
  public List<ItemDTO> getItems() {
    return this.itemService.getItems();
  }

  @PostMapping(path = "/purchase/",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ReceiptDTO> create(
      @RequestBody List<PurchaseItemDTO> purchaseItemDTO) {
    ReceiptDTO receiptDTO = this.itemService.purchase(purchaseItemDTO);
    if (receiptDTO == null) {
      return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(receiptDTO,
        HttpStatus.CREATED);
  }

}
