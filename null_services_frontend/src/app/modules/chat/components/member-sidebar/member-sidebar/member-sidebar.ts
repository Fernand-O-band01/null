import { Component, Input } from '@angular/core';
import { CommonModule, UpperCasePipe
 } from '@angular/common';

@Component({
  selector: 'app-member-sidebar',
  standalone: true,
  imports: [CommonModule, UpperCasePipe],
  templateUrl: './member-sidebar.html',
  styleUrl: './member-sidebar.css',
})
export class MemberSidebar {

  @Input() members: any[] = []

}
