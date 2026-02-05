import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { environment } from '../../../environments/environment';

export abstract class GenericService<T> {
  protected readonly apiUrl = environment.apiUrl;
  protected readonly apiUrlService: string = "";

  constructor(
    protected http: HttpClient,
    protected resource: string,
  ) {
    this.apiUrlService = `${this.apiUrl}${this.resource}`;
  }

  findAll(): Observable<T[]> {
    return this.http
      .get<T[]>(`${this.apiUrlService}`)
      .pipe(catchError(this.handleError));
  }

  findById(id: number | string): Observable<T> {
    return this.http
      .get<T>(`${this.apiUrlService}/${id}`)
      .pipe(catchError(this.handleError));
  }

  create(data: T): Observable<T> {
    return this.http
      .post<T>(`${this.apiUrlService}`, data)
      .pipe(catchError(this.handleError));
  }

  update(id: number | string, data: Partial<T>): Observable<T> {
    return this.http
      .put<T>(`${this.apiUrlService}/${id}`, data)
      .pipe(catchError(this.handleError));
  }

  delete(id: number | string): Observable<void> {
    return this.http
      .delete<void>(`${this.apiUrlService}/${id}`)
      .pipe(catchError(this.handleError));
  }

  protected handleError(error: HttpErrorResponse) {
    console.error('API Error:', error);
    return throwError(() => error);
  }
}
