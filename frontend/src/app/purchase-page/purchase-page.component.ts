import {Component, OnInit} from '@angular/core';
import {Item} from "../types/item";
import {ItemService} from "../item.service";
import {MatTableDataSource} from "@angular/material/table";
import {MatSnackBar} from "@angular/material/snack-bar";
import {PurchaseItem} from "../types/purchaseItem";
import {MatDialog} from "@angular/material/dialog";
import {PurchaseDialogComponent} from "./purchase-dialog/purchase-dialog.component";

@Component({
  selector: 'app-purchase-page',
  templateUrl: './purchase-page.component.html',
  styleUrls: ['./purchase-page.component.css']
})
export class PurchasePageComponent implements OnInit {


  constructor(private itemService: ItemService,
              private snackBar: MatSnackBar, public dialog: MatDialog) {
  }


  items: Item[] = [];
  dataSource = new MatTableDataSource<Item>();
  displayedColumns: string[] = ['name', 'price', 'amount'];

  ngOnInit(): void {
    this.itemService.getItems().subscribe(itemsList => {
      itemsList.forEach(item => {
        item.amount = 0;
        this.items.push(item);
      });
      this.dataSource.data = this.items;
    }, _ => {
      this.snackBar.open("connection unavailable", "close");
    });
  }

  selectInputOne() {
    for (let item of this.items) {
      if (item.name === 'book' && item.preTaxPrice === 12.49) {
        item.amount = 1
      } else if (item.name === 'music CD' && item.preTaxPrice === 14.99) {
        item.amount = 1
      } else if (item.name === 'chocolate bar' && item.preTaxPrice === 0.85) {
        item.amount = 1
      } else {
        item.amount = 0
      }
    }
  }

  selectInputTwo() {
    for (let item of this.items) {
      if (item.name === 'imported box of chocolates' && item.preTaxPrice === 10.00) {
        item.amount = 1
      } else if (item.name === 'imported bottle of perfume' && item.preTaxPrice === 47.50) {
        item.amount = 1
      } else {
        item.amount = 0
      }
    }

  }

  selectInputThree() {
    for (let item of this.items) {
      if (item.name === 'imported bottle of perfume' && item.preTaxPrice === 27.99) {
        item.amount = 1
      } else if (item.name === 'bottle of perfume' && item.preTaxPrice === 18.99) {
        item.amount = 1
      } else if (item.name === 'packet of headache pills' && item.preTaxPrice === 9.75) {
        item.amount = 1
      } else if (item.name === 'box of imported chocolates' && item.preTaxPrice === 11.25) {
        item.amount = 1
      } else {
        item.amount = 0
      }
    }

  }

  purchase() {
    let purchaseItems: PurchaseItem[] = []
    for (let item of this.items) {
      if (item.amount > 0) {
        purchaseItems.push({id: item.id, amount: item.amount})
      }
    }
    if (purchaseItems.length === 0) {
      this.snackBar.open("You should choose at least one product", "close");
      return;
    }
    this.itemService.purchase(purchaseItems).subscribe(receipt => {
      this.dialog.open(PurchaseDialogComponent, {
        width: "45vw",
        maxWidth: "100%",
        maxHeight: "100%",
        data: {
          receipt,
        },
      });
    }, _ => {
      this.snackBar.open("connection unavailable", "close");
    })

  }
}
