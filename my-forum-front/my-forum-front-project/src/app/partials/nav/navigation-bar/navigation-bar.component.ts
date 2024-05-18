import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-navigation-bar',
  standalone: true,
  imports: [],
  templateUrl: './navigation-bar.component.html',
  styleUrl: './navigation-bar.component.css'
})
export class NavigationBarComponent{
constructor(private router: Router){}



navigateToForumerPage() {
  this.router.navigate(['forumer-page']);
}
navigateToModeratorPage() {
  this.router.navigate(['moderator-page']);
}
navigateToAdminPage() {
  this.router.navigate(['admin-page']);
}

logoutButton(){
  sessionStorage.clear();
  this.router.navigate(['']);
}

}
