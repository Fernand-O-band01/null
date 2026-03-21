import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MemberResponse } from '../../../../../../../../services/api';

@Component({
  selector: 'app-member-profile-pop-up',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './member-profile-pop-up.html',
  styleUrl: './member-profile-pop-up.css',
})
export class MemberProfilePopUp {

@Input({required: true}) member!: MemberResponse;
@Output() closePopUp = new EventEmitter<void>();
@Input() topPosition = 0;

}
