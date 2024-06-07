import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-navigation-bar',
  standalone: true,
  imports: [],
  templateUrl: './navigation-bar.component.html',
  styleUrl: './navigation-bar.component.css'
})
export class NavigationBarComponent implements OnInit{
constructor(private router: Router){


}

ngOnInit(): void {
  
}

public navigateToForumerPage() {
  this.router.navigate(['forumer-page']);
  //console.log("Navigate to Forumer Page");
  }
public navigateToModeratorPage() {
  this.router.navigate(['moderator-page']);
  //console.log("Navigate to Moderator Page");
  }
public navigateToAdminPage() {
  this.router.navigate(['admin-page']);
  //console.log("Navigate to Admin Page");
  }

public logoutButton(){
  //console.log("Logout Button Clicked");
  sessionStorage.clear();
  localStorage.clear();
  this.router.navigate(['']);
  }

}
