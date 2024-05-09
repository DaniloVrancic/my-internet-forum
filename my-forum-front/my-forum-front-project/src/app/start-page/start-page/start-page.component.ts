import { Component } from '@angular/core';
import { Router } from '@angular/router';


@Component({
  selector: 'app-start-page',
  standalone: true,
  imports: [],
  templateUrl: './start-page.component.html',
  styleUrl: './start-page.component.css',
  providers: [Router]
})
export class StartPageComponent {

  constructor(private router: Router){}

  registerClick(){
    this.router.navigate(['/register-page']);
  }

  loginClick(){
    this.router.navigate(['/login-page']);
  }
}
