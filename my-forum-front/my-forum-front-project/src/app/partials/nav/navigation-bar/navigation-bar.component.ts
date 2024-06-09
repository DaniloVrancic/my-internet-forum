import { Component, OnInit, inject } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from '../../../services/user.service';

@Component({
  selector: 'app-navigation-bar',
  standalone: true,
  imports: [],
  templateUrl: './navigation-bar.component.html',
  styleUrl: './navigation-bar.component.css'
})
export class NavigationBarComponent implements OnInit{
;
constructor(private router: Router, private userService: UserService){
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

  

  this.userService.logoutUser(this.userService.getCurrentUser()?.id as number).subscribe((booleanResponse: boolean) =>{
    if(booleanResponse === true){
      sessionStorage.clear();
      localStorage.clear();
      this.router.navigate(['']);
    }
  });
  }

}
