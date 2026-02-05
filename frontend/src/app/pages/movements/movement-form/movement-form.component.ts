import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { finalize } from 'rxjs/operators';

import { Client } from '../../../models/client.model';
import { Account } from '../../../models/account.model';
import { Movement } from '../../../models/movement.model';

import { ClientService } from '../../../services/client.service';
import { AccountService } from '../../../services/account.service';
import { MovementService } from '../../../services/movement.service';

@Component({
  selector: 'app-movement-form',
  standalone: false,
  templateUrl: './movement-form.component.html',
  styleUrls: ['./movement-form.component.css'],
})
export class MovementFormComponent implements OnInit {
  form: any;
  isLoading = false;
  errorMessage = '';

  clients: Client[] = [];
  accounts: Account[] = [];

  constructor(
    private readonly fb: FormBuilder,
    private readonly router: Router,
    private readonly clientService: ClientService,
    private readonly accountService: AccountService,
    private readonly movementService: MovementService,
  ) {}

  ngOnInit(): void {
    this.form = this.fb.nonNullable.group({
      clientId: ['', Validators.required],
      accountId: ['', Validators.required],
      movementType: ['', Validators.required],
      value: [0, [Validators.required, Validators.min(1)]],
    });

    this.loadClients();

    this.form.controls.clientId.valueChanges.subscribe((clientId: number) => {
      this.form.controls.accountId.reset('');
      this.accounts = [];

      if (clientId) {
        this.loadAccounts(clientId);
      }
    });
  }

  private loadClients(): void {
    this.clientService.findAll().subscribe({
      next: (data) => (this.clients = data.filter((c) => c.active)),
      error: () => (this.errorMessage = 'Error cargando clientes'),
    });
  }

  private loadAccounts(clientId: number): void {
    this.accountService.findAll().subscribe({
      next: (data) => {
        this.accounts = data.filter((a) => a.clientId === clientId && a.active);
      },
      error: () => (this.errorMessage = 'Error cargando cuentas'),
    });
  }

  submit(): void {
    this.errorMessage = '';
    this.form.markAllAsTouched();
    if (this.form.invalid) return;

    const payload = this.form.getRawValue() as Movement;

    this.isLoading = true;
    this.movementService
      .create(payload)
      .pipe(finalize(() => (this.isLoading = false)))
      .subscribe({
        next: () =>
          this.router.navigate(['/movements'], {
            queryParams: { msg: 'created' },
          }),
        error: (err) =>
          (this.errorMessage =
            err?.error?.message || 'Error registrando movimiento'),
      });
  }

  cancel(): void {
    this.router.navigate(['/movements']);
  }

  trackByClientId(_: number, c: Client) {
    return c.id;
  }

  trackByAccountId(_: number, a: Account) {
    return a.id;
  }
}
