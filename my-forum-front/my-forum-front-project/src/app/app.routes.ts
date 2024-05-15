import { Routes } from '@angular/router';
import { StartPageComponent } from './start-page/start-page/start-page.component';
import { RegisterPageComponent } from './register-page/register-page/register-page.component';
import { LoginPageComponent } from './login-page/login-page/login-page.component';
import { MainPageComponent } from './main-page/main-page/main-page.component';
import { VerifyCodePageComponent } from '../verify-code-page/verify-code-page.component';
import { AdminPageComponent } from './main-page/main-page/admin-page/admin-page.component';
import { ModeratorPageComponent } from './main-page/main-page/moderator-page/moderator-page.component';
import { ForumerPageComponent } from './main-page/main-page/forumer-page/forumer-page.component';

export const routes: Routes = [
    { path: 'start-page', component: StartPageComponent},
    { path: 'register-page', component: RegisterPageComponent},
    { path: 'login-page', component: LoginPageComponent},
    { path: 'main-page', component: MainPageComponent},
    { path: 'verify-page', component: VerifyCodePageComponent},
    { path: 'admin-page', component: AdminPageComponent},
    { path: 'moderator-page', component: ModeratorPageComponent},
    { path: 'forumer-page', component: ForumerPageComponent},
    { path: '',   redirectTo: '/start-page', pathMatch: 'full' }
];
