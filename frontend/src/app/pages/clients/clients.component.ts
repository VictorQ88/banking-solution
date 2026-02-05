import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { Client, ClientCreateRequest, ClientUpdateRequest } from './client.model';
import { ClientService } from './client.service';

type Mode = 'create' | 'edit';

@Component({
  selector: 'app-clients',
  standalone: true,
 imports: [CommonModule, ReactiveFormsModule, FormsModule],
  templateUrl: './clients.html',
  styleUrl: './clients.css',
})
export class ClientsComponent implements OnInit {
  clients: Client[] = [];
  filteredClients: Client[] = [];
  searchText = '';

  isLoading = false;
  errorMessage = '';

  isModalOpen = false;
  mode: Mode = 'create';
  editingId: number | null = null;
  form: any;

  constructor(
    private readonly fb: FormBuilder,
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
      password: ['', [Validators.required, Validators.minLength(4), Validators.maxLength(120)]],
      active: [true, [Validators.required]],
    });

    this.load();
  }

  load(): void {
    this.isLoading = true;
    this.errorMessage = '';

    this.clientService.findAll().subscribe({
      next: (data) => {
        console.log("🚀 ~ ClientsComponent ~ load ~ data:", data)
        this.clients = data;
        this.applyFilter();
      },
      error: (err) => (this.errorMessage = this.getApiMessage(err) || 'Error loading clients'),
      complete: () => {
        console.log("🚀 ~ ClientsComponent ~ load ~ complete");
        this.isLoading = false;},
    });
  }

  onSearchChange(value: string): void {
    this.searchText = value;
    this.applyFilter();
  }

  applyFilter(): void {
    const q = this.searchText.trim().toLowerCase();
    if (!q) {
      this.filteredClients = this.clients;
      return;
    }

    this.filteredClients = this.clients.filter((c) => {
      const hay = `${c.name} ${c.identification} ${c.phone} ${c.clientId}`.toLowerCase();
      return hay.includes(q);
    });
  }

  openCreate(): void {
    this.mode = 'create';
    this.editingId = null;
    this.form.reset({
      name: '',
      gender: '',
      age: 18,
      identification: '',
      address: '',
      phone: '',
      password: '',
      active: true,
    });
    this.isModalOpen = true;
  }

  openEdit(client: Client): void {
    console.log("🚀 ~ ClientsComponent ~ openEdit ~ client:", client)
    this.mode = 'edit';
    this.editingId = client.id;

    this.form.reset({
      name: client.name,
      gender: client.gender,
      age: client.age,
      identification: client.identification,
      address: client.address,
      phone: client.phone,
      password: '',
      active: client.active,
    });

    this.isModalOpen = true;
  }

  closeModal(): void {
    this.isModalOpen = false;
    this.errorMessage = '';
  }

  submit(): void {
    this.form.markAllAsTouched();
    if (this.form.invalid) return;

    this.isLoading = true;
    this.errorMessage = '';

    const payload = this.form.getRawValue();

    if (this.mode === 'create') {
      const req: ClientCreateRequest = payload;
      this.clientService.create(req).subscribe({
        next: (created) => {
          this.clients = [created, ...this.clients];
          this.applyFilter();
          this.closeModal();
        },
        error: (err) => (this.errorMessage = this.getApiMessage(err) || 'Error creating client'),
        complete: () => (this.isLoading = false),
      });
      return;
    }

    if (this.editingId == null) return;

    const req: ClientUpdateRequest = payload;
    this.clientService.update(this.editingId, req).subscribe({
      next: (updated) => {
        this.clients = this.clients.map((c) => (c.id === updated.id ? updated : c));
        this.applyFilter();
        this.closeModal();
      },
      error: (err) => (this.errorMessage = this.getApiMessage(err) || 'Error updating client'),
      complete: () => (this.isLoading = false),
    });
  }

  confirmDelete(c: Client): void {
    if (!window.confirm(`Delete client "${c.name}"?`)) return;

    this.isLoading = true;
    this.errorMessage = '';

    this.clientService.delete(c.id).subscribe({
      next: () => {
        this.clients = this.clients.filter((x) => x.id !== c.id);
        this.applyFilter();
      },
      error: (err) => (this.errorMessage = this.getApiMessage(err) || 'Error deleting client'),
      complete: () => (this.isLoading = false),
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
