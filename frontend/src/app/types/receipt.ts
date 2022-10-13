import {ReceiptItem} from "./receiptItem";

export interface Receipt {
  items: ReceiptItem[];
  salesTaxes: number;
  total: number;
}
