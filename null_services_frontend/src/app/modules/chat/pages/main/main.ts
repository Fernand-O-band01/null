import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { ServerSidebar } from "../../components/server-sidebar/server-sidebar";

@Component({
  selector: 'app-main',
  imports: [RouterOutlet, ServerSidebar],
  templateUrl: './main.html',
  styleUrl: './main.css',
})
export class Main {

}
