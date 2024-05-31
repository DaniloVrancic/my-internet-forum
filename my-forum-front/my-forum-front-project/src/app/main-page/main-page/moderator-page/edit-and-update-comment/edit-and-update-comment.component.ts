import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import Comment from '../../../../../interfaces/comment';
import { ModeratorPageService } from '../../../../services/moderator-page.service';
import { environment } from '../../../../../environments/environment';
import { CommentsService } from '../../../../services/comments.service';
import { response } from 'express';

@Component({
  selector: 'app-edit-and-update-comment',
  standalone: true,
  imports: [],
  templateUrl: './edit-and-update-comment.component.html',
  styleUrl: './edit-and-update-comment.component.css',
  providers:[CommentsService]
})
export class EditAndUpdateCommentComponent implements OnInit{

  public possibleStatuses: string[] = environment.possibleStatuses;
  public isPostChanged: boolean = false;
  public selectedStatusName: string;

  private contentTextArea = document.getElementById("text-content") as any;

  constructor(@Inject(MAT_DIALOG_DATA) public caughtPost: Comment, 
                                       private moderatorPageService: ModeratorPageService,
                                       private commentsService: CommentsService){
        this.selectedStatusName = caughtPost.status;
  }

  public updateIsPostChanged(){
    let starterStatus = this.caughtPost.status;
    
    if((this.caughtPost.status !== this.selectedStatusName) || (this.contentTextArea?.value !== this.caughtPost.content)){
      this.isPostChanged = true;
    }
    else{
      this.isPostChanged = false;
    }

  }

  ngOnInit(): void {
      
  }

  selectStatusName(statusName: string) {
    this.selectedStatusName = statusName;
    }

  updatePostClick() {
    this.commentsService.updateComment({id: this.caughtPost.id, content: this.contentTextArea.value as string, status: this.selectedStatusName})
    .subscribe({next: response => console.log(response)});
    }
  
}
