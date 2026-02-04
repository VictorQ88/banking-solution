import { Routes } from '@angular/router';

import { Shell } from './layout/shell/shell';
import { Home } from './pages/home/home';
import { Clients } from './pages/clients/clients';
import { Accounts } from './pages/accounts/accounts';
import { Movements } from './pages/movements/movements';
import { Reports } from './pages/reports/reports';

export const routes: Routes = [
  {
    path: '',
    component: Shell,
    children: [
      { path: '', pathMatch: 'full', component: Home },
      { path: 'clients', component: Clients },
      { path: 'accounts', component: Accounts },
      { path: 'movements', component: Movements },
      { path: 'reports', component: Reports },
    ],
  },
  { path: '**', redirectTo: '' },
];
