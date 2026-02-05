import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { GenericService } from '../core/services/generic.service';
import { Account } from '../models/account.model';

@Injectable({ providedIn: 'root' })
export class AccountService extends GenericService<Account> {
  constructor(http: HttpClient) {
    super(http, '/accounts');
  }
}
