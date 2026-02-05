import { Component, OnInit } from '@angular/core';
import { ReportService } from '../../services/report.service';
import { ClientService } from '../../services/client.service';

@Component({
  selector: 'app-reports',
  standalone: false,
  templateUrl: './reports.component.html',
})
export class ReportsComponent implements OnInit {
  clients: any[] = [];
  clientId!: number;
  from!: string;
  to!: string;

  report: any;
  errorMessage = '';
  isLoading = false;

  constructor(
    private reportService: ReportService,
    private clientService: ClientService,
  ) {}

  ngOnInit(): void {
    this.clientService.findAll().subscribe((c) => (this.clients = c));
  }

  generate(): void {
    this.errorMessage = '';
    this.isLoading = true;

    this.reportService
      .getAccountStatement(this.clientId, this.from, this.to)
      .subscribe({
        next: (r) => {
          this.report = r;
          this.isLoading = false;
        },
        error: (e) => {
          this.errorMessage = e?.error?.message || 'Error generando reporte';
          this.isLoading = false;
        },
      });
  }

  downloadPdf(): void {
    window.print();
  }
}
