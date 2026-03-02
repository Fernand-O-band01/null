import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { Main } from './pages/main/main';
import { authGuard } from '../../services/api/guard/authguard';
import { Home } from './pages/home/home/home';
import { DiscoverServers } from './pages/discover-servers/discover-servers';
import { Server } from './pages/server/server/server';

const routes: Routes = [

  {
    path: '',
    component: Main,
    canActivate: [authGuard],
    children: [
      {
        path: '',
        component: Home,
        canActivate: [authGuard]
      },
      {
        path: 'discover',
        component: DiscoverServers,
        canActivate: [authGuard]
      },
      {
        path: 'server/:serverId',
        component: Server,
        canActivate: [authGuard]
      }
    ]
  }

];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ChatRoutingModule { }
