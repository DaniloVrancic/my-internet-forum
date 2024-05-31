import { Component, OnInit } from '@angular/core';
import { NavigationBarComponent } from '../../partials/nav/navigation-bar/navigation-bar.component';
import { Router } from '@angular/router';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-main-page',
  standalone: true,
  imports: [NavigationBarComponent],
  templateUrl: './main-page.component.html',
  styleUrl: './main-page.component.css'
})
export class MainPageComponent implements OnInit{

  constructor(private router: Router){

  }

  ngOnInit(): void {
      let reload = sessionStorage.getItem(environment.needsReloadString);
      if(reload === "true"){
        sessionStorage.removeItem(environment.needsReloadString);
        window.location.reload();
      }
  }

  testButton() {
    location.reload();
    }
}
