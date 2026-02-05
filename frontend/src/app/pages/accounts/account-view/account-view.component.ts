import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { finalize } from 'rxjs/operators';

import { Account } from '../../../models/account.model';
import { AccountService } from '../../../services/account.service';
import { Movement } from '../../../models/movement.model';
import { MovementService } from '../../../services/movement.service';

@Component({
  selector: 'app-account-view',
  standalone: false,
  templateUrl: './account-view.component.html',
  styleUrls: ['./account-view.component.css'],
})
export class AccountViewComponent implements OnInit {
  id: number | null = null;

  account: Account | null = null;
  movements: Movement[] = [];

  isLoadingAccount = false;
  isLoadingMovements = false;

  errorMessage = '';

  constructor(
    private readonly route: ActivatedRoute,
    private readonly router: Router,
    private readonly accountService: AccountService,
    private readonly movementService: MovementService,
  ) {}

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    if (!idParam) return;

    this.id = Number(idParam);
    this.loadAccount(this.id);
    this.loadMovements(this.id);
  }

  back(): void {
    this.router.navigate(['/accounts']);
  }

  private loadAccount(id: number): void {
    this.isLoadingAccount = true;
    this.errorMessage = '';

    this.accountService
      .findById(id)
      .pipe(finalize(() => (this.isLoadingAccount = false)))
      .subscribe({
        next: (a) => (this.account = a),
        error: (err) =>
          (this.errorMessage =
            this.getApiMessage(err) || 'Error cargando cuenta'),
      });
  }

  private loadMovements(accountId: number): void {
    this.isLoadingMovements = true;

    this.movementService
      .findByAccountId(accountId)
      .pipe(finalize(() => (this.isLoadingMovements = false)))
      .subscribe({
        next: (data) => (this.movements = Array.isArray(data) ? data : []),
        error: (err) =>
          (this.errorMessage =
            this.getApiMessage(err) || 'Error cargando movimientos'),
      });
  }

  trackByMovementId(_: number, m: Movement) {
    return m.id;
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