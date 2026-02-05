import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { finalize } from 'rxjs/operators';

import { Account } from '../../models/account.model';
import { AccountService } from '../../services/account.service';

@Component({
  selector: 'app-accounts',
  standalone: false,
  templateUrl: './accounts.component.html',
  styleUrls: ['./accounts.component.css'],
})
export class AccountsComponent implements OnInit {
  accounts: Account[] = [];
  filteredAccounts: Account[] = [];
  searchText = '';

  isLoading = false;
  errorMessage = '';
  successMessage = '';

  constructor(
    private readonly accountService: AccountService,
    private readonly router: Router,
    private readonly route: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.route.queryParamMap.subscribe((p) => {
      const msg = p.get('msg');
      if (msg === 'created') this.successMessage = 'Cuenta creada';
      if (msg === 'updated') this.successMessage = 'Cuenta actualizada';

      if (msg) {
        this.router.navigate([], { relativeTo: this.route, queryParams: {}, replaceUrl: true });
      }
    });

    this.loadAccounts();
  }

  loadAccounts(): void {
    this.isLoading = true;
    this.errorMessage = '';

    this.accountService
      .findAll()
      .pipe(finalize(() => (this.isLoading = false)))
      .subscribe({
        next: (data) => {
          this.accounts = Array.isArray(data) ? data : [];
          this.applyFilter();
        },
        error: (err) => {
          this.errorMessage = this.getApiMessage(err) || 'Error cargando cuentas';
          this.accounts = [];
          this.filteredAccounts = [];
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
      this.filteredAccounts = [...this.accounts];
      return;
    }

    this.filteredAccounts = this.accounts.filter((a) => {
      const hay = `${a.accountNumber} ${a.accountType} ${a.clientId} ${a.initialBalance}`.toLowerCase();
      return hay.includes(q);
    });
  }

  goCreate(): void {
    this.router.navigate(['/accounts/new']);
  }

  goEdit(account: Account): void {
    this.router.navigateByUrl(`/accounts/${account.id}/edit`);
  }

  deleteAccount(account: Account): void {
    if (!window.confirm(`Eliminar cuenta "${account.accountNumber}"?`)) return;

    this.isLoading = true;
    this.errorMessage = '';

    this.accountService
      .delete(account.id)
      .pipe(finalize(() => (this.isLoading = false)))
      .subscribe({
        next: () => {
          this.accounts = this.accounts.filter((x) => x.id !== account.id);
          this.applyFilter();
        },
        error: (err) => {
          this.errorMessage = this.getApiMessage(err) || 'Error eliminando cuenta';
        },
      });
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

  trackById(_: number, item: Account) {
    return item.id;
  }
}