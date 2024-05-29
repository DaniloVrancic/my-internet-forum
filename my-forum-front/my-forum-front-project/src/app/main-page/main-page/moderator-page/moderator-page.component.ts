import { Component } from '@angular/core';
import { NavigationBarComponent } from '../../../partials/nav/navigation-bar/navigation-bar.component';
import { CommentsService } from '../../../services/comments.service';

@Component({
  selector: 'app-moderator-page',
  standalone: true,
  imports: [NavigationBarComponent],
  templateUrl: './moderator-page.component.html',
  styleUrl: './moderator-page.component.css',
  providers: [CommentsService]
})
export class ModeratorPageComponent {
  allComments: Comment[];
  pendingComments: Comment[];

  constructor(commentsService: CommentsService){
    this.allComments = [];
    this.pendingComments = [];
  };

}
