import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ClientsComponent } from './clients.component';
import { ClientService } from '../../services/client.service';
import { Client } from '../../models/client.model';

describe('ClientsComponent', () => {
  let fixture: ComponentFixture<ClientsComponent>;
  let component: ClientsComponent;

  const clientServiceMock = {
    findAll: jasmine.createSpy('findAll'),
    delete: jasmine.createSpy('delete'),
  };

  const activatedRouteMock = {
    snapshot: {
      queryParamMap: {
        get: (_key: string) => null,
      },
      paramMap: {
        get: (_key: string) => null,
      },
    },
  };

  beforeEach(async () => {
    const data: Client[] = [
      {
        id: 1,
        name: 'Jose Lema',
        gender: 'M',
        age: 30,
        identification: '0102030406',
        address: 'Av. Siempre Viva',
        phone: '0999999999',
        clientId: 'C-0001',
        active: true,
        password: '',
      },
    ];

    clientServiceMock.findAll.and.returnValue(of(data));

    await TestBed.configureTestingModule({
      declarations: [ClientsComponent],
      imports: [FormsModule, RouterTestingModule],
      providers: [
        { provide: ClientService, useValue: clientServiceMock },
        { provide: ActivatedRoute, useValue: activatedRouteMock },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(ClientsComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load clients on init', () => {
    fixture.detectChanges();

    expect(clientServiceMock.findAll).toHaveBeenCalled();
    expect(component.clients.length).toBe(1);
    expect(component.filteredClients.length).toBe(1);
    expect(component.filteredClients[0].name).toBe('Jose Lema');
  });
});
