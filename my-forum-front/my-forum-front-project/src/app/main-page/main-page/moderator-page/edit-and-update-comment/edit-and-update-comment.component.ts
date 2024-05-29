import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import Comment from '../../../../../interfaces/comment';
import { ModeratorPageService } from '../../../../services/moderator-page.service';

@Component({
  selector: 'app-edit-and-update-comment',
  standalone: true,
  imports: [],
  templateUrl: './edit-and-update-comment.component.html',
  styleUrl: './edit-and-update-comment.component.css'
})
export class EditAndUpdateCommentComponent {

  constructor(@Inject(MAT_DIALOG_DATA) private caughtPost: Comment, 
                                       private moderatorPageService: ModeratorPageService){
  }

  
}
