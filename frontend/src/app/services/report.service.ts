import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class ReportService {
  protected readonly apiUrl = environment.apiUrl;
  private readonly baseUrl = `${this.apiUrl}/reportes`;

  constructor(private http: HttpClient) {}

  getAccountStatement(clientId: number, from: string, to: string) {
    const params = new HttpParams()
      .set('clientId', clientId)
      .set('from', from)
      .set('to', to);

    return this.http.get<any>(this.baseUrl, { params });
  }

  downloadPdf(clientId: number, from: string, to: string) {
    const params = new HttpParams()
      .set('clientId', clientId)
      .set('from', from)
      .set('to', to)
      .set('format', 'pdf');

    return this.http.get<any>(this.baseUrl, { params });
  }
}
