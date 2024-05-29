import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ForumPost } from '../../../../../interfaces/forum-post';
import { CommentsService } from '../../../../services/comments.service';
import { ModeratorPageService } from '../../../../services/moderator-page.service';

@Component({
  selector: 'app-edit-and-update-forum-post',
  standalone: true,
  imports: [],
  templateUrl: './edit-and-update-forum-post.component.html',
  styleUrl: './edit-and-update-forum-post.component.css'
})
export class EditAndUpdateForumPostComponent {

  constructor(@Inject(MAT_DIALOG_DATA) public caughtPost: ForumPost, 
  private moderatorPageService: ModeratorPageService){
    
  }

}
