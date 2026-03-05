import { ApplicationConfig, provideBrowserGlobalErrorListeners } from '@angular/core';
import { provideRouter } from '@angular/router';
import { routes } from './app.routes';
import { httpTokenInterceptor } from './services/api/interceptor/http-token-interceptor';
import { provideHttpClient, withInterceptors } from '@angular/common/http';

import { environment } from '../environments/environment';
import { BASE_PATH } from './services/api';

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideRouter(routes),
    provideHttpClient(withInterceptors([httpTokenInterceptor])),

    {provide: BASE_PATH, useValue: environment.apiUrl}
  ]
};
