import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { Main } from './pages/main/main';
import { authGuard } from '../../services/api/guard/authguard';
import { ChatRoom } from './pages/chat-room/chat-room';

const routes: Routes = [

  {
    path: '',
    component: Main,
    canActivate: [authGuard],
    children: [
      {
        path: 'server/:serverId/channel/:channelId',
        component: ChatRoom,
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
