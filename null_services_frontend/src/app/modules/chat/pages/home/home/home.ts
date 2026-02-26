import { Component } from '@angular/core';
import { DmSidebar } from "../../../components/dm-sidebar/dm-sidebar";
import { CommonModule } from '@angular/common';

import { FriendsOnline } from '../friends-online/friends-online/friends-online';
import { FriendsAll } from '../friends-all/friends-all/friends-all';
import { FriendsPending } from '../friends-pending/friends-pending/friends-pending';
import { FriendAdd } from '../friends-add/friend-add/friend-add';

type tabType = 'ONLINE' | 'ALL' | 'PENDING' | 'ADD';

@Component({
  selector: 'app-home',
  imports: [DmSidebar, CommonModule, FriendsOnline, FriendsAll, FriendsPending, FriendAdd],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home  {

  activeTab: tabType = 'ALL';
  setTab(tab: tabType) {
    this.activeTab = tab;
  }

}
