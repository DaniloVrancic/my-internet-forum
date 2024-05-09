import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register-page',
  standalone: true,
  imports: [],
  templateUrl: './register-page.component.html',
  styleUrl: './register-page.component.css',
  providers: [Router]
})
export class RegisterPageComponent {
constructor(private router: Router){}

onSubmit() {
throw new Error('Method not implemented.');
}

}
