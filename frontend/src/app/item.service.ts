import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Item} from "./types/item";
import {environment} from "../environments/environment.prod";
import {Receipt} from "./types/receipt";
import {PurchaseItem} from "./types/purchaseItem";

@Injectable({
  providedIn: 'root'
})
export class ItemService {

  constructor(private httpClient: HttpClient) {
  }

  public getItems(): Observable<Item[]> {
    return this.httpClient.get<Item[]>(environment.BACKEND_HOST + "/items/");
  }

  public purchase(items: PurchaseItem[]): Observable<Receipt> {
    return this.httpClient.post<Receipt>(environment.BACKEND_HOST + "/items/purchase/", items);
  }
}
