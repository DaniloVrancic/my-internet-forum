import { Routes } from '@angular/router';
import { StartPageComponent } from './start-page/start-page/start-page.component';
import { RegisterPageComponent } from './register-page/register-page/register-page.component';
import { LoginPageComponent } from './login-page/login-page/login-page.component';
import { MainPageComponent } from './main-page/main-page/main-page.component';
import { VerifyCodePageComponent } from '../verify-code-page/verify-code-page.component';
import { AdminPageComponent } from './main-page/main-page/admin-page/admin-page.component';
import { ModeratorPageComponent } from './main-page/main-page/moderator-page/moderator-page.component';
import { ForumerPageComponent } from './main-page/main-page/forumer-page/forumer-page.component';
import { SelectedPostComponent } from './main-page/main-page/forumer-page/selected-post/selected-post.component';
import { authGuard } from './services/guard/auth.guard';
import { loggedUserGuard } from './services/guard/logged-user.guard';
import { moderatorGuard } from './services/guard/moderator.guard';
import { administratorGuard } from './services/guard/administrator.guard';
import { CallbackComponent } from './start-page/callback/callback.component';

export const routes: Routes = [
    { path: 'start-page', component: StartPageComponent},
    { path: 'register-page', component: RegisterPageComponent},
    { path: 'login-page', component: LoginPageComponent},
    { path: 'main-page', component: MainPageComponent, canActivate: [authGuard]},
    { path: 'verify-page', component: VerifyCodePageComponent, canActivate: [loggedUserGuard]},
    { path: 'admin-page', component: AdminPageComponent, canActivate: [authGuard, loggedUserGuard, administratorGuard]},
    { path: 'moderator-page', component: ModeratorPageComponent, canActivate: [authGuard, loggedUserGuard, moderatorGuard]},
    { path: 'forumer-page', component: ForumerPageComponent, canActivate: [authGuard, loggedUserGuard]},
    { path: 'selected-post', component: SelectedPostComponent, canActivate: [authGuard, loggedUserGuard]},
    { path: 'callback', component: CallbackComponent},
    { path: '',   redirectTo: '/start-page', pathMatch: 'full' }
];
