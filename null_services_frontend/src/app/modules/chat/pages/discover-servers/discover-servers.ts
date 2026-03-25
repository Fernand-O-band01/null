import { Component, OnInit, ChangeDetectorRef, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { ServerControllerService, ServerResponse } from '../../../../services/api';
import { DiscoverSidebar } from '../../components/discover-sidebar/discover-sidebar';

@Component({
  selector: 'app-discover-servers',
  standalone: true,
  imports: [CommonModule, DiscoverSidebar],
  templateUrl: './discover-servers.html',
  styleUrl: './discover-servers.css',
})
export class DiscoverServers implements OnInit {

  publicServers: ServerResponse[] = [];

  private serverService = inject(ServerControllerService)
  private cdr = inject(ChangeDetectorRef)
  private router = inject(Router)

  ngOnInit() {
    this.loadPublicServers();
  }

  loadPublicServers() {
    this.serverService.findAllServer().subscribe({
      next: (response) => {
        this.publicServers = response;
        console.log('Public servers loaded:', this.publicServers);
        this.cdr.detectChanges();
      },
      error: (error) => {
        console.error('Error loading public servers:', error);
      }
    })
  }

  joinServer(serverId: number | undefined, event: Event){
    if(!serverId) return

    event.stopPropagation()

    this.serverService.joinServer(serverId).subscribe({
      next: (response) => {
        console.log('You has join the server', response);

        window.dispatchEvent(new CustomEvent('server-joined'));

        this.router.navigate(['/home/server', serverId])
      },
      error: (err) => {
        console.log('Failed joining server', err);
        
      }
    })
  }

}
