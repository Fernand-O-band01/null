import { Component } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { AuthenticationService } from '../../services/api';
import { CodeInputModule } from 'angular-code-input';

@Component({
  selector: 'app-activate-account',
  imports: [CodeInputModule, RouterLink],
  templateUrl: './activate-account.html',
  styleUrl: './activate-account.css',
})
export class ActivateAccount {

  errorMessage = ' ';

  constructor(
    private authService: AuthenticationService,
    private router: Router
  ) {

  }

  onCodeCompleted(code: string) {
    this.errorMessage = ' ';
    this.authService.confirm(code).subscribe({
      next: () => {
        console.log("Cuenta activada con exito!");
        this.router.navigate(['/login']);
      },
      error: (err) => {
        console.log("Error al activar", err);
        this.errorMessage = "Código inválido o expirado.";
      }
    })
  }

}
