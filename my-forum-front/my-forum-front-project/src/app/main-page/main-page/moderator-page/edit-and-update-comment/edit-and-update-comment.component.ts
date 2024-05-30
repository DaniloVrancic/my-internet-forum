import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import Comment from '../../../../../interfaces/comment';
import { ModeratorPageService } from '../../../../services/moderator-page.service';
import { environment } from '../../../../../environments/environment';

@Component({
  selector: 'app-edit-and-update-comment',
  standalone: true,
  imports: [],
  templateUrl: './edit-and-update-comment.component.html',
  styleUrl: './edit-and-update-comment.component.css'
})
export class EditAndUpdateCommentComponent implements OnInit{

  public possibleStatuses: string[] = environment.possibleStatuses;
  public isPostChanged: boolean = false;

  constructor(@Inject(MAT_DIALOG_DATA) public caughtPost: Comment, 
                                       private moderatorPageService: ModeratorPageService){
  }

  private updateIsPostChanged(){
    let contentTextArea = document.getElementById("text-content");

    //TODO: Finish toggling the isPostChanged value to true if the values don't match the starting values of the fields in the radio button and the text area

  }

  ngOnInit(): void {
      
  }

  selectStatusName(statusName: any) {
    throw new Error('Method not implemented.');
    }

  updatePostClick() {
    throw new Error('Method not implemented.');
    }
  
}
