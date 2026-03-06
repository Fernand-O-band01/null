import { Injectable } from '@angular/core';
import { RxStomp, RxStompConfig } from '@stomp/rx-stomp';

// 🚀 1. Importamos el environment
import { environment } from '../../../../environments/environment'; // <-- Ajusta los '../' según tu estructura

@Injectable({
  providedIn: 'root',
})
export class Websocket {
  public rxStomp: RxStomp = new RxStomp();

  // 🚀 Nivel Arquitecto: Conectar SOLO cuando sabemos que hay token
  public conectar() {
    // Si ya está conectado, no hacemos nada
    if (this.rxStomp.active) {
      return;
    }

    const token = localStorage.getItem('token');
    
    // Si no hay token (ej. pantalla de login), abortamos la conexión
    if (!token) {
      console.warn('⚠️ Intento de conexión WS abortado: No hay token aún.');
      return;
    }

    const rxStompConfig: RxStompConfig = {
      // 🚀 2. LA MAGIA: Usamos la URL del Gateway dinámicamente
      brokerURL: environment.wsUrl, 
      
      connectHeaders: {
        Authorization: `Bearer ${token}`
      },
      heartbeatIncoming: 20000,
      heartbeatOutgoing: 20000,
      reconnectDelay: 1000,
      debug: (msg: string): void => {
        console.log(new Date(), msg);
      },
    };

    this.rxStomp.configure(rxStompConfig);
    this.rxStomp.activate();
    console.log('🔌 WebSocket activado a través del Gateway!');
  }
}