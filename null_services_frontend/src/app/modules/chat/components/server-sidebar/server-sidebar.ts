import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ServerControllerService } from '../../../../services/api';
import { ServerResponse } from '../../../../services/api';

@Component({
  selector: 'app-server-sidebar',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './server-sidebar.html',
  styleUrl: './server-sidebar.css',
})
export class ServerSidebar {

  servers: ServerResponse[] = [];
  constructor( private serverService: ServerControllerService) {

  }

  ngOnInit() {
    this.loadServers();
  }

  loadServers() {
    this.serverService.findMyServers().subscribe({
      next: (response) => {
        this.servers = response;
        console.log('Servers loaded:', this.servers);
      },
      error: (error) => {
        console.error('Error loading servers:', error);
      }
    })
  }

  createServer() {
    
  }

}
