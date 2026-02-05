import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { finalize } from 'rxjs/operators';

import { MovementService } from '../../../services/movement.service';
import { Movement } from '../../../models/movement.model';

@Component({
  selector: 'app-movement-form',
  standalone: false,
  templateUrl: './movement-form.component.html',
  styleUrls: ['./movement-form.component.css'],
})
export class MovementFormComponent implements OnInit {
  isLoading = false;
  errorMessage = '';
  form: any;

  constructor(
    private readonly fb: FormBuilder,
    private readonly router: Router,
    private readonly route: ActivatedRoute,
    private readonly movementService: MovementService,
  ) {}

  ngOnInit(): void {
    const accountIdParam = this.route.snapshot.queryParamMap.get('accountId');

    this.form = this.fb.nonNullable.group({
      accountId: [accountIdParam ? Number(accountIdParam) : 0, [Validators.required, Validators.min(1)]],
      movementType: ['', [Validators.required]],
      value: [0, [Validators.required, Validators.min(0.01)]],
    });
  }

  cancel(): void {
    this.router.navigate(['/movements']);
  }

  submit(): void {
    this.errorMessage = '';
    this.form.markAllAsTouched();
    if (this.form.invalid) return;

    this.isLoading = true;

    const payload = this.form.getRawValue();

    // backend calcula balance y normaliza signo según movementType
    const req: any = {
      accountId: payload.accountId,
      movementType: payload.movementType,
      value: payload.value,
    };

    this.movementService
      .create(req as Movement)
      .pipe(finalize(() => (this.isLoading = false)))
      .subscribe({
        next: () =>
          this.router.navigate(['/movements'], {
            queryParams: { msg: 'created' },
          }),
        error: (err) =>
          (this.errorMessage =
            this.getApiMessage(err) || 'Error registrando movimiento'),
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