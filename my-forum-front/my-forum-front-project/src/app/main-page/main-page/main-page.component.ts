import { Component } from '@angular/core';
import { NavigationBarComponent } from '../../partials/nav/navigation-bar/navigation-bar.component';

@Component({
  selector: 'app-main-page',
  standalone: true,
  imports: [NavigationBarComponent],
  templateUrl: './main-page.component.html',
  styleUrl: './main-page.component.css'
})
export class MainPageComponent {

}
