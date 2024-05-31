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
  }
public navigateToModeratorPage() {
  this.router.navigate(['moderator-page']);
  }
public navigateToAdminPage() {
  this.router.navigateByUrl('admin-page');
  }

public logoutButton(){
  sessionStorage.clear();
  this.router.navigate(['']);
  }

}
