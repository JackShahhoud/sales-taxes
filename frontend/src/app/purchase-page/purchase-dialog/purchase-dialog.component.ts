import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA} from '@angular/material/dialog';
import {Receipt} from "../../types/receipt";

@Component({
  selector: 'app-purchase-dialog',
  templateUrl: './purchase-dialog.component.html',
  styleUrls: ['./purchase-dialog.component.css']
})
export class PurchaseDialogComponent implements OnInit {

  receipt: Receipt;

  constructor(@Inject(MAT_DIALOG_DATA) public data: any) {
    this.receipt = data.receipt;
  }

  ngOnInit(): void {
  }

}
