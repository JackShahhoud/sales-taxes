import {Component, OnInit} from '@angular/core';
import {Item} from "../types/item";
import {HttpClient} from "@angular/common/http";
import {ItemService} from "../item.service";
import {MatTableDataSource} from "@angular/material/table";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: 'app-purchase-page',
  templateUrl: './purchase-page.component.html',
  styleUrls: ['./purchase-page.component.css']
})
export class PurchasePageComponent implements OnInit {


  constructor(private itemService: ItemService, private snackBar: MatSnackBar) {
  }


  items: Item[] = [];
  dataSource = new MatTableDataSource<Item>();
  displayedColumns: string[] = ['name', 'price', 'amount'];

  ngOnInit(): void {
    this.itemService.getItems().subscribe(itemsList => {
      itemsList.forEach(item => {
        this.items.push(item);
      });
      this.dataSource.data = this.items;
    }, _ => {
      this.snackBar.open("connection unavailable", "close");
    });
  }
}
