import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Client, ClientCreateRequest, ClientUpdateRequest } from './client.model';
import { BaseService } from '../../core/services/base.service';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class ClientService extends BaseService<Client, ClientCreateRequest, ClientUpdateRequest> {
  constructor(http: HttpClient) {
    super(http, `${environment.apiBaseUrl}/clients`);
  }
}
