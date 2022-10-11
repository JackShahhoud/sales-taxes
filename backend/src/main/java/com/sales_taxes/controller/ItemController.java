package com.sales_taxes.controller;

import com.sales_taxes.dto.ItemDTO;
import com.sales_taxes.services.ItemService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController("")
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

}
