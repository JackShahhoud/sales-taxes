import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Item} from "./types/item";
import {environment} from "../environment/environment";

@Injectable({
  providedIn: 'root'
})
export class ItemService {

  constructor(private httpClient: HttpClient) { }

  public getItems() : Observable<Item[]> {
    return this.httpClient.get<Item[]>(environment.BACKEND_HOST+"/items/");
  }
}
