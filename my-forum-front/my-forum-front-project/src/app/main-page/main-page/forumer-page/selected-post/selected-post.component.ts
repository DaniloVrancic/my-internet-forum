import { Component, Inject, OnInit } from '@angular/core';
import { NavigationBarComponent } from '../../../../partials/nav/navigation-bar/navigation-bar.component';
import { ForumPost } from '../../../../../interfaces/forum-post';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Comment } from '../../../../../interfaces/comment';
import {MatFormFieldModule} from '@angular/material/form-field';
import { FormsModule } from '@angular/forms';
import { CommentsService } from '../../../../services/comments.service';
import { CommentRequest } from '../../../../../interfaces/requests/comment-request';
import { ForumerPageService } from '../../../../services/forumer-page.service';
import { UserService } from '../../../../services/user.service';

@Component({
  selector: 'app-selected-post',
  standalone: true,
  imports: [NavigationBarComponent, MatFormFieldModule, FormsModule],
  templateUrl: './selected-post.component.html',
  styleUrl: './selected-post.component.css',
  providers:[CommentsService, ForumerPageService, UserService]
})
export class SelectedPostComponent implements OnInit{

  commentsOnProgram: Comment[];
  commentToPost: string;

  constructor(@Inject(MAT_DIALOG_DATA) public caughtPost: ForumPost, 
  private commentService: CommentsService,
  private forumerPageService: ForumerPageService,
  private userService: UserService){
    this.commentsOnProgram = [];
    this.commentToPost = "";
  }

  ngOnInit(): void {
      this.commentService.findAllApprovedByPostId(this.caughtPost.id)
      .subscribe({
        next: response => {this.commentsOnProgram = response}
      });
  }

  postComment($event: MouseEvent) {
    let commentRequest: CommentRequest = {content: this.commentToPost, forum_post: this.caughtPost.id, user: this.userService.getCurrentUser()?.id as number}
    this.commentService.addComment(commentRequest).subscribe({
      next: response => { 
        if(this.userService.getCurrentUser()?.type == "Administrator" || this.userService.getCurrentUser()?.type == "Moderator")
        this.commentsOnProgram?.push(response); 
        alert("Comment successfully added.")},
      error: errorResponse => {alert("An error occured!");}  
    });
    }
}
