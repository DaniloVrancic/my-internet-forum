import { Component } from '@angular/core';
import { NavigationBarComponent } from '../../../../partials/nav/navigation-bar/navigation-bar.component';

@Component({
  selector: 'app-selected-post',
  standalone: true,
  imports: [NavigationBarComponent],
  templateUrl: './selected-post.component.html',
  styleUrl: './selected-post.component.css'
})
export class SelectedPostComponent {

}
