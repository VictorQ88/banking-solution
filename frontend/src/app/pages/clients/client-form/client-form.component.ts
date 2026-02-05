import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { finalize } from 'rxjs/operators';

import { Client } from '../../../models/client.model';
import { ClientService } from '../../../services/client.service';

type Mode = 'create' | 'edit';

@Component({
  selector: 'app-client-form',
  standalone: false,
  templateUrl: './client-form.component.html',
  styleUrls: ['./client-form.component.css'],
})
export class ClientFormComponent implements OnInit {
  mode: Mode = 'create';
  id: number | null = null;

  isLoading = false;
  errorMessage = '';
  form: any;

  constructor(
    private readonly fb: FormBuilder,
    private readonly route: ActivatedRoute,
    private readonly router: Router,
    private readonly clientService: ClientService,
  ) {}

  ngOnInit(): void {
    this.form = this.fb.nonNullable.group({
      name: ['', [Validators.required, Validators.maxLength(120)]],
      gender: ['', [Validators.required, Validators.maxLength(20)]],
      age: [18, [Validators.required, Validators.min(18), Validators.max(130)]],
      identification: ['', [Validators.required, Validators.maxLength(20)]],
      address: ['', [Validators.required, Validators.maxLength(200)]],
      phone: ['', [Validators.required, Validators.maxLength(20)]],
      password: [
        '',
        [
          Validators.required,
          Validators.minLength(4),
          Validators.maxLength(120),
        ],
      ],
      active: [true, [Validators.required]],
    });
    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      this.mode = 'edit';
      this.id = Number(idParam);
      this.setPasswordOptional();
      this.loadClient(this.id);
    } else {
      this.mode = 'create';
      this.id = null;
      this.setPasswordRequired();
    }
  }

  cancel(): void {
    this.router.navigate(['/clients']);
  }

  submit(): void {
    this.errorMessage = '';
    this.form.markAllAsTouched();
    if (this.form.invalid) return;

    if (this.mode === 'create') {
      this.createClient();
    } else {
      this.updateClient();
    }
  }

  private loadClient(id: number): void {
    this.isLoading = true;

    this.clientService
      .findById(id)
      .pipe(finalize(() => (this.isLoading = false)))
      .subscribe({
        next: (c) => this.patchClient(c),
        error: (err) =>
          (this.errorMessage =
            this.getApiMessage(err) || 'Error cargando cliente'),
      });
  }

  private patchClient(c: Client): void {
    this.form.patchValue({
      name: c.name,
      gender: c.gender,
      age: c.age,
      identification: c.identification,
      address: c.address,
      phone: c.phone,
      active: c.active,
      password: '',
    });
  }

  private createClient(): void {
    this.isLoading = true;

    const payload = this.form.getRawValue();
    const req: Client = payload;

    this.clientService
      .create(req)
      .pipe(finalize(() => (this.isLoading = false)))
      .subscribe({
        next: () =>
          this.router.navigate(['/clients'], {
            queryParams: { msg: 'created' },
          }),
        error: (err) =>
          (this.errorMessage =
            this.getApiMessage(err) || 'Error creando cliente'),
      });
  }

  private updateClient(): void {
    if (this.id == null) return;

    this.isLoading = true;

    const payload = this.form.getRawValue();
    payload.password = 'secret';

    this.clientService
      .update(this.id, payload)
      .pipe(finalize(() => (this.isLoading = false)))
      .subscribe({
        next: () =>
          this.router.navigate(['/clients'], {
            queryParams: { msg: 'updated' },
          }),
        error: (err) =>
          (this.errorMessage =
            this.getApiMessage(err) || 'Error actualizando cliente'),
      });
  }

  private setPasswordOptional(): void {
    const ctrl = this.form.controls.password;
    ctrl.clearValidators();
    ctrl.setValue('');
    ctrl.updateValueAndValidity();
  }

  private setPasswordRequired(): void {
    const ctrl = this.form.controls.password;
    ctrl.setValidators([
      Validators.required,
      Validators.minLength(4),
      Validators.maxLength(120),
    ]);
    ctrl.updateValueAndValidity();
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
