import { Component } from '@angular/core';
import { AuthenticationRequest } from '../../services/api';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthenticationService } from '../../services/api';
import { Token } from '../../services/api/token/token';

@Component({
  selector: 'app-login',
  imports: [FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {

  authRequest: AuthenticationRequest = {
    username: '',
    password: ''
  };

  errorMessage: string = '';

  constructor(
    private authService: AuthenticationService,
    private router: Router,
    private tokenService: Token
  ) {
    
  }

  login() {

  }

  register() {
    this.router.navigate(['/register']);
  }

}
