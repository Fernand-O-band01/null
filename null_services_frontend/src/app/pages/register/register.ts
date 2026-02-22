import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthenticationService } from '../../services/api';
import { RegistrationRequest } from '../../services/api';

@Component({
  selector: 'app-register',
  imports: [FormsModule, RouterLink],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register {

  registrationRequest: RegistrationRequest = {
    email: '',
    fullname: '',
    nickName: '',
    password: '',
    dateOfBirth: ''
  };

  selectedDay: string = '';
  selectedMonth: string = '';
  selectedYear: string = '';  

  days: number[] = Array.from({ length: 31 }, (_, i) => i + 1);
  years: number[] = Array.from({ length: 100 }, (_, i) => new Date().getFullYear() - i);
  months = [
    { value: '01', name: 'Enero' },
    { value: '02', name: 'Febrero' },
    { value: '03', name: 'Marzo' },
    { value: '04', name: 'Abril' },
    { value: '05', name: 'Mayo' },
    { value: '06', name: 'Junio' },
    { value: '07', name: 'Julio' },
    { value: '08', name: 'Agosto' },
    { value: '09', name: 'Septiembre' },
    { value: '10', name: 'Octubre' },
    { value: '11', name: 'Noviembre' },
    { value: '12', name: 'Diciembre' }]

  errorMessage: Array<string> = [];

  constructor(
    private authService: AuthenticationService,
    private router: Router
  ) {

  }

  register() {

    // 1. Validar que el usuario sí haya seleccionado las 3 cosas
    if (!this.selectedYear || !this.selectedMonth || !this.selectedDay) {
      this.errorMessage = ['Por favor, selecciona tu fecha de nacimiento completa.'];
      return; // Detenemos la ejecución para que no envíe datos incompletos
    }

    // 2. Formatear el día para que siempre tenga 2 dígitos (ej: el día '5' pasa a ser '05')
    const diaFormateado = this.selectedDay.toString().padStart(2, '0');
    
    // 3. Fusionar todo con guiones (El mes ya lo teníamos con 2 dígitos desde el HTML)
    const fechaArmada = `${this.selectedYear}-${this.selectedMonth}-${diaFormateado}`;
    
    // 4. Asignar la fecha fusionada al request generado por OpenAPI
    this.registrationRequest.dateOfBirth = fechaArmada;
    
    this.errorMessage = [];
    this.authService.register(this.registrationRequest).subscribe({
      next: (response) => {
        console.log('Registro Exitoso', response);
        this.router.navigate(['/activate-account']);
      },
      error: (error) => {
        this.errorMessage = error.error.ValidationErrors || [error.error.message];
      }

    });
  }

  login() {
    this.router.navigate(['/login']);
  }

}
