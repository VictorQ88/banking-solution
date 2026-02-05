import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { GenericService } from '../core/services/generic.service';
import { Client } from '../models/client.model';

@Injectable({ providedIn: 'root' })
export class ClientService extends GenericService<Client> {
  constructor(http: HttpClient) {
    super(http, '/clients');
  }
}
