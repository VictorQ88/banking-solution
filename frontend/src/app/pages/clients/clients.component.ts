import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { finalize } from 'rxjs/operators';

import { Client } from '../../models/client.model';
import { ClientService } from '../../services/client.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-clients',
  standalone: false,
  templateUrl: './clients.component.html',
  styleUrls: ['./clients.component.css'],
})
export class ClientsComponent implements OnInit {
  clients: Client[] = [];
  filteredClients: Client[] = [];
  searchText = '';

  isLoading = false;
  errorMessage = '';
  messageText = '';
  messageType: 'success' | 'error' = 'success';

  constructor(
    private readonly clientService: ClientService,
    private readonly router: Router,
    private readonly route: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.handleMessage();
    this.loadClients();
  }

  handleMessage(): void {
    const msg = this.route.snapshot.queryParamMap.get('msg');

    if (msg === 'created') {
      this.messageType = 'success';
      this.messageText = 'Cliente creado correctamente';
    } else if (msg === 'updated') {
      this.messageType = 'success';
      this.messageText = 'Cliente actualizado correctamente';
    }
    
    console.log("🚀 ~ ClientsComponent ~ handleMessage ~ this.messageType :", this.messageType )
    if (msg) {
      setTimeout(() => (this.messageText = ''), 3000);
      this.router.navigate([], { queryParams: {} });
    }
  }

  loadClients(): void {
    this.isLoading = true;
    this.errorMessage = '';

    this.clientService
      .findAll()
      .pipe(finalize(() => (this.isLoading = false)))
      .subscribe({
        next: (data) => {
          this.clients = Array.isArray(data) ? data : [];
          this.applyFilter();
        },
        error: (err) => {
          this.errorMessage =
            this.getApiMessage(err) || 'Error cargando clientes';
          this.clients = [];
          this.filteredClients = [];
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
      this.filteredClients = [...this.clients];
      return;
    }

    this.filteredClients = this.clients.filter((c) => {
      const hay =
        `${c.name} ${c.identification} ${c.phone} ${c.clientId}`.toLowerCase();
      return hay.includes(q);
    });
  }

  goCreate(): void {
    this.router.navigate(['/clients/new']);
  }

  goEdit(client: Client): void {
    this.router.navigateByUrl(`/clients/${client.id}/edit`);
  }

  deleteClient(client: Client): void {
    if (!window.confirm(`Eliminar cliente "${client.name}"?`)) return;

    this.isLoading = true;
    this.errorMessage = '';

    this.clientService
      .delete(client.id)
      .pipe(finalize(() => (this.isLoading = false)))
      .subscribe({
        next: () => {
          this.clients = this.clients.filter((x) => x.id !== client.id);
          this.applyFilter();
        },
        error: (err) => {
          this.errorMessage =
            this.getApiMessage(err) || 'Error eliminando cliente';
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

  trackById(_: number, item: Client) {
    return item.id;
  }
}
