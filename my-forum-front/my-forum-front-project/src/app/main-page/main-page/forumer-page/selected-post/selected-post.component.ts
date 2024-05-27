import { Component, Inject } from '@angular/core';
import { NavigationBarComponent } from '../../../../partials/nav/navigation-bar/navigation-bar.component';
import { ForumPost } from '../../../../../interfaces/forum-post';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Comment } from '../../../../../interfaces/comment';
import {MatFormFieldModule} from '@angular/material/form-field';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-selected-post',
  standalone: true,
  imports: [NavigationBarComponent, MatFormFieldModule, FormsModule],
  templateUrl: './selected-post.component.html',
  styleUrl: './selected-post.component.css'
})
export class SelectedPostComponent {

  commentsOnProgram: Comment[];
  commentToPost: string;

  constructor(@Inject(MAT_DIALOG_DATA) public caughtPost: ForumPost){
    this.commentsOnProgram = [];
    this.commentToPost = "";
  }

  postComment($event: MouseEvent) {
    throw new Error('Method not implemented.');
    //TODO: Implement
    }
}
