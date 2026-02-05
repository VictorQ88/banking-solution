import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { finalize } from 'rxjs/operators';

import { Account } from '../../../models/account.model';
import { AccountService } from '../../../services/account.service';

type Mode = 'create' | 'edit';

@Component({
  selector: 'app-account-form',
  standalone: false,
  templateUrl: './account-form.component.html',
  styleUrls: ['./account-form.component.css'],
})
export class AccountFormComponent implements OnInit {
  mode: Mode = 'create';
  id: number | null = null;

  isLoading = false;
  errorMessage = '';
  form: any;

  constructor(
    private readonly fb: FormBuilder,
    private readonly route: ActivatedRoute,
    private readonly router: Router,
    private readonly accountService: AccountService,
  ) {}

  ngOnInit(): void {
    this.form = this.fb.nonNullable.group({
      accountType: ['', [Validators.required, Validators.maxLength(20)]],
      initialBalance: [0, [Validators.required]],
      active: [true, [Validators.required]],
      clientId: [0, [Validators.required]],
    });

    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      this.mode = 'edit';
      this.id = Number(idParam);
      this.loadAccount(this.id);
    } else {
      this.mode = 'create';
      this.id = null;
    }
  }

  cancel(): void {
    this.router.navigate(['/accounts']);
  }

  submit(): void {
    this.errorMessage = '';
    this.form.markAllAsTouched();
    if (this.form.invalid) return;

    if (this.mode === 'create') this.createAccount();
    else this.updateAccount();
  }

  private loadAccount(id: number): void {
    this.isLoading = true;

    this.accountService
      .findById(id)
      .pipe(finalize(() => (this.isLoading = false)))
      .subscribe({
        next: (a) => this.patchAccount(a),
        error: (err) =>
          (this.errorMessage =
            this.getApiMessage(err) || 'Error cargando cuenta'),
      });
  }

  private patchAccount(a: Account): void {
    this.form.patchValue({
      accountType: a.accountType,
      initialBalance: a.initialBalance,
      active: a.active,
      clientId: a.clientId,
    });
  }

  private createAccount(): void {
    this.isLoading = true;

    const payload = this.form.getRawValue();
    const req = payload as Account;

    this.accountService
      .create(req)
      .pipe(finalize(() => (this.isLoading = false)))
      .subscribe({
        next: () =>
          this.router.navigate(['/accounts'], {
            queryParams: { msg: 'created' },
          }),
        error: (err) =>
          (this.errorMessage =
            this.getApiMessage(err) || 'Error creando cuenta'),
      });
  }

  private updateAccount(): void {
    if (this.id == null) return;

    this.isLoading = true;

    const payload = this.form.getRawValue();

    this.accountService
      .update(this.id, payload)
      .pipe(finalize(() => (this.isLoading = false)))
      .subscribe({
        next: () =>
          this.router.navigate(['/accounts'], {
            queryParams: { msg: 'updated' },
          }),
        error: (err) =>
          (this.errorMessage =
            this.getApiMessage(err) || 'Error actualizando cuenta'),
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
}
