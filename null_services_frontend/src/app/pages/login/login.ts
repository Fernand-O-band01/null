import { Component } from '@angular/core';
import { AuthenticationRequest } from '../../services/api';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthenticationService } from '../../services/api';
import { Token } from '../../services/api/token/token';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {

  authRequest: AuthenticationRequest = {
    username: '',
    password: ''
  };

  errorMessage: Array<string> = [];

  constructor(
    private authService: AuthenticationService,
    private router: Router,
    private tokenService: Token
  ) {
    
  }

  login() {
    this.errorMessage = [];
    this.authService.authenticate(this.authRequest).subscribe({
      next: (response) => {
        this.tokenService.token = response.token as string;
        console.log('Inicio de Sesion Exitoso', response);
        this.router.navigate(['/dashboard']);

      },
      error: (error) => {

        console.error('Error al iniciar sesión', error);

        if(error.error.ValidationErrors) {
          this.errorMessage = error.error.ValidationErrors
        }else{
          this.errorMessage.push(error.error.message);
        }

      }
    });
  }

  register() {
    this.router.navigate(['/register']);
  }

}
