import { Component, inject } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { ServerSidebar } from "../../components/server-sidebar/server-sidebar";
import { UserPanel } from '../../components/user-panel/user-panel';

import { UserSettingsModal } from '../../components/modals/user-settings-modal/user-settings-modal/user-settings-modal';
import { Modalservice } from '../../../../services/api/modalservice/modalservice';
import { CommonModule } from '@angular/common';


@Component({
  selector: 'app-main',
  imports: [RouterOutlet, ServerSidebar, UserPanel, CommonModule, UserSettingsModal],
  templateUrl: './main.html',
  styleUrl: './main.css',
})
export class Main {

  public modalService = inject(Modalservice)


}
