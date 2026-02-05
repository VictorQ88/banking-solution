import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export abstract class BaseService<T, TCreate, TUpdate> {
  protected constructor(
    protected readonly http: HttpClient,
    private readonly resourceUrl: string,
  ) {}

  findAll(): Observable<T[]> {
    return this.http.get<T[]>(this.resourceUrl);
  }

  findById(id: number): Observable<T> {
    return this.http.get<T>(`${this.resourceUrl}/${id}`);
  }

  create(payload: TCreate): Observable<T> {
    return this.http.post<T>(this.resourceUrl, payload);
  }

  update(id: number, payload: TUpdate): Observable<T> {
    return this.http.put<T>(`${this.resourceUrl}/${id}`, payload);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.resourceUrl}/${id}`);
  }
}
