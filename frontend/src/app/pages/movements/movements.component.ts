import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { finalize } from 'rxjs/operators';
import { Movement } from '../../models/movement.model';
import { MovementService } from '../../services/movement.service';

type MsgType = 'success' | 'error' | '';

@Component({
  selector: 'app-movements',
  standalone: false,
  templateUrl: './movements.component.html',
  styleUrls: ['./movements.component.css'],
})
export class MovementsComponent implements OnInit {
  movements: Movement[] = [];
  filteredMovements: Movement[] = [];
  searchText = '';

  isLoading = false;
  messageText = '';
  messageType: MsgType = '';

  accountId: number | null = null;

  constructor(
    private readonly service: MovementService,
    private readonly route: ActivatedRoute,
    private readonly router: Router,
  ) {}

  ngOnInit(): void {
    const accountIdParam = this.route.snapshot.queryParamMap.get('accountId');
    this.accountId = accountIdParam ? Number(accountIdParam) : null;
    this.loadMovements();
  }

  loadMovements(): void {
    this.isLoading = true;
    this.messageText = '';
    this.messageType = '';

    const req$ = this.accountId
      ? this.service.findByAccountId(this.accountId)
      : this.service.findAll();

    req$.pipe(finalize(() => (this.isLoading = false))).subscribe({
      next: (data) => {
        this.movements = Array.isArray(data) ? data : [];
        this.applyFilter();
      },
      error: (err) => {
        this.movements = [];
        this.filteredMovements = [];
        this.messageType = 'error';
        this.messageText =
          this.getApiMessage(err) || 'Error cargando movimientos';
      },
    });
  }

  onSearchChange(value: string): void {
    this.searchText = value;
    this.applyFilter();
  }

  applyFilter(): void {
    const q = (this.searchText || '').trim().toLowerCase();
    if (!q) {
      this.filteredMovements = [...this.movements];
      return;
    }

    this.filteredMovements = this.movements.filter((m) => {
      const hay =
        `${m.movementDate} ${m.movementType} ${m.value} ${m.balance} ${m.accountId}`.toLowerCase();
      return hay.includes(q);
    });
  }

  deleteMovement(m: Movement): void {
    if (!window.confirm(`Eliminar movimiento #${m.id}?`)) return;

    this.isLoading = true;
    this.messageText = '';
    this.messageType = '';

    this.service
      .delete(m.id)
      .pipe(finalize(() => (this.isLoading = false)))
      .subscribe({
        next: () => {
          this.movements = this.movements.filter((x) => x.id !== m.id);
          this.applyFilter();
          this.messageType = 'success';
          this.messageText = 'Movimiento eliminado';
        },
        error: (err) => {
          this.messageType = 'error';
          this.messageText =
            this.getApiMessage(err) || 'Error eliminando movimiento';
        },
      });
  }

  goCreate(): void {
    this.router.navigate(['/movements/new']);
  }

  trackById(_: number, item: Movement) {
    return item.id;
  }

  private getApiMessage(err: any): string {
    const body = err?.error;

    if (body?.fields && typeof body.fields === 'object') {
      const firstKey = Object.keys(body.fields)[0];
      if (firstKey) return `${firstKey}: ${body.fields[firstKey]}`;
    }

    if (typeof body?.error === 'string') return body.error;
    if (typeof body?.message === 'string') return body.message;

    return '';
  }
}
