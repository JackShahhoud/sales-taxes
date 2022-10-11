package com.sales_taxes.services;

import com.sales_taxes.dto.ItemDTO;
import com.sales_taxes.entitiy.Item;
import com.sales_taxes.repository.ItemRepository;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemService {

  ItemRepository itemRepository;


  @Autowired
  public ItemService(ItemRepository itemRepository) {
    this.itemRepository = itemRepository;
  }

  public List<ItemDTO> getItems() {
    List<Item> itemsList = new ArrayList<>(this.itemRepository.getItems());
    List<ItemDTO> itemsDTO = new ArrayList<>();
    for (Item item : itemsList) {
      ItemDTO itemDTO = new ItemDTO();
      itemDTO.convertToDTO(item);
      itemsDTO.add(itemDTO);
    }
    return itemsDTO;
  }

}
