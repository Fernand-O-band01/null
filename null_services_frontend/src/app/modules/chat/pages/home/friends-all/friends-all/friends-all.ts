import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';

import { FriendsControllerService } from '../../../../../../services/api/api/friendsController.service';
import { FriendResponseDTO } from '../../../../../../services/api/model/friendResponseDTO';

@Component({
  selector: 'app-friends-all',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './friends-all.html',
  styleUrl: './friends-all.css',
})
export class FriendsAll implements OnInit {

  FriendsList: FriendResponseDTO[] = [];

  constructor
  (
    private friendsControllerService: FriendsControllerService,
    private cdr: ChangeDetectorRef

  ) {}

  ngOnInit(): void {
      this.fetchFriends();
  }

  fetchFriends(): void {

    this.friendsControllerService.getMyFriends().subscribe({
      next: (data) => {
        this.FriendsList = data;
        console.log('Friends loaded in Friends All Tab:', this.FriendsList);

        this.cdr.detectChanges();
        
      },
      error: (error) => {
        console.error('Error fetching friends:', error);
      }
    });

  }



}
