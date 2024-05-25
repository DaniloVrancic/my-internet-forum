import { Component, Inject } from '@angular/core';
import { NavigationBarComponent } from '../../../../partials/nav/navigation-bar/navigation-bar.component';
import { ForumPost } from '../../../../../interfaces/forum-post';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-selected-post',
  standalone: true,
  imports: [NavigationBarComponent],
  templateUrl: './selected-post.component.html',
  styleUrl: './selected-post.component.css'
})
export class SelectedPostComponent {

  constructor(@Inject(MAT_DIALOG_DATA) public caughtPost: ForumPost){

  }
}
