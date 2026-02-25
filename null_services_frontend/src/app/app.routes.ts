import { Routes } from '@angular/router';
import { Login } from './pages/login/login';
import { Register } from './pages/register/register';
import { ActivateAccount } from './pages/activate-account/activate-account';
import { authGuard } from './services/api/guard/authguard';

export const routes: Routes = [
    {
        path: '',
        redirectTo: 'login',
        pathMatch: 'full'
    },
    {
        path: 'login',
        component: Login
    },
    {
        path: 'register',
        component: Register
    },
    {
        path: 'activate-account',
        component: ActivateAccount
    },
    {
        path: 'home',
        loadChildren: () => import('./modules/chat/chat-module').then(m => m.ChatModule),
        canActivate: [authGuard]
    }
];
