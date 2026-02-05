import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { GenericService } from '../core/services/generic.service';
import { Movement } from '../models/movement.model';

@Injectable({ providedIn: 'root' })
export class MovementService extends GenericService<Movement> {
  constructor(http: HttpClient) {
    super(http, '/movements');
  }

  findByAccountId(accountId: number) {
    return this.http.get<Movement[]>(`${this.apiUrlService}/account/${accountId}`);
  }
}