import { Routes } from '@angular/router';
import { StartPageComponent } from './start-page/start-page/start-page.component';
import { RegisterPageComponent } from './register-page/register-page/register-page.component';
import { LoginPageComponent } from './login-page/login-page/login-page.component';
import { MainPageComponent } from './main-page/main-page/main-page.component';

export const routes: Routes = [
    { path: 'start-page', component: StartPageComponent},
    { path: 'register-page', component: RegisterPageComponent},
    { path: 'login-page', component: LoginPageComponent},
    { path: 'main-page', component: MainPageComponent},
    { path: '',   redirectTo: '/start-page', pathMatch: 'full' }
];
