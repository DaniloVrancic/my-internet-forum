import { Component } from '@angular/core';
import { NavigationBarComponent } from '../../../partials/nav/navigation-bar/navigation-bar.component';
import { ForumerPageService } from '../../../services/forumer-page.service';

@Component({
  selector: 'app-forumer-page',
  standalone: true,
  imports: [NavigationBarComponent],
  templateUrl: './forumer-page.component.html',
  styleUrl: './forumer-page.component.css',
  providers: [ForumerPageService]
})
export class ForumerPageComponent {

  constructor(private forumerPageService: ForumerPageService){
    
  }

}
